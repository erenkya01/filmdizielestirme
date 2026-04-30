// models/Game.java
package com.example.filmdizi.models;

import com.google.gson.annotations.SerializedName;

public class Game {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("background_image")
    private String backgroundImage;

    @SerializedName("rating")
    private double rating;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBackgroundImage() { return backgroundImage; }
    public double getRating() { return rating; }
}