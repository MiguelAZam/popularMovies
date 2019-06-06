package com.example.android.popularmovies.api;

import com.example.android.popularmovies.models.MovieResults;
import com.example.android.popularmovies.models.ReviewResults;
import com.example.android.popularmovies.models.VideoResults;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Caller {

    //Base url of Movies API
    private static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    //API key to make calls
    //TODO: Remove API_KEY
    private static String API_KEY = "8fecd5432bfe0a9668cfc80766c9a1e7";

    //Set up retrofit
    private static MovieAPI buildCaller(){
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return rf.create(MovieAPI.class);
    }

    //Create call object to obtain popular movies
    public static Call<MovieResults> getPopularMovies(){
        MovieAPI movieAPI = buildCaller();
        return movieAPI.getPopularMovies(API_KEY);
    }

    //Create call object to obtain top rated movies
    public static Call<MovieResults> getTopRatedMovies(){
        MovieAPI movieAPI = buildCaller();
        return movieAPI.getTopRatedMovies(API_KEY);
    }

    //Create call object to obtain videos of movie selected
    public static Call<VideoResults> getVideos(String id){
        MovieAPI movieAPI = buildCaller();
        return movieAPI.getMovieVideos(id, API_KEY);
    }

    //Create call object to obtain review of movie selected
    public static Call<ReviewResults> getReviews(String id){
        MovieAPI movieAPI = buildCaller();
        return movieAPI.getMovieReviews(id, API_KEY);
    }

}
