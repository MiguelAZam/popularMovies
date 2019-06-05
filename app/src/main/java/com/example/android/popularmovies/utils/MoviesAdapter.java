package com.example.android.popularmovies.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.popularmovies.MovieDetails;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    private Context mContext;
    private List<Movie> movieList;

    //Constructor which takes a Context (c) and a list of movies (movieList)
    public MoviesAdapter(Context c, List<Movie> movieList){
        this.mContext = c;
        this.movieList = movieList;
    }

    //View holder class
    class MovieViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        TextView title;
        ImageView iv_poster;

        MovieViewHolder(View view){
            super(view);
            card = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.title);
            iv_poster = view.findViewById(R.id.iv_main_poster);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(itemView);
    }

    //Method to populate each view holder
    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {

        //Get movie of the corresponding position
        final Movie movie = movieList.get(position);

        RequestOptions options = new RequestOptions().placeholder(R.drawable.notfound).fitCenter();

        Glide.with(mContext)
                .load(movie.completePosterPath())
                .apply(options)
                .into(holder.iv_poster);

        //Creation of a click/tap listener to redirect to the second activity (Details screen)
        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, MovieDetails.class);
            //Send the movie id to the second activity
            intent.putExtra("Movie", movie);
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

}
