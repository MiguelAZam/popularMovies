package com.example.android.popularmovies.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private Context mContext;
    private List<Review> reviewList;

    //Constructor which takes a Context (c) and a list of reviews (reviewList)
    public ReviewsAdapter(Context c, List<Review> reviewList){
        this.mContext = c;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ReviewViewHolder(itemView);
    }

    //Method to populate each view holder
    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {
        //Get review of the corresponding position
        final Review review = reviewList.get(position);

        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    //View holder class
    class ReviewViewHolder extends RecyclerView.ViewHolder{
        ImageView reviewer_placeholder;
        TextView author;
        TextView content;

        ReviewViewHolder(View view){
            super(view);
            reviewer_placeholder = view.findViewById(R.id.iv_reviewer);
            author = view.findViewById(R.id.tv_author);
            content = view.findViewById(R.id.tv_content);
        }
    }

}
