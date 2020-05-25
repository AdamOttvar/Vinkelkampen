package com.example.vinkelkampen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class RecyclerAdapterGuesses extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Participant> mData;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerAdapterGuesses(Context context, List<Participant> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewguess_row, parent, false);
            return new ItemViewHolder(itemView);
        }
        else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewguess_header, parent, false);
            return new HeaderViewHolder(itemView);
        }
        else return null;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.textViewName.setText(R.string.default_name);
            headerViewHolder.textViewGuess.setText(R.string.guess);
            headerViewHolder.textViewScore.setText(R.string.result);
        }
        else if (holder instanceof ItemViewHolder){
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            String participantName = mData.get(position-1).getParticipantName();
            itemViewHolder.textViewName.setText(participantName);

            float participantGuess = mData.get(position-1).getCurrentGuess();
            itemViewHolder.textViewGuess.setText(String.format(new Locale("sv","SE"),"%.2f", participantGuess));

            float participantScore = mData.get(position-1).getScore();
            itemViewHolder.textViewScore.setText(String.format(new Locale("sv","SE"),"%.2f", participantScore));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    // Stores and recycles views for header
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewGuess;
        TextView textViewScore;

        HeaderViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.participantNameHeader);
            textViewGuess = itemView.findViewById(R.id.participantGuessHeader);
            textViewScore = itemView.findViewById(R.id.participantScoreHeader);
        }

    }

    // Stores and recycles views for items as they are scrolled off screen
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName;
        TextView textViewGuess;
        TextView textViewScore;

        ItemViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.participantName);
            textViewGuess = itemView.findViewById(R.id.participantGuess);
            textViewScore = itemView.findViewById(R.id.participantScore);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Participant getItem(int id) {
        return mData.get(id-1);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
