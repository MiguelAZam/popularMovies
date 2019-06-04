package com.example.android.popularmovies.models;

import java.io.Serializable;
import java.util.List;

//POJO to model the response of /favorite and /toprated endpoints
public class MovieResults implements Serializable {

    private int page;
    private List<Movie> results;

    public MovieResults(List<Movie> movies){
        page = -1;
        results = movies;
    }

    public List<Movie> getResults() {
        return results;
    }
}
