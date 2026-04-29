package com.example.filmdizi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filmdizi.R;
import com.example.filmdizi.models.Episode;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    private List<Episode> episodeList;

    public EpisodeAdapter(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        Episode episode = episodeList.get(position);
        holder.textNumber.setText(String.valueOf(episode.getEpisodeNumber()));
        holder.textName.setText(episode.getName());
        // Puanı burası yazdırıyor
        holder.textScore.setText("⭐ " + String.format("%.1f", episode.getVoteAverage()));
    }

    @Override
    public int getItemCount() { return episodeList.size(); }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        TextView textNumber, textName, textScore;
        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            textNumber = itemView.findViewById(R.id.text_ep_number);
            textName = itemView.findViewById(R.id.text_ep_name);
            textScore = itemView.findViewById(R.id.text_ep_score);
        }
    }
}