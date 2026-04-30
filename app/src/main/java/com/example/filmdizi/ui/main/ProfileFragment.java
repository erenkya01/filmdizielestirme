package com.example.filmdizi.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmdizi.R;
import com.example.filmdizi.adapters.MovieAdapter;
import com.example.filmdizi.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerItems;
    private Button btnFavorites, btnRated;
    private TextView textProfileEmail;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerItems = view.findViewById(R.id.recycler_rated_items);
        btnFavorites = view.findViewById(R.id.btn_tab_favorites);
        btnRated = view.findViewById(R.id.btn_tab_rated);
        textProfileEmail = view.findViewById(R.id.text_profile_email);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerItems.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if(currentUser != null && currentUser.getEmail() != null) {
            textProfileEmail.setText(currentUser.getEmail());
        }

        // Sayfa açıldığında otomatik Favorileri Getir
        fetchFavoritesFromCloud();

        btnFavorites.setOnClickListener(v -> {
            btnFavorites.setBackgroundColor(android.graphics.Color.parseColor("#E91E63"));
            btnRated.setBackgroundColor(android.graphics.Color.parseColor("#1E1E1E"));
            fetchFavoritesFromCloud();
        });

        btnRated.setOnClickListener(v -> {
            btnRated.setBackgroundColor(android.graphics.Color.parseColor("#E91E63"));
            btnFavorites.setBackgroundColor(android.graphics.Color.parseColor("#1E1E1E"));
            // Şimdilik burası boş
            recyclerItems.setAdapter(new MovieAdapter(new ArrayList<>()));
        });

        return view;
    }

    private void fetchFavoritesFromCloud() {
        if (currentUser != null) {
            db.collection("Users").document(currentUser.getUid()).collection("Favorites")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Movie> favList = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Movie movie = doc.toObject(Movie.class);
                            favList.add(movie);
                        }
                        recyclerItems.setAdapter(new MovieAdapter(favList));
                    });
        }
    }
}