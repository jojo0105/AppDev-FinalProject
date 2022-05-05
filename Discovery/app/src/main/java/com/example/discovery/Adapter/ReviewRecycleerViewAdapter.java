package com.example.discovery.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discovery.Models.Review;
import com.example.discovery.R;

import java.util.List;

public class ReviewRecycleerViewAdapter  extends RecyclerView.Adapter<ReviewRecycleerViewAdapter.ViewHolder>{
    private final List<Review> reviews;

    public ReviewRecycleerViewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewRecycleerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new ReviewRecycleerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecycleerViewAdapter.ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.userName.setText(review.getUserName());
        holder.comment.setText(review.getComment());
        holder.img.setImageAlpha(R.drawable.profile);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView comment;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.review_userName_textView);
            comment = itemView.findViewById(R.id.review_comments_textView);
            img = itemView.findViewById(R.id.review_userPic_textView);
        }
    }
}
