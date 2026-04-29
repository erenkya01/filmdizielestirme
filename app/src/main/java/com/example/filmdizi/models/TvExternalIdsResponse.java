package com.example.filmdizi.models;

import com.google.gson.annotations.SerializedName;

public class TvExternalIdsResponse {
    @SerializedName("imdb_id")
    private String imdbId;

    public String getImdbId() {
        return imdbId;
    }
}