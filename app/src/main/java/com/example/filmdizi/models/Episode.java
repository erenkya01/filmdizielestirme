package com.example.filmdizi.models;

import com.google.gson.annotations.SerializedName;

public class Episode {
    @SerializedName("name")
    private String name;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("episode_number")
    private int episodeNumber;

    // Boş Constructor (Hata almamak için şart)
    public Episode() {}

    // Getter metodları
    public String getName() { return name; }
    public double getVoteAverage() { return voteAverage; }
    public int getEpisodeNumber() { return episodeNumber; }
}