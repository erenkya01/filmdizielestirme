package com.example.filmdizi.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmdizi.R;
import com.example.filmdizi.adapters.MovieAdapter;
import com.example.filmdizi.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerRatedItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerRatedItems = view.findViewById(R.id.recycler_rated_items);

        // Puanladıklarım listesini 2 sütunlu Izgara (Grid) şeklinde ayarlıyoruz
        recyclerRatedItems.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Puanladığın içeriklerin yerel (dummy) listesi
        List<Movie> ratedList = new ArrayList<>();
        ratedList.add(new Movie("Interstellar", "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MvrIdZ2O.jpg", 9.5));
        ratedList.add(new Movie("The Last of Us", "https://image.tmdb.org/t/p/w500/u3bZgnGQ9T01sWNhyveQz0wH0Hl.jpg", 9.8));
        ratedList.add(new Movie("The Matrix", "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", 8.8));
        ratedList.add(new Movie("Red Dead Redemption 2", "https://image.tmdb.org/t/p/w500/uF5jExT4Knb0LqO6jU6G4K6K4rF.jpg", 10.0));

        // Aynı MovieAdapter'ı burada da kullanarak kod tasarrufu yapıyoruz
        recyclerRatedItems.setAdapter(new MovieAdapter(ratedList));

        return view;
    }
}