package com.example.filmdizi.models;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("name")
    private String name;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("imdb_id") // YENİ EKLENDİ
    private String imdbId;

    public Movie() {}

    public Movie(String title, String posterPath, double voteAverage) {
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

    public int getId() { return id; }
    public String getTitle() { return title != null ? title : name; }
    public String getPosterPath() { return posterPath; }
    public double getVoteAverage() { return voteAverage; }
    public String getImdbId() { return imdbId; } // YENİ EKLENDİ
    public boolean isMovie() { return title != null; }
}