package com.example.filmdizi.models;

import com.google.gson.annotations.SerializedName;

public class TvShowDetailResponse {
    @SerializedName("number_of_seasons")
    private int numberOfSeasons;

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }
}