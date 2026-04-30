package com.example.filmdizi.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmdizi.R;
import com.example.filmdizi.adapters.MovieAdapter;
import com.example.filmdizi.models.Game;
import com.example.filmdizi.models.GameResponse;
import com.example.filmdizi.models.Movie;
import com.example.filmdizi.models.MovieResponse;
import com.example.filmdizi.network.ApiClient;
import com.example.filmdizi.network.RawgApiClient;
import com.example.filmdizi.network.RawgApiService;
import com.example.filmdizi.network.TmdbApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private EditText editLiveSearch;
    private RecyclerView recyclerSearchResults;
    private Button btnSearchMovie, btnSearchGame;

    private MovieAdapter searchAdapter;
    private List<Movie> searchResults = new ArrayList<>();

    private boolean isSearchingGame = false; // Hangi sekmedeyiz?

    private final String API_KEY = "9a8b276e5a73bf376fdfba9de1ba4b60";
    private final String RAWG_API_KEY = "50c78ab41bee4e6d97b69aaa58455538";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editLiveSearch = view.findViewById(R.id.edit_live_search);
        recyclerSearchResults = view.findViewById(R.id.recycler_search_results);
        btnSearchMovie = view.findViewById(R.id.btn_search_movie);
        btnSearchGame = view.findViewById(R.id.btn_search_game);

        searchAdapter = new MovieAdapter(searchResults);
        recyclerSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerSearchResults.setAdapter(searchAdapter);

        // SEKME DEĞİŞTİRME OLAYLARI
        btnSearchMovie.setOnClickListener(v -> {
            isSearchingGame = false;
            btnSearchMovie.setBackgroundColor(android.graphics.Color.parseColor("#E91E63"));
            btnSearchGame.setBackgroundColor(android.graphics.Color.parseColor("#1E1E1E"));
            editLiveSearch.setHint("Film veya dizi ara (En az 2 harf)...");
            triggerSearch(); // Sekme değişince aramayı yenile
        });

        btnSearchGame.setOnClickListener(v -> {
            isSearchingGame = true;
            btnSearchGame.setBackgroundColor(android.graphics.Color.parseColor("#E91E63"));
            btnSearchMovie.setBackgroundColor(android.graphics.Color.parseColor("#1E1E1E"));
            editLiveSearch.setHint("Oyun ara (En az 2 harf)...");
            triggerSearch(); // Sekme değişince aramayı yenile
        });

        // ARAMA KUTUSU DİNLEYİCİSİ
        editLiveSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                triggerSearch();
            }
        });

        return view;
    }

    private void triggerSearch() {
        String query = editLiveSearch.getText().toString().trim();
        if (query.length() >= 2) {
            performLiveSearch(query);
        } else {
            searchResults.clear();
            searchAdapter.notifyDataSetChanged();
        }
    }

    private void performLiveSearch(String query) {
        searchResults.clear();
        searchAdapter.notifyDataSetChanged();

        if (!isSearchingGame) {
            // SADECE FİLM/DİZİ ARA
            TmdbApiService tmdbApiService = ApiClient.getClient().create(TmdbApiService.class);
            tmdbApiService.searchMulti(API_KEY, query, "tr-TR").enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        searchResults.addAll(response.body().getResults());
                        searchAdapter.notifyDataSetChanged();
                    }
                }
                @Override public void onFailure(Call<MovieResponse> call, Throwable t) {}
            });
        } else {
            // SADECE OYUN ARA
            RawgApiService rawgApiService = RawgApiClient.getClient().create(RawgApiService.class);
            rawgApiService.searchGames(RAWG_API_KEY, query).enqueue(new Callback<GameResponse>() {
                @Override
                public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (Game game : response.body().getResults()) {
                            Movie gameAsMovie = new Movie(game.getName(), game.getBackgroundImage(), game.getRating());

                            // KRİTİK EKLENTİ BURADA: Bu öğe bir oyundur diye işaretliyoruz!
                            gameAsMovie.setGame(true);

                            searchResults.add(gameAsMovie);
                        }
                        searchAdapter.notifyDataSetChanged();
                    }
                }
                @Override public void onFailure(Call<GameResponse> call, Throwable t) {}
            });
        }
    }
}