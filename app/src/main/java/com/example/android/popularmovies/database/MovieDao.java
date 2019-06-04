package com.example.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies.models.Movie;

import java.util.List;


@Dao
public interface MovieDao {

    //Obtain all favorite movies
    @Query("SELECT * FROM Favorite")
    LiveData<List<Movie>> loadFavoriteMovies();

    //Obtain favorite movie identified by id
    @Query("SELECT * FROM Favorite WHERE id LIKE :id")
    List<Movie> getMovie(String id);

    //Insert movie in the table
    @Insert
    void insertMovie(Movie movie);

    //Delete movie in the table
    @Delete
    void deleteMovie(Movie movie);

}
