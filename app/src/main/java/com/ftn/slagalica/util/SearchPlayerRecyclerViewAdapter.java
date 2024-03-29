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

public class SearchPlayerRecyclerViewAdapter extends RecyclerView.Adapter<SearchPlayerRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Player> players;
    private View.OnClickListener mClickListener;
    private final Context context;

    /**
     * Initialize the dataset of the Adapter
     *
     * @param players String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public SearchPlayerRecyclerViewAdapter(Context context, ArrayList<Player> players) {
        this.context = context;
        this.players = new ArrayList<>();
        this.players.addAll(players);

    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        notifyDataSetChanged();
    }

    public Player getPlayer(int pos) {
        return players.get(pos);
    }

    public void setClickListener(View.OnClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_people_row_item, viewGroup, false);
//                .inflate(R.layout.test_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.tvUsername.setText(array[position]);
        viewHolder.embedData(players.get(position));
        viewHolder.rowContainer.setOnClickListener(mClickListener);
//        viewHolder.itemView.setOnClickListener(mClickListener);
        viewHolder.rowContainer.setTag(viewHolder);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return players.size();
//        return array.length;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvUsername;
        private final TextView tvCurrentRank;
        private final TextView tvStars;
        private final ImageView imgPicture;
        private final LinearLayout rowContainer;

        public ViewHolder(View view) {
            super(view);
            tvUsername = view.findViewById(R.id.tvSearchRecyclerRowUsername);
            tvCurrentRank = view.findViewById(R.id.tvSearchRecyclerRowRank);
            tvStars = view.findViewById(R.id.tvSearchRecyclerRowStars);
            imgPicture = view.findViewById(R.id.ivSearchRecyclerRowPic);
            rowContainer = view.findViewById(R.id.rowContainerParent);
        }

        public void embedData(Player loadingPlayer){

//            tvUsername.setText(string);
            tvUsername.setText(loadingPlayer.getUsername());
            tvCurrentRank.setText( String.valueOf(loadingPlayer.getPointsCurrentRank()) );
            tvStars.setText( String.valueOf(loadingPlayer.getStars()) );
//            Todo default profile pic ->  loaded img parse
            imgPicture.setImageResource(R.mipmap.ic_battle_bird);
        }

        // Define click listener for the ViewHolder's View
//        @Override
//        public void onClick(View view) {
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
//        }
    }
}