package com.example.filmdizi.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.filmdizi.R;
import com.example.filmdizi.models.Movie;
import com.example.filmdizi.ui.details.DetailActivity;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> itemList;

    public MovieAdapter(List<Movie> itemList) { this.itemList = itemList; }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie item = itemList.get(position);
        holder.textTitle.setText(item.getTitle());

        String finalUrl = item.getPosterPath() != null && item.getPosterPath().startsWith("http") ?
                item.getPosterPath() : "https://image.tmdb.org/t/p/w500" + item.getPosterPath();

        Glide.with(holder.itemView.getContext()).load(finalUrl).into(holder.imagePoster);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("poster", finalUrl);
            intent.putExtra("vote", item.getVoteAverage());
            intent.putExtra("isMovie", item.isMovie()); // Kritik Bilgi
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return itemList != null ? itemList.size() : 0; }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        ImageView imagePoster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            imagePoster = itemView.findViewById(R.id.image_poster);
        }
    }
}