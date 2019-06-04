package com.example.android.popularmovies.api;

import com.example.android.popularmovies.models.MovieResults;
import com.example.android.popularmovies.models.ReviewResults;
import com.example.android.popularmovies.models.VideoResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//Interface required for retrofit library
//https://square.github.io/retrofit/
public interface MovieAPI {

    //GET popular movies
    @GET("popular")
    Call<MovieResults> getPopularMovies(@Query("api_key") String key);

    //GET top rated movies
    @GET("top_rated")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String key);

    //GET videos of selected movie identified by ID
    @GET("{id}/videos")
    Call<VideoResults> getMovieVideos(@Path("id") String id, @Query("api_key") String key);

    //GET reviews of selected movie identified by ID
    @GET("{id}/reviews")
    Call<ReviewResults> getMovieReviews(@Path("id") String id, @Query("api_key") String key);
}
