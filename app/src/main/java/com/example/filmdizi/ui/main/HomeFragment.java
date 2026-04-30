package com.example.filmdizi.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// TMDB ve Ortak Sınıflar
import com.example.filmdizi.R;
import com.example.filmdizi.adapters.MovieAdapter;
import com.example.filmdizi.models.Movie;
import com.example.filmdizi.models.MovieResponse;
import com.example.filmdizi.network.ApiClient;
import com.example.filmdizi.network.TmdbApiService;

// RAWG Sınıfları (Eksik Olan Importlar Bunlardı)
import com.example.filmdizi.adapters.GameAdapter;
import com.example.filmdizi.models.Game;
import com.example.filmdizi.models.GameResponse;
import com.example.filmdizi.network.RawgApiClient;
import com.example.filmdizi.network.RawgApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerMovies, recyclerSeries, recyclerGames;
    private MovieAdapter movieAdapter, seriesAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private List<Movie> seriesList = new ArrayList<>();
    private final String API_KEY = "9a8b276e5a73bf376fdfba9de1ba4b60";

    // Oyunlar için değişkenler
    private GameAdapter gameAdapter;
    private List<Game> gameList = new ArrayList<>();
    private final String RAWG_API_KEY = "50c78ab41bee4e6d97b69aaa58455538";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerMovies = view.findViewById(R.id.recycler_movies);
        recyclerSeries = view.findViewById(R.id.recycler_series);
        recyclerGames = view.findViewById(R.id.recycler_games);

        recyclerMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerSeries.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerGames.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        movieAdapter = new MovieAdapter(movieList);
        seriesAdapter = new MovieAdapter(seriesList);
        recyclerMovies.setAdapter(movieAdapter);
        recyclerSeries.setAdapter(seriesAdapter);

        // Oyun Adaptörünü Bağlıyoruz
        gameAdapter = new GameAdapter(gameList);
        recyclerGames.setAdapter(gameAdapter);

        // API Çağrıları
        fetchMovies();
        fetchSeries();
        fetchGames();

        return view;
    }

    private void fetchGames() {
        RawgApiService apiService = RawgApiClient.getClient().create(RawgApiService.class);
        // Puanı en yüksek olan 10 oyunu çekiyoruz
        apiService.getPopularGames(RAWG_API_KEY, "-rating", 10).enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gameList.clear();
                    gameList.addAll(response.body().getResults());
                    gameAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<GameResponse> call, Throwable t) {}
        });
    }

    private void fetchMovies() {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);
        apiService.getPopularMovies(API_KEY, "tr-TR").enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList.addAll(response.body().getResults());
                    movieAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) {}
        });
    }

    private void fetchSeries() {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);
        apiService.getPopularSeries(API_KEY, "tr-TR").enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    seriesList.addAll(response.body().getResults());
                    seriesAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) {}
        });
    }
}