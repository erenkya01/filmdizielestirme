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

    // 1. BOŞ CONSTRUCTOR (Retrofit/API için şart)
    public Movie() {
    }

    // 2. ÜÇ PARAMETRELİ CONSTRUCTOR (Senin elinle eklediğin oyunlar için şart!)
    // Hatanın çözümü tam olarak bu metottur:
    public Movie(String title, String posterPath, double voteAverage) {
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

    // GETTER METOTLARI
    public int getId() {
        return id;
    }

    public String getTitle() {
        // Eğer title doluysa (film), değilse name (dizi) döndür
        return title != null ? title : name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    // Film mi dizi mi kontrolü (DetailActivity için)
    public boolean isMovie() {
        return title != null;
    }
}