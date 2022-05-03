package com.example.discovery.Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.discovery.Adapter.ViewPagerAdapter;
import com.example.discovery.Data.FavoriteDao;
import com.example.discovery.Data.VisiteDao;
import com.example.discovery.Models.Park;
import com.example.discovery.Models.ParkViewModel;
import com.example.discovery.Models.Visit;
import com.example.discovery.R;
import com.example.discovery.Util.Session;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class DetailsFragment extends Fragment {
    private ParkViewModel parkViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.detail_viewPager);
        parkViewModel = ParkViewModel.getInstance(requireActivity());
        
        TextView parkName = view.findViewById(R.id.detail_parkName_textView);
        TextView parkDesignation = view.findViewById(R.id.detail_park_designation_textView);
        TextView description = view.findViewById(R.id.details_description);
        TextView activities = view.findViewById(R.id.details_activities);
        TextView entranceFees = view.findViewById(R.id.details_entrancefees);
        TextView opHours = view.findViewById(R.id.details_operatinghours);
        TextView detailsTopics = view.findViewById(R.id.details_topics);
        TextView directions = view.findViewById(R.id.details_directions);
        ToggleButton fav = view.findViewById(R.id.fav_btn);
        ToggleButton add = view.findViewById(R.id.detail_scheduleVisit_btn);

        parkViewModel.getSelectedPark().observe(getViewLifecycleOwner(), new Observer<Park>() {
            @Override
            public void onChanged(Park park) {

                Log.d("detailFragment", "onclick:" + park.getFullName());
                parkName.setText(park.getName());
                parkDesignation.setText(park.getDesignation());

                viewPagerAdapter = new ViewPagerAdapter(park.getImages());
                viewPager.setAdapter(viewPagerAdapter);

                description.setText(park.getDescription());

                //Activities
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < park.getActivities().size(); i++) {
                    stringBuilder.append(park.getActivities().get(i).getName())
                            .append(" | ");
                }
                activities.setText(stringBuilder);

                // EntranceFree
                if (park.getEntranceFees().size() > 0) {
                    entranceFees.setText(String.format("Cost: $%s", park.getEntranceFees().get(0).getCost()));
                } else {
                    entranceFees.setText("Info not available");
                }

                // Operation Hours
                StringBuilder opsString = new StringBuilder();
                opsString.append("Wednesday: ").append(park.getOperatingHours().get(0).getStandardHours().getWednesday()).append("\n")
                        .append("Monday: ").append(park.getOperatingHours().get(0).getStandardHours().getMonday()).append("\n")
                        .append("Thursday: ").append(park.getOperatingHours().get(0).getStandardHours().getThursday()).append("\n")
                        .append("Sunday: ").append(park.getOperatingHours().get(0).getStandardHours().getSunday()).append("\n")
                        .append("Tuesday: ").append(park.getOperatingHours().get(0).getStandardHours().getTuesday()).append("\n")
                        .append("Friday: ").append(park.getOperatingHours().get(0).getStandardHours().getFriday()).append("\n")
                        .append("Saturday: ").append(park.getOperatingHours().get(0).getStandardHours().getSaturday());

                opHours.setText(opsString);

                //Topic
                StringBuilder topicBuilder = new StringBuilder();
                for (int i = 0; i < park.getTopics().size(); i++) {
                    topicBuilder.append(park.getTopics().get(i).getName()).append(" | ");
                }
                detailsTopics.setText(topicBuilder);

                //Direction
                if (!TextUtils.isEmpty(park.getDirectionsInfo())) {
                    directions.setText(park.getDirectionsInfo());
                } else {
                    directions.setText("Directions not available");
                }

                FavoriteDao.readAllFav(allFav -> {
                    for(Park favPark : allFav){
                        if(favPark.getId().equals(park.getId())){
                            fav.setChecked(true);
                        }
                    }
                });

                fav.setOnClickListener(view1 -> {
                    if(fav.isChecked()){
                        FavoriteDao.addToFavorite(park);
                    } else {
                        FavoriteDao.removeFromFavorite(park);
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAddedScheduleClick(view, getContext(), park);
                    }
                });


            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    public static void onAddedScheduleClick(View view, Context context, Park park) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View bottonSheetView = LayoutInflater.from(context)
                .inflate(R.layout.schedule_layout, view.findViewById(R.id.scheduleContainer));

        TextView park_name =  bottonSheetView.findViewById(R.id.popUp_row_parkName_textView);
        TextView  state =  bottonSheetView.findViewById(R.id.popUp_row_park_state_textView);
        TextView type =  bottonSheetView.findViewById(R.id.popUp_row_park_type_textView);
        ImageView imageView = bottonSheetView.findViewById(R.id.popUp_row_park_imageView);
        EditText notes = bottonSheetView.findViewById(R.id.notes_editText);
        CalendarView calendarView = bottonSheetView.findViewById(R.id.calen_view);

       park_name.setText(park.getFullName());
       state.setText(park.getStates());
       type.setText(park.getDesignation());
        Picasso.get()
                .load(park.getImages().get(0).getUrl())
                .placeholder(android.R.drawable.stat_sys_download)
                .error(android.R.drawable.stat_notify_error)
                .resize(100, 100)
                .centerCrop()
                .into(imageView);


        bottonSheetView.findViewById(R.id.scheduleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Visit visit = new Visit();
                visit.setPark(park);
                visit.setUserId(Session.getInstance().getUserId());
                visit.setNote(notes.getText().toString().trim());
                SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd yyyy");
                visit.setDate(formatter.format(calendarView.getDate()));

                VisiteDao.addToViste(visit);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();
    }
}