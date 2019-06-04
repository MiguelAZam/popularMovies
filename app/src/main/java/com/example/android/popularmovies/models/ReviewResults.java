package com.example.android.popularmovies.models;

import java.util.List;

//POJO to model the response of /{id}/reviews endpoint
public class ReviewResults {

    private int id;
    private int page;
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }
}
