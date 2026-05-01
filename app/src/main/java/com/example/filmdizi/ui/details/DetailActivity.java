package com.example.filmdizi.ui.details;
import android.widget.RatingBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.filmdizi.models.Movie;
import com.example.filmdizi.models.SeasonResponse;
import com.example.filmdizi.models.TvExternalIdsResponse;
import com.example.filmdizi.models.TvShowDetailResponse;
import com.example.filmdizi.network.ApiClient;
import com.example.filmdizi.network.TmdbApiService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleView, imdbScoreView, seasonTitle, episodesTitle, textScoreType;
    private Spinner spinnerSeasons;
    private RecyclerView recyclerEpisodes;
    private Button btnWatch, btnFavorite;

    private EpisodeAdapter episodeAdapter;
    private List<Episode> episodeList = new ArrayList<>();
    private final String API_KEY = "9a8b276e5a73bf376fdfba9de1ba4b60";

    private String currentImdbId = "";
    private Movie currentItemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.detail_image);
        ImageView imageBg = findViewById(R.id.detail_image_bg);
        titleView = findViewById(R.id.detail_title);
        imdbScoreView = findViewById(R.id.text_imdb);
        textScoreType = findViewById(R.id.text_score_type); // YENİ
        spinnerSeasons = findViewById(R.id.spinner_seasons);
        recyclerEpisodes = findViewById(R.id.recycler_episodes);
        seasonTitle = findViewById(R.id.text_season_title);
        episodesTitle = findViewById(R.id.text_episodes_title);
        btnWatch = findViewById(R.id.btn_watch);
        btnFavorite = findViewById(R.id.btn_favorite);

        episodeAdapter = new EpisodeAdapter(episodeList);
        recyclerEpisodes.setLayoutManager(new LinearLayoutManager(this));
        recyclerEpisodes.setAdapter(episodeAdapter);

        // Gelen Verileri Alıyoruz
        int id = getIntent().getIntExtra("id", -1);
        boolean isMovie = getIntent().getBooleanExtra("isMovie", false);
        boolean isGame = getIntent().getBooleanExtra("isGame", false); // Oyun mu?
        String title = getIntent().getStringExtra("title");
        String poster = getIntent().getStringExtra("poster");
        double vote = getIntent().getDoubleExtra("vote", 0.0);

        currentItemModel = new Movie(title, poster, vote);

        titleView.setText(title);
        imdbScoreView.setText(String.format("%.1f", vote));
        Glide.with(this).load(poster).into(imageView);
        Glide.with(this).load(poster).into(imageBg);

        // UI MANTIĞI: Oyun mu, Film/Dizi mi?
        if (isGame) {
            // OYUN İSE:
            btnWatch.setVisibility(View.GONE); // İzle butonunu gizle
            textScoreType.setText("RAWG Puanı"); // IMDb yerine RAWG Puanı yaz

            spinnerSeasons.setVisibility(View.GONE);
            seasonTitle.setVisibility(View.GONE);
            episodesTitle.setVisibility(View.GONE);
            recyclerEpisodes.setVisibility(View.GONE);
        } else {
            // FİLM VEYA DİZİ İSE:
            fetchImdbId(id, isMovie);

            if (isMovie || id == -1) {
                spinnerSeasons.setVisibility(View.GONE);
                seasonTitle.setVisibility(View.GONE);
                episodesTitle.setVisibility(View.GONE);
                recyclerEpisodes.setVisibility(View.GONE);
            } else {
                fetchTvDetails(id);
            }
        }

        // İZLE BUTONU (Sadece filmler ve diziler için görünür)
        btnWatch.setOnClickListener(v -> {
            if (!currentImdbId.isEmpty()) {
                String url = "https://www.playimdb.com/title/" + currentImdbId + "/";
                Intent intent = new Intent(DetailActivity.this, PlayerActivity.class);
                intent.putExtra("VIDEO_URL", url);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Link hazırlanıyor veya bulunamadı...", Toast.LENGTH_SHORT).show();
            }
        });

        // FAVORİLERE EKLE BUTONU (Firestore'a yazar)
        btnFavorite.setOnClickListener(v -> saveToFavorites(currentItemModel));
        // --- PUANLAMA (RATING) MANTIĞI ---
        RatingBar ratingBar = findViewById(R.id.user_rating_bar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 1. Kullanıcı sayfaya girdiğinde daha önce puan vermişse o puanı yıldızlarda göster
        if (user != null) {
            String docId = currentItemModel.getTitle().replace("/", "_");
            db.collection("Users").document(user.getUid())
                    .collection("Rated").document(docId)
                    .get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Double savedRating = documentSnapshot.getDouble("userRating");
                            if (savedRating != null) {
                                ratingBar.setRating(savedRating.floatValue());
                            }
                        }
                    });
        }

        // 2. Kullanıcı yeni puan verdiğinde bunu buluta kaydet
        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            if (fromUser) {
                saveRatingToCloud(currentItemModel, rating);
            }
        });
    }

    private void fetchImdbId(int id, boolean isMovie) {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);
        if (isMovie) {
            apiService.getMovieDetails(id, API_KEY, "tr-TR").enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentImdbId = response.body().getImdbId();
                    }
                }
                @Override public void onFailure(Call<Movie> call, Throwable t) {}
            });
        } else {
            apiService.getTvExternalIds(id, API_KEY).enqueue(new Callback<TvExternalIdsResponse>() {
                @Override
                public void onResponse(Call<TvExternalIdsResponse> call, Response<TvExternalIdsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentImdbId = response.body().getImdbId();
                    }
                }
                @Override public void onFailure(Call<TvExternalIdsResponse> call, Throwable t) {}
            });
        }
    }

    private void saveToFavorites(Movie movie) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Veritabanı dosya ismi hata vermesin diye / gibi karakterleri temizliyoruz
            String docId = movie.getTitle().replace("/", "_");

            db.collection("Users").document(user.getUid())
                    .collection("Favorites").document(docId)
                    .set(movie)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Favorilere eklendi!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Favoriye eklemek için giriş yapmalısınız!", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveRatingToCloud(Movie movie, float rating) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String docId = movie.getTitle().replace("/", "_");

            // Hem filmi kaydediyoruz (Profilde göstermek için) hem de verilen puanı ekliyoruz
            db.collection("Users").document(user.getUid())
                    .collection("Rated").document(docId)
                    .set(movie)
                    .addOnSuccessListener(aVoid -> {
                        // Film objesi kaydedildikten sonra "userRating" alanını da yanına ekliyoruz
                        db.collection("Users").document(user.getUid())
                                .collection("Rated").document(docId)
                                .update("userRating", rating);

                        Toast.makeText(this, "Puanınız kaydedildi: " + rating, Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Puan vermek için giriş yapmalısınız!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchTvDetails(int tvId) {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);
        apiService.getTvShowDetails(tvId, API_KEY, "tr-TR").enqueue(new Callback<TvShowDetailResponse>() {
            @Override
            public void onResponse(Call<TvShowDetailResponse> call, Response<TvShowDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupSeasonSpinner(tvId, response.body().getNumberOfSeasons());
                }
            }
            @Override public void onFailure(Call<TvShowDetailResponse> call, Throwable t) {}
        });
    }

    private void setupSeasonSpinner(int tvId, int count) {
        List<String> seasons = new ArrayList<>();
        for (int i = 1; i <= count; i++) seasons.add(i + ". Sezon");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seasons) {
            @Override
            public View getView(int pos, View convert, ViewGroup p) {
                TextView tv = (TextView) super.getView(pos, convert, p);
                tv.setTextColor(Color.WHITE); return tv;
            }
            @Override
            public View getDropDownView(int pos, View convert, ViewGroup p) {
                TextView tv = (TextView) super.getDropDownView(pos, convert, p);
                tv.setTextColor(Color.WHITE); tv.setBackgroundColor(Color.parseColor("#1E1E1E")); return tv;
            }
        };

        spinnerSeasons.setAdapter(adapter);
        spinnerSeasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) { fetchEpisodes(tvId, pos + 1); }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    private void fetchEpisodes(int tvId, int season) {
        TmdbApiService apiService = ApiClient.getClient().create(TmdbApiService.class);
        apiService.getSeasonDetails(tvId, season, API_KEY, "tr-TR").enqueue(new Callback<SeasonResponse>() {
            @Override
            public void onResponse(Call<SeasonResponse> call, Response<SeasonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    episodeList.clear();
                    episodeList.addAll(response.body().getEpisodes());
                    episodeAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<SeasonResponse> call, Throwable t) {}
        });
    }
}