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
import com.example.filmdizi.models.Game;
import com.example.filmdizi.ui.details.DetailActivity;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gameList;

    public GameAdapter(List<Game> gameList) {
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.textTitle.setText(game.getName());

        if (game.getBackgroundImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(game.getBackgroundImage())
                    .into(holder.imagePoster);
        }

        // YENİ EKLENEN: Ana sayfadan tıklandığında oyun olduğunu bildir
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("id", game.getId());
            intent.putExtra("title", game.getName());
            intent.putExtra("poster", game.getBackgroundImage());
            intent.putExtra("vote", game.getRating());
            intent.putExtra("isGame", true); // BU BİR OYUN!
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gameList != null ? gameList.size() : 0;
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        ImageView imagePoster;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            imagePoster = itemView.findViewById(R.id.image_poster);
        }
    }
}