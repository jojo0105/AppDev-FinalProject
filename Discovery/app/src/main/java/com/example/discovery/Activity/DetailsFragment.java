package com.example.discovery.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.discovery.Adapter.ReviewRecycleerViewAdapter;
import com.example.discovery.Adapter.ViewPagerAdapter;
import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Park;
import com.example.discovery.Models.Review;
import com.example.discovery.Models.User;
import com.example.discovery.Models.Visit;
import com.example.discovery.Notification;
import com.example.discovery.R;
import com.example.discovery.Util.Session;
import com.example.discovery.Util.Util;
import com.example.discovery.ViewModels.FavoriteViewModel;
import com.example.discovery.ViewModels.ParkViewModel;
import com.example.discovery.ViewModels.ReviewViewModel;
import com.example.discovery.ViewModels.UserViewModel;
import com.example.discovery.ViewModels.VisiteViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


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
        ToggleButton review = view.findViewById(R.id.add_review_btn);
        Log.d("Session_detailFragment", "User_id: " + Session.getInstance().getUserId());

        parkViewModel.getSelectedPark().observe(getViewLifecycleOwner(), new Observer<Park>() {
            @Override
            public void onChanged(Park park) {

                Log.d("detailFragment", "onclick:" + park.getFullName());
                parkName.setText(park.getName());
                parkDesignation.setText(park.getDesignation());

                viewPagerAdapter = new ViewPagerAdapter(park);
                viewPager.setAdapter(viewPagerAdapter);

                description.setText ("\t\t\t" + park.getDescription());

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




                FavoriteViewModel.readAllFav(allFav -> {
                    for(Park favPark : allFav){
                        if(favPark.getId().equals(park.getId())){
                            fav.setChecked(true);
                        }
                    }
                });

                fav.setOnClickListener(view1 -> {
                    if(fav.isChecked()){
                        FavoriteViewModel.addToFavorite(park);
                    } else {
                        FavoriteViewModel.removeFromFavorite(park).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot value : snapshot.getChildren()){
                                    Favorites favorite = value.getValue(Favorites.class);
                                    if(park.getId().equalsIgnoreCase(favorite.getPark().getId())){
                                        value.getRef().removeValue();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("DetailsFragments", "onCancelled", error.toException());
                            }
                        });
                    }
                });

                VisiteViewModel.getAllVisit(visits -> {
                    for(Visit visPark : visits){
                        if(visPark.getPark().getId().equals(park.getId())){
                        add.setChecked(true);
                    }
                }
            });


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAddedScheduleClick(view, getContext(), park);
                    }
                });

                review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reviewClick(view, getContext(), park);
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
        Util.hideSoftKeyboard(view);
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



        Visit visit = new Visit();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView1, int i, int i1, int i2) {
                String date =  i2 + "/" + (i1 + 1) + "/" + i;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    visit.setDate(formatter.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        UserViewModel.getUser(user -> visit.setUserId(user.getUserId()));

        bottonSheetView.findViewById(R.id.scheduleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visit.setPark(park);
                visit.setNote(notes.getText().toString().trim());
                visit.setStatus(false);
                VisiteViewModel.addToViste(visit);
                UserViewModel.getUser(user -> sendEmail(visit, user));
                Notification notification = new Notification();
                notification.getScheculeConfimaionNotif(context, visit);
                Toast.makeText(context, "Add to Visit!", Toast.LENGTH_SHORT);
                bottomSheetDialog.dismiss();
            }
        });
        Log.d("user_id", "user" + visit.getUserId());

        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();
    }

    public static void reviewClick(View view, Context context, Park park) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View bottonSheetView = LayoutInflater.from(context)
                .inflate(R.layout.review_layout, view.findViewById(R.id.commentContainer));


        Review review = new Review();


        EditText comment = bottonSheetView.findViewById(R.id.review_comment_editText);
        Button btn = bottonSheetView.findViewById(R.id.addComment_btn);
        RecyclerView recyclerView = bottonSheetView.findViewById(R.id.review_recyclerView);



        btn.setOnClickListener(view1 -> {
            review.setUserName(Session.getInstance().getUserName());
            review.setComment(comment.getText().toString().trim());
            review.setParkID(park.getId());
            ReviewViewModel.addReview(review).addOnSuccessListener(unused -> Log.d("review", "added"));
            comment.setText("");
        });

        ReviewViewModel.retrieveReview(park.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Review> reviews = new ArrayList<>();
                reviews.clear();


                for (DataSnapshot value : snapshot.getChildren()){
                    Review review = value.getValue(Review.class);
                    reviews.add(review);
                }
                ReviewRecycleerViewAdapter reviewRecycleerViewAdapter = new ReviewRecycleerViewAdapter(reviews);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(reviewRecycleerViewAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Review", "cancelled");
            }
        });

        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();

    }

    public static void sendEmail(Visit visit, User user){
        Log.d("SendEmail", user.getEmail());
        String message = "Hey " +user.getFirstName() + " "+ user.getLastname() + ",\n\nYour visit has been arranged on the " + visit.getDateString()
                + " at " + visit.getPark().getFullName() + ".\n\n Enjoy\n Discovery Team";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");

        javax.mail.Session session = javax.mail.Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Util.email, Util.email_pwd);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            mimeMessage.setSubject("Schedule Confirmation");
            mimeMessage.setText(message);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}