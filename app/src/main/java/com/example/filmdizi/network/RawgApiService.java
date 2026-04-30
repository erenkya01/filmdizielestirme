package com.example.filmdizi.network;

import com.example.filmdizi.models.GameResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RawgApiService {
    @GET("games")
    Call<GameResponse> getPopularGames(
            @Query("key") String apiKey,
            @Query("ordering") String ordering,
            @Query("page_size") int pageSize
    );

    // YENİ EKLENEN: Arama metodu
    @GET("games")
    Call<GameResponse> searchGames(
            @Query("key") String apiKey,
            @Query("search") String searchQuery
    );
}