package com.example.android.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.popularmovies.models.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private FavoriteRepository movieFav;
    private LiveData<List<Movie>> favoriteMovies;

    public MovieViewModel(Application app){
        super(app);
        movieFav = new FavoriteRepository(app);
        favoriteMovies = movieFav.getFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies(){
        return favoriteMovies;
    }

    public void insertMovie(Movie movie){
        movieFav.insert(movie);
    }

    public void deleteMovie(Movie movie){
        movieFav.delete(movie);
    }

}
