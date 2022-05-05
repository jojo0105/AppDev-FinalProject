package com.example.discovery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discovery.Models.Visit;
import com.example.discovery.R;
import com.example.discovery.Util.DB;
import com.example.discovery.ViewModels.VisiteViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VisitRecyclerViewAdapter extends RecyclerView.Adapter<VisitRecyclerViewAdapter.ViewHolder> {
    private final List<Visit> visits;
    private Context context;
    private View view;

    public VisitRecyclerViewAdapter(List<Visit> visits, Context context) {
        this.visits = visits;
        this.context = context;
    }

    @NonNull
    @Override
    public VisitRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_row, parent, false);
        return new VisitRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitRecyclerViewAdapter.ViewHolder holder, int position) {
        Visit visit = visits.get(position);
        view = holder.view;
        holder.parkName.setText(visit.getPark().getName());
        holder.notes.setText(visit.getNote());
        holder.date.setText(visit.getDateString());
        Picasso.get()
                .load(visit.getPark().getImages().get(0).getUrl())
                .fit()
                .centerCrop()
                .into(holder.parkImage);

        holder.checkBox.setChecked(visit.getStatus());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    DB.selectCollection("visit").child(visit.getId()).child("status").setValue(true);
                }else {
                    DB.selectCollection("visit").child(visit.getId()).child("status").setValue(false);
                }
            }
        });

    }

    public void deleteVisit(int position){
        Visit visit = visits.get(position);
        VisiteViewModel.removeViste(visit).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot value : snapshot.getChildren()){
                    Visit visit1 = value.getValue(Visit.class);
                    if(visit1.getId().equals(visit.getId())){
                        value.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        notifyItemRemoved(position);

    }

    public Context getContext(){
       return context;
    }

    public View getView(){
        return view;
    }

    @Override
    public int getItemCount() {
        return visits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView parkImage;
        public TextView parkName;
        public TextView date;
        public TextView notes;
        public CheckBox checkBox;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            parkImage = itemView.findViewById(R.id.row_park_imageView);
            parkName = itemView.findViewById(R.id.visite_parkName_textView);
            date = itemView.findViewById(R.id.schedule_date_textView);
            notes = itemView.findViewById(R.id.notes_textView);
            checkBox = itemView.findViewById(R.id.checkbox_visit);
        }
    }
}
