package com.example.filmdizi.network;

import com.example.filmdizi.models.Movie;
import com.example.filmdizi.models.MovieResponse;
import com.example.filmdizi.models.SeasonResponse;
import com.example.filmdizi.models.TvShowDetailResponse;
import com.example.filmdizi.models.TvExternalIdsResponse; // EKLENDİ

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApiService {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("tv/popular")
    Call<MovieResponse> getPopularSeries(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("tv/{tv_id}/season/{season_number}")
    Call<SeasonResponse> getSeasonDetails(@Path("tv_id") int tvId, @Path("season_number") int seasonNumber, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("search/multi")
    Call<MovieResponse> searchMulti(@Query("api_key") String apiKey, @Query("query") String query, @Query("language") String language);

    @GET("tv/{tv_id}")
    Call<TvShowDetailResponse> getTvShowDetails(@Path("tv_id") int tvId, @Query("api_key") String apiKey, @Query("language") String language);

    // YENİ: Filmin Detaylarını Çek (IMDb ID için)
    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("language") String language);

    // YENİ: Dizinin Dış Kimliklerini Çek (IMDb ID için)
    @GET("tv/{tv_id}/external_ids")
    Call<TvExternalIdsResponse> getTvExternalIds(@Path("tv_id") int tvId, @Query("api_key") String apiKey);
}