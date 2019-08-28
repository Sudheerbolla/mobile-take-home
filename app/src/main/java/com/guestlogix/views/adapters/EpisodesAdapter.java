package com.guestlogix.views.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.R;
import com.guestlogix.listeners.IClickListener;
import com.guestlogix.models.EpisodeModel;

import java.util.ArrayList;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.MyViewHolder> {

    private ArrayList<EpisodeModel> episodeModelArrayList;
    private Context context;
    private IClickListener iClickListener;

    public EpisodesAdapter(Context context, ArrayList<EpisodeModel> episodeModelArrayList, IClickListener iClickListener) {
        this.context = context;
        this.iClickListener = iClickListener;
        this.episodeModelArrayList = episodeModelArrayList;
    }

    @NonNull
    @Override
    public EpisodesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EpisodesAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_episodes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final EpisodesAdapter.MyViewHolder holder, final int position) {
        EpisodeModel episodeModel = episodeModelArrayList.get(position);

        holder.txtAirDate.setText(episodeModel.airDate);
        if (TextUtils.isEmpty(episodeModel.episode)) {
            holder.txtEpisode.setVisibility(View.GONE);
        } else {
            holder.txtEpisode.setText(episodeModel.episode);
            holder.txtEpisode.setVisibility(View.VISIBLE);
        }
        holder.txtEpisodeName.setText(episodeModel.name);

        holder.cardEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iClickListener != null) iClickListener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodeModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView txtEpisodeName, txtAirDate, txtEpisode;
        private CardView cardEpisode;

        MyViewHolder(View view) {
            super(view);
            txtEpisodeName = view.findViewById(R.id.txtEpisodeName);
            txtAirDate = view.findViewById(R.id.txtAirDate);
            txtEpisode = view.findViewById(R.id.txtEpisode);
            cardEpisode = view.findViewById(R.id.cardEpisode);
        }
    }

}