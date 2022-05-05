package com.example.discovery.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discovery.Models.Visit;
import com.example.discovery.R;
import com.example.discovery.ViewModels.VisiteViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    private VisitRecyclerViewAdapter adapter;
    private List<Visit> visits;

    public TouchHelper(VisitRecyclerViewAdapter adapter, List<Visit> visit) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.visits = visit;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.RIGHT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setMessage("Are You Sure?")
                    .setTitle("Delete Visit")
                    .setPositiveButton("Yes", (dialogInterface, i) -> adapter.deleteVisit(position))
                    .setNegativeButton("No", (dialogInterface, i) -> adapter.notifyItemChanged(position));
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Visit oldvisit = visits.get(position);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(adapter.getContext(), R.style.BottomSheetDialogTheme);
            View bottonSheetView = LayoutInflater.from(adapter.getContext())
                    .inflate(R.layout.schedule_layout, adapter.getView().findViewById(R.id.scheduleContainer));

            TextView park_name =  bottonSheetView.findViewById(R.id.popUp_row_parkName_textView);
            TextView  state =  bottonSheetView.findViewById(R.id.popUp_row_park_state_textView);
            TextView type =  bottonSheetView.findViewById(R.id.popUp_row_park_type_textView);
            ImageView imageView = bottonSheetView.findViewById(R.id.popUp_row_park_imageView);
            EditText notes = bottonSheetView.findViewById(R.id.notes_editText);

            CalendarView calendarView = bottonSheetView.findViewById(R.id.calen_view);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            park_name.setText(oldvisit.getPark().getFullName());
            state.setText(oldvisit.getPark().getStates());
            type.setText(oldvisit.getPark().getDesignation());
            Picasso.get()
                    .load(oldvisit.getPark().getImages().get(0).getUrl())
                    .placeholder(android.R.drawable.stat_sys_download)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(100, 100)
                    .centerCrop()
                    .into(imageView);



            calendarView.setDate(oldvisit.getDate().getTime(), true, true);
            notes.setText(oldvisit.getNote());


            Visit visit = new Visit();
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView1, int i, int i1, int i2) {
                    String date =  i2 + "/" + (i1 + 1) + "/" + i;
                    try {
                        visit.setDate(formatter.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            bottonSheetView.findViewById(R.id.scheduleBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    visit.setPark(oldvisit.getPark());
                    visit.setUserId(oldvisit.getUserId());
                    visit.setNote(notes.getText().toString().trim());
                    visit.setStatus(false);
                    VisiteViewModel.updateVisit(visit, oldvisit.getId());
                    Toast.makeText(adapter.getContext(), "Update", Toast.LENGTH_SHORT);
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(bottonSheetView);
            bottomSheetDialog.show();
            adapter.notifyItemChanged(position);
        }
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_24)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit_24)
                .addSwipeLeftBackgroundColor(Color.GREEN)
                //.addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext() , Color.GREEN))
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}

interface GetDate{
   void date(String date);
}
