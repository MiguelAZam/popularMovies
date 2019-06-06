package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.popularmovies.api.Caller;
import com.example.android.popularmovies.database.MovieViewModel;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.ReviewResults;
import com.example.android.popularmovies.models.Video;
import com.example.android.popularmovies.models.VideoResults;
import com.example.android.popularmovies.utils.ReviewsAdapter;
import com.example.android.popularmovies.utils.VideosAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetails extends AppCompatActivity {

    TextView tv_movie_title;
    ImageView iv_movie_poster;
    RatingBar rb_movie_rating;
    CheckBox cb_favorite;
    TextView tv_movie_release_date;
    TextView tv_movie_overview;

    RecyclerView rv_videos;
    RecyclerView rv_reviews;

    Movie selectedMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //UI to populate with movie details
        tv_movie_title = findViewById(R.id.tv_movieTitle);
        iv_movie_poster = findViewById(R.id.iv_poster);
        rb_movie_rating = findViewById(R.id.rb_rating);
        cb_favorite = findViewById(R.id.checkBox_favorite);
        tv_movie_release_date = findViewById(R.id.tv_release_date);
        tv_movie_overview = findViewById(R.id.tv_overview);

        //UI to populate with reviews and videos endpoints
        rv_videos = findViewById(R.id.recyclerview_videos);
        rv_reviews = findViewById(R.id.recyclerview_reviews);

        //Get Movie Object
        Intent intent = getIntent();
        selectedMovie = (Movie) intent.getSerializableExtra("Movie");
        boolean isFavorite = intent.getBooleanExtra("Favorite", false);
        populateMovieDetails(selectedMovie, isFavorite);
        String id = Integer.toString(selectedMovie.getId());

        //If internet, get reviews and videos, otherwise notify user that there is
        //not internet connection
        if(hasInternet()){
            enqueueVideosCall(Caller.getVideos(id));
            enqueueReviewsCall(Caller.getReviews(id));
        } else {
            showToast(R.string.no_internet_error);
        }

    }

    //Method to check if device has internet
    public boolean hasInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    //Method to show toast if there is a problem in the app
    public void showToast(int message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
    }

    //Method to populate the movie details, check if movie
    //is in the favorite database and add onclick listener
    //to the checkbox
    public void populateMovieDetails(Movie movie, boolean isFavorite){
        //Populate UI with movie details passed through intent
        tv_movie_title.setText(movie.getOriginal_title());

        RequestOptions options = new RequestOptions().placeholder(R.drawable.notfound).fitCenter();
        Glide.with(this)
                .load(movie.completePosterPath())
                .apply(options)
                .into(iv_movie_poster);

        rb_movie_rating.setRating(Float.valueOf(movie.getVote_average())/2);
        tv_movie_release_date.setText(movie.getRelease_date());
        tv_movie_overview.setText(movie.getOverview());

        //Check if movie is in favorites
        cb_favorite.setChecked(isFavorite);

        //Add check listener to insert / delete movie from database
        addListenerOnCheckFavorites();
    }

    //Method to populate videos recycler view
    public void setVideos(List<Video> videos){
        if(videos != null){
            VideosAdapter adapter = new VideosAdapter(this, videos);
            RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);

            rv_videos.setLayoutManager(mLayout);
            rv_videos.setItemAnimator(new DefaultItemAnimator());
            rv_videos.setAdapter(adapter);
        }
    }

    //Method to populate reviews recycler view
    public void setReviews(List<Review> reviews){
        if(reviews != null){
            ReviewsAdapter adapter = new ReviewsAdapter(this, reviews);
            RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);

            rv_reviews.setLayoutManager(mLayout);
            rv_reviews.setItemAnimator(new DefaultItemAnimator());
            rv_reviews.setAdapter(adapter);
        }
    }

    //Method to call /{id}/videos endpoint asynchronously
    public void enqueueVideosCall(Call<VideoResults> call){
        call.enqueue(new Callback<VideoResults>() {
            @Override
            public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {
                if(response.isSuccessful()){
                    setVideos(response.body().getResults());
                } else{
                    showToast(R.string.server_error);
                }
            }

            @Override
            public void onFailure(Call<VideoResults> call, Throwable t) {
                showToast(R.string.server_error);
            }
        });
    }

    //Method to call /{id}/reviews endpoint asynchronously
    public void enqueueReviewsCall(Call<ReviewResults> call){
        call.enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                if(response.isSuccessful()){
                    setReviews(response.body().getResults());
                } else{
                    showToast(R.string.server_error);
                }
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {
                showToast(R.string.server_error);
            }
        });
    }

    //Method to create an on click listener in checkbox
    public void addListenerOnCheckFavorites(){

        cb_favorite.setOnClickListener(view -> {
            MovieViewModel viewModel = new MovieViewModel(getApplication());
            if(((CheckBox) view).isChecked()){
                //if checkbox is checked, insert movie in the favorite table and notify user
                viewModel.insertMovie(selectedMovie);
                Toast.makeText(getApplicationContext(), R.string.added_to_database, Toast.LENGTH_SHORT).show();
            } else{
                //otherwise, remove movie in the favorite table and notify user
                viewModel.deleteMovie(selectedMovie);
                Toast.makeText(getApplicationContext(), R.string.deleted_from_database, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
