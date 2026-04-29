package com.example.filmdizi.ui.details;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filmdizi.R;
import com.example.filmdizi.adapters.EpisodeAdapter;
import com.example.filmdizi.models.Episode;
import com.example.filmdizi.models.SeasonResponse;
import com.example.filmdizi.models.TvShowDetailResponse;
import com.example.filmdizi.network.ApiClient;
import com.example.filmdizi.network.TmdbApiService;

import java.util.ArrayList;
import java.util.List;

// BURADAKİ İMPORTLAR ÇOK ÖNEMLİ, KIRMIZILIĞI BUNLAR ÇÖZER:
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleView, imdbScoreView, seasonTitle, episodesTitle;
    private Spinner spinnerSeasons;
    private RecyclerView recyclerEpisodes;
    private EpisodeAdapter episodeAdapter;
    private List<Episode> episodeList = new ArrayList<>();
    private final String API_KEY = "9a8b276e5a73bf376fdfba9de1ba4b60";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // XML Bağlantıları
        imageView = findViewById(R.id.detail_image);
        titleView = findViewById(R.id.detail_title);
        imdbScoreView = findViewById(R.id.text_imdb);
        spinnerSeasons = findViewById(R.id.spinner_seasons);
        recyclerEpisodes = findViewById(R.id.recycler_episodes);
        seasonTitle = findViewById(R.id.text_season_title);
        episodesTitle = findViewById(R.id.text_episodes_title);

        // RecyclerView Hazırlığı
        episodeAdapter = new EpisodeAdapter(episodeList);
        recyclerEpisodes.setLayoutManager(new LinearLayoutManager(this));
        recyclerEpisodes.setAdapter(episodeAdapter);

        // Verileri Yakala (Intent)
        int id = getIntent().getIntExtra("id", -1);
        boolean isMovie = getIntent().getBooleanExtra("isMovie", false);
        String title = getIntent().getStringExtra("title");
        String poster = getIntent().getStringExtra("poster");
        double vote = getIntent().getDoubleExtra("vote", 0.0);

        // Arayüzü Doldur
        titleView.setText(title);
        imdbScoreView.setText(String.format("%.1f", vote));
        Glide.with(this).load(poster).into(imageView);

        // Film mi Dizi mi Kontrolü
        if (isMovie || id == -1) {
            hideSeriesElements();
        } else {
            showSeriesElements();
            fetchTvDetails(id); // Dizi ise detayları çek
        }
    }

    private void fetchTvDetails(int tvId) {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);

        // Bu blok artık kırmızı yanmayacak:
        apiService.getTvShowDetails(tvId, API_KEY, "tr-TR").enqueue(new Callback<TvShowDetailResponse>() {
            @Override
            public void onResponse(Call<TvShowDetailResponse> call, Response<TvShowDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Kaç sezon varsa Spinner'ı ona göre kuruyoruz
                    setupSeasonSpinner(tvId, response.body().getNumberOfSeasons());
                }
            }

            @Override
            public void onFailure(Call<TvShowDetailResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Dizi detayları alınamadı", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSeasonSpinner(int tvId, int count) {
        List<String> seasons = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            seasons.add(i + ". Sezon");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seasons) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.WHITE);
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.parseColor("#1E1E1E"));
                return tv;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeasons.setAdapter(adapter);

        spinnerSeasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchEpisodes(tvId, position + 1); // Seçilen sezonun bölümlerini getir
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchEpisodes(int tvId, int seasonNumber) {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);
        apiService.getSeasonDetails(tvId, seasonNumber, API_KEY, "tr-TR").enqueue(new Callback<SeasonResponse>() {
            @Override
            public void onResponse(Call<SeasonResponse> call, Response<SeasonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    episodeList.clear();
                    episodeList.addAll(response.body().getEpisodes());
                    episodeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SeasonResponse> call, Throwable t) {}
        });
    }

    private void hideSeriesElements() {
        spinnerSeasons.setVisibility(View.GONE);
        recyclerEpisodes.setVisibility(View.GONE);
        if (seasonTitle != null) seasonTitle.setVisibility(View.GONE);
        if (episodesTitle != null) episodesTitle.setVisibility(View.GONE);
    }

    private void showSeriesElements() {
        spinnerSeasons.setVisibility(View.VISIBLE);
        recyclerEpisodes.setVisibility(View.VISIBLE);
        if (seasonTitle != null) seasonTitle.setVisibility(View.VISIBLE);
        if (episodesTitle != null) episodesTitle.setVisibility(View.VISIBLE);
    }
}