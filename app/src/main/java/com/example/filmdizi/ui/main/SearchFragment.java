package com.example.filmdizi.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class SearchFragment extends Fragment {

    private EditText editLiveSearch;
    private RecyclerView recyclerSearchResults;
    private MovieAdapter searchAdapter;
    private List<Movie> searchResults = new ArrayList<>();
    private final String API_KEY = "9a8b276e5a73bf376fdfba9de1ba4b60";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editLiveSearch = view.findViewById(R.id.edit_live_search);
        recyclerSearchResults = view.findViewById(R.id.recycler_search_results);

        // Grid kullanarak sonuçları yan yana 2'li kutular halinde daha şık diziyoruz
        searchAdapter = new MovieAdapter(searchResults);
        recyclerSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerSearchResults.setAdapter(searchAdapter);

        // CANLI ARAMA (Harf girildikçe tetiklenir)
        editLiveSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();

                // 2 harf veya daha fazla yazıldıysa API'ye git
                if (query.length() >= 2) {
                    performLiveSearch(query);
                } else if (query.isEmpty()) {
                    // Yazı silinip kutu boşaldıysa listeyi temizle
                    searchResults.clear();
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void performLiveSearch(String query) {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);
        apiService.searchMulti(API_KEY, query, "tr-TR").enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchResults.clear();
                    searchResults.addAll(response.body().getResults());
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {}
        });
    }
}