package com.example.android.popularmovies.models;

import java.util.List;

//POJO to model the response of /{id}/videos endpoint
public class VideoResults {

    private int page;
    private List<Video> results;

    public List<Video> getResults() {
        return results;
    }
}
