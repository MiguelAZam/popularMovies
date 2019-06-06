package com.example.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.models.Movie;

import java.util.List;

public class FavoriteRepository {

    private static MovieDao mMovieDao;
    private static LiveData<List<Movie>> favorites;

    //Constants
    private static int INSERT_MOVIE=0;
    private static int DELETE_MOVIE=1;

    public FavoriteRepository(Context c){
        FavoriteDatabase db = FavoriteDatabase.getInstance(c);
        mMovieDao = db.MovieDao();
        favorites = mMovieDao.loadFavoriteMovies();
    }

    //Get all favorite movies
    public LiveData<List<Movie>> getFavoriteMovies(){
        return favorites;
    }

    //Check if movie identified by id is in the table
    public static Boolean isFavorite(Movie movie){
        AsyncTask<Movie, Void, Boolean> result = new AsyncTask<Movie, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Movie... movies) {
                String id = Integer.toString(movie.getId());
                //Look for movie
                return mMovieDao.movieExist(id);
            }
        };

        try{
            return result.execute(movie).get();
        } catch(Exception e){
            return false;
        }
    }

    //Insert movie
    public void insert(Movie movie){
        new dbAsyncTask(mMovieDao, INSERT_MOVIE).execute(movie);
    }

    //Delete movie
    public void delete(Movie movie){
        new dbAsyncTask(mMovieDao, DELETE_MOVIE).execute(movie);
    }

    //Create async task to perform database operations
    private static class dbAsyncTask extends AsyncTask<Movie, Void, Void> {

        //Data access object to perform database operations
        private MovieDao asyncDao;
        //type of operation
        private int operation;

        dbAsyncTask(MovieDao movieDao, int operation){
            asyncDao = movieDao;
            this.operation = operation;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            if(operation == INSERT_MOVIE){
                asyncDao.insertMovie(params[0]);
            } else if(operation == DELETE_MOVIE) {
                asyncDao.deleteMovie(params[0]);
            }
            return null;
        }
    }

}
