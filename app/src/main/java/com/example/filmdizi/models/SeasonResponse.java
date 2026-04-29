package com.example.filmdizi.models;

import com.google.gson.annotations.SerializedName;
import java.util.List; // Bu import eksikse hata verir

public class SeasonResponse {

    @SerializedName("episodes")
    private List<Episode> episodes;

    // Boş yapıcı metot (Retrofit/Gson için güvenlik önlemi)
    public SeasonResponse() {
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}