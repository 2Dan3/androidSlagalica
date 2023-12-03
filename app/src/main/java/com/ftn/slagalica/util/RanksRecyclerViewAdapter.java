package com.ftn.slagalica.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.slagalica.R;
import com.ftn.slagalica.data.model.Player;

import java.util.ArrayList;

public class RanksRecyclerViewAdapter extends RecyclerView.Adapter<RanksRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Player> players;

    public RanksRecyclerViewAdapter(ArrayList<Player> players){
        this.players = players;
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        notifyDataSetChanged();
    }
//    public Player getPlayer(int pos) {
//        return players.get(pos);
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ranked_player_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.embedData(players.get(position), position);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvRankOrderNum;
        private final TextView tvUsername;
        private final TextView tvCurrentRank;
        private final TextView tvStars;
        private final ImageView imgPicture;

        public ViewHolder(View view) {
            super(view);
            tvRankOrderNum = view.findViewById(R.id.tvRankOrderNum);
            tvUsername = view.findViewById(R.id.tvSearchRecyclerRowUsername);
            tvCurrentRank = view.findViewById(R.id.tvSearchRecyclerRowRank);
            tvStars = view.findViewById(R.id.tvSearchRecyclerRowStars);
            imgPicture = view.findViewById(R.id.ivSearchRecyclerRowPic);
        }

        public void embedData(Player loadingPlayer, int position){

            tvRankOrderNum.setText(String.valueOf(position + 1));
            tvUsername.setText(loadingPlayer.getUsername());
            tvCurrentRank.setText( String.valueOf(loadingPlayer.getPointsCurrentRank()) );
            tvStars.setText( String.valueOf(loadingPlayer.getStars()) );
//            Todo default profile pic ->  loaded img parse
            imgPicture.setImageResource(R.mipmap.ic_battle_bird);
        }
    }

}
