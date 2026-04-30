// models/GameResponse.java
package com.example.filmdizi.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GameResponse {
    @SerializedName("results")
    private List<Game> results;

    public List<Game> getResults() {
        return results;
    }
}