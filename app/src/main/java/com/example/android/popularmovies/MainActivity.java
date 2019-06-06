package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.api.Caller;
import com.example.android.popularmovies.database.MovieViewModel;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.MovieResults;
import com.example.android.popularmovies.utils.MoviesAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;

    //Variable to save the actual movies shown in the app
    private static List<Movie> moviesState;

    //Constants to access information from the instance state object
    private static String TITLE_KEY = "TITLE";
    private static String LIST_KEY = "MOVIES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview);
        mProgressBar = findViewById(R.id.pb_fetching);

        //Check if instance state was created and restore it -
        if(savedInstanceState != null){
            //Restore title
            CharSequence title = savedInstanceState.getCharSequence(TITLE_KEY);
            setTitle(title);

            //Restore Movies
            MovieResults movies = (MovieResults) savedInstanceState.getSerializable(LIST_KEY);
            setRecyclerView(movies.getResults());
            return;
        }

        //if(device has internet) -> Populate UI with popular movies
        //Otherwise -> Show connection error
        if(hasInternet()){
            enqueueCall(Caller.getPopularMovies());
        } else{
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

    //Method to toggle progress bar if network work is in progress
    public void toggleProgressBar(){
        int state = (mProgressBar.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
        mProgressBar.setVisibility(state);
    }

    //Method to make network work in the background with Retrofit library
    //and populate UI
    public void enqueueCall(Call<MovieResults> call){
        toggleProgressBar();
        call.enqueue(new Callback<MovieResults>() {
            //If call was a success or there was an response
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                toggleProgressBar();
                if(response.isSuccessful()){
                    //Populate recycler view
                    setRecyclerView(response.body().getResults());
                } else{
                    showToast(R.string.server_error);
                }

            }

            //If there was a connection error
            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                toggleProgressBar();
                showToast(R.string.server_error);
            }
        });
    }

    private int calculateBestSpanCount(){
        int cardWidth = (int) getResources().getDimension(R.dimen.card_width);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth/cardWidth);
    }

    //Method to show Recycler View of movies
    public void setRecyclerView(List<Movie> movies){
        if(movies != null){
            //Save movies in global variable
            moviesState = movies;
            //Create adapter and layout manager
            MoviesAdapter adapter = new MoviesAdapter(this, movies);
            RecyclerView.LayoutManager mLayout = new GridLayoutManager(this, calculateBestSpanCount());

            //Populate recycler view
            mRecyclerView.setLayoutManager(mLayout);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mRecyclerView.setVisibility(View.INVISIBLE);

        //Get id of item selected
        int id = item.getItemId();

        //Get actual app title
        String actualTitle = getTitle().toString();
        String newTitle = "";

        //Set app title according to the selected item
        //This change is to give feedback to the user about
        //what item was selected
        if(id == R.id.action_most_popular){
            setTitle(R.string.app_popular_name);
        } else if(id == R.id.action_highest_rated) {
            setTitle(R.string.app_rated_name);
        } else if(id == R.id.action_favorite){
            setTitle(R.string.app_favorite_name);
        }

        //Get new app title
        newTitle = getTitle().toString();

        //Compare new title and actual app
        //If they are the same, it means the user selected the same item and
        //is not required to call the api again
        //Otherwise, the selected item is different and an api call is required
        if(!actualTitle.equals(newTitle)) {
            if(hasInternet() && id == R.id.action_most_popular){
                //Get popular movies
                enqueueCall(Caller.getPopularMovies());
            } else if(hasInternet() && id == R.id.action_highest_rated){
                //Get top rated movies
                enqueueCall(Caller.getTopRatedMovies());
            } else if(id == R.id.action_favorite){
                //Get favorite movies in the database
                MovieViewModel movieViewModel = new MovieViewModel(getApplication());
                movieViewModel.getFavoriteMovies().observe(this, movies -> setRecyclerView(movies));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Save title
        CharSequence title = getTitle();
        outState.putCharSequence(TITLE_KEY, title);

        //Save movies
        MovieResults movies = new MovieResults(moviesState);
        outState.putSerializable(LIST_KEY, movies);
        super.onSaveInstanceState(outState);
    }
}
