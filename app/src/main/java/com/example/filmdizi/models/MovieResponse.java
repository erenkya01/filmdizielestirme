package com.example.filmdizi.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieResponse {
    // TMDB API'si film listesini "results" adında bir dizi (array) içinde gönderir
    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }
}