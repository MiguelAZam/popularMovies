package com.example.android.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.popularmovies.models.Movie;

//Singleton class to start connection with database
@Database(entities={Movie.class}, version=1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    public abstract MovieDao MovieDao();
    private static FavoriteDatabase sInstance;
    private static String DATABASE_NAME="Movies";

    public static FavoriteDatabase getInstance(final Context c){
        if(sInstance == null){
            synchronized (FavoriteDatabase.class){
                sInstance = Room.databaseBuilder(c.getApplicationContext(),
                        FavoriteDatabase.class, FavoriteDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

}
