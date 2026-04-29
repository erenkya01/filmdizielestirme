package com.example.filmdizi.network;

import com.example.filmdizi.models.MovieResponse;
import com.example.filmdizi.models.SeasonResponse;
import com.example.filmdizi.models.TvShowDetailResponse; // BU SATIR EKSİKTİ, KIRMIZILIĞI BU ÇÖZER

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApiService {

    // Popüler Filmler
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    // Popüler Diziler
    @GET("tv/popular")
    Call<MovieResponse> getPopularSeries(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    // Dizi Bölüm Detayları
    @GET("tv/{tv_id}/season/{season_number}")
    Call<SeasonResponse> getSeasonDetails(
            @Path("tv_id") int tvId,
            @Path("season_number") int seasonNumber,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    // Arama (Film ve Dizi Karışık)
    @GET("search/multi")
    Call<MovieResponse> searchMulti(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("language") String language
    );

    // Dizinin Genel Detayları (Sezon sayısını öğrenmek için)
    @GET("tv/{tv_id}")
    Call<TvShowDetailResponse> getTvShowDetails(
            @Path("tv_id") int tvId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}