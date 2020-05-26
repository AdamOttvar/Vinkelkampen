package com.example.vinkelkampen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class RecyclerAdapterHighscore extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Participant> mData;

    // Data is passed into the constructor
    RecyclerAdapterHighscore(List<Participant> data) {
        this.mData = data;
    }

    // Inflates the row layout from xml when needed
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewscore_row, parent, false);
            return new ItemViewHolderScore(itemView);
        }
        else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewscore_header, parent, false);
            return new com.example.vinkelkampen.RecyclerAdapterGuesses.HeaderViewHolder(itemView);
        }
        else throw new IllegalArgumentException("Unexpected viewType: " + viewType);
    }

    // Binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolderScore){
            HeaderViewHolderScore headerViewHolder = (HeaderViewHolderScore) holder;

            headerViewHolder.textViewName.setText(R.string.default_name);
            headerViewHolder.textViewScore.setText(R.string.diff);
        }
        else if (holder instanceof ItemViewHolderScore){
            final ItemViewHolderScore itemViewHolder = (ItemViewHolderScore) holder;

            String participantName = mData.get(position-1).getParticipantName();
            itemViewHolder.textViewName.setText(participantName);

            float participantScore = mData.get(position-1).getTotalScore();
            itemViewHolder.textViewScore.setText(String.format(new Locale("sv","SE"),"%.2f", participantScore));
        }

    }

    // To differ between header and list items
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    // Stores and recycles views for header
    public static class HeaderViewHolderScore extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewScore;

        HeaderViewHolderScore(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.participantNameHeader);
            textViewScore = itemView.findViewById(R.id.participantScoreHeader);
        }

    }

    // Stores and recycles views for items as they are scrolled off screen
    public static class ItemViewHolderScore extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewScore;

        ItemViewHolderScore(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.participantName);
            textViewScore = itemView.findViewById(R.id.participantScore);
        }

    }

}
