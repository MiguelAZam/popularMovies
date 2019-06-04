package com.example.android.popularmovies.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Favorite")
public class Movie implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String poster_path;
    private String original_title;
    private String overview;
    private String vote_average;
    private String release_date;

    private String COMPLETE_PATH="http://image.tmdb.org/t/p/w185";

    public Movie(int id, String poster_path, String original_title, String overview, String vote_average, String release_date){
        this.id = id;
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public int getId(){
        return this.id;
    }

    public String completePosterPath() { return COMPLETE_PATH + poster_path; }

    public String getPoster_path(){
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }
}
