package com.example.filmdizi.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filmdizi.R;
import com.example.filmdizi.adapters.MovieAdapter;
import com.example.filmdizi.models.Movie;
import com.example.filmdizi.models.MovieResponse;
import com.example.filmdizi.network.ApiClient;
import com.example.filmdizi.network.TmdbApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerMovies, recyclerSeries, recyclerGames;
    private MovieAdapter movieAdapter, seriesAdapter;
    private List<Movie> movieList = new ArrayList<>(), seriesList = new ArrayList<>();
    private EditText editSearch;
    private final String API_KEY = "9a8b276e5a73bf376fdfba9de1ba4b60";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Bağlantılar
        editSearch = view.findViewById(R.id.edit_search);
        recyclerMovies = view.findViewById(R.id.recycler_movies);
        recyclerSeries = view.findViewById(R.id.recycler_series);
        recyclerGames = view.findViewById(R.id.recycler_games);

        // Layout Ayarları
        recyclerMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerSeries.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerGames.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Adaptörleri Bağla
        movieAdapter = new MovieAdapter(movieList);
        seriesAdapter = new MovieAdapter(seriesList);
        recyclerMovies.setAdapter(movieAdapter);
        recyclerSeries.setAdapter(seriesAdapter);

        // Arama Dinleyicisi (Klavyedeki Büyüteç/Tamam ikonuna basınca çalışır)
        editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String query = editSearch.getText().toString();
                if (!query.isEmpty()) {
                    performSearch(query);
                } else {
                    fetchInitialData();
                }
                return true;
            }
            return false;
        });

        fetchInitialData();
        setupLocalGames();

        return view;
    }

    private void fetchInitialData() {
        movieList.clear();
        seriesList.clear();
        fetchMovies();
        fetchSeries();
    }

    private void performSearch(String query) {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);
        apiService.searchMulti(API_KEY, query, "tr-TR").enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList.clear();
                    movieList.addAll(response.body().getResults());
                    movieAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) {}
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

    private void setupLocalGames() {
        List<Movie> gamesList = new ArrayList<>();
        gamesList.add(new Movie("Resident Evil 4", "https://image.tmdb.org/t/p/w500/1p5BWeE5o1dE53rG50o1z9c2m1B.jpg", 9.5));
        gamesList.add(new Movie("BeamNG.drive", "https://image.tmdb.org/t/p/w500/6vA1xK1Jz2Q9d8m7H4aW5X3kZ1v.jpg", 9.0));
        recyclerGames.setAdapter(new MovieAdapter(gamesList));
    }
}