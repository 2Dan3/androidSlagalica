package com.ftn.slagalica.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.slagalica.R;
import com.ftn.slagalica.data.model.Player;

import java.util.ArrayList;

public class SearchPlayerRecyclerViewAdapter extends RecyclerView.Adapter<SearchPlayerRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Player> players;
    private ItemClickListener mClickListener;
    private final Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tvUsername;
        private final TextView tvCurrentRank;
        private final TextView tvStars;
        private final ImageView imgPicture;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvUsername = view.findViewById(R.id.tvSearchRecyclerRowUsername);
            tvCurrentRank = view.findViewById(R.id.tvSearchRecyclerRowRank);
            tvStars = view.findViewById(R.id.tvSearchRecyclerRowStars);
            imgPicture = view.findViewById(R.id.ivSearchRecyclerRowPic);
        }

        public void embedData(Player loadingPlayer){

            tvUsername.setText(loadingPlayer.getUsername());
            tvCurrentRank.setText(loadingPlayer.getPointsCurrentRank());
            tvStars.setText(loadingPlayer.getStars());
//            Todo default profile pic ->  loaded img parse
            imgPicture.setImageResource(R.drawable.menu_profile);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Player getPlayer(int pos) {
        return players.get(pos);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param players String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public SearchPlayerRecyclerViewAdapter(Context context, ArrayList<Player> players) {
        this.context = context;
        this.players = players;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
//        Todo if buggy, might need replacement context ->  viewGroup.getContext()
        View view = LayoutInflater.from(context)
                .inflate(R.layout.search_people_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.embedData(players.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return players.size();
    }
}