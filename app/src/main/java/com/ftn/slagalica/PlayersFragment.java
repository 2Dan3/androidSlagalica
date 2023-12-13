package com.ftn.slagalica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.slagalica.data.model.Player;
import com.ftn.slagalica.util.SearchPlayerRecyclerViewAdapter;

import java.util.ArrayList;

public class PlayersFragment extends Fragment {

    private SearchPlayerRecyclerViewAdapter adapter;

    private ArrayList<Player> friends = new ArrayList<>();

    public PlayersFragment(){ }

    //    public static PlayersFragment newInstance(String param1, String param2) {
//        PlayersFragment fragment = new PlayersFragment();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friends = getFriendsList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_players, container, false);

        RecyclerView playersRecyclerView = rootView.findViewById(R.id.recycler_friends);

        adapter = new SearchPlayerRecyclerViewAdapter(this.getContext(), friends);
        adapter.setClickListener(this::onItemClick);

        playersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        playersRecyclerView.setAdapter(adapter);

        return rootView;
    }

    private void onItemClick(View view) {
        SearchPlayerRecyclerViewAdapter.ViewHolder viewHolder = (SearchPlayerRecyclerViewAdapter.ViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
        Player selectedFriend = adapter.getPlayer(position);
        makePopup(view, selectedFriend, position);
    }

    private void makePopup(View v, Player selectedPlayer, int playerListPosition) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.getMenuInflater().inflate(R.menu.friends_actions_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(
                menuItem -> {

                    switch (menuItem.getItemId()){
                        case R.id.challenge_to_game:
                            sendChallenge(selectedPlayer);
                            Toast.makeText(getActivity(), selectedPlayer.getUsername() + getString(R.string.challenge_sent_await_response), Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.remove_friend:
                            adapter.setPlayers(removeFriend(playerListPosition));
                            Toast.makeText(getActivity(), selectedPlayer.getUsername() + getString(R.string.removed_friend_msg), Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return true;
                }
        );
        popupMenu.show();
    }

    private void sendChallenge(Player challengeReceiver) {
//            Todo
//             - send them a challenge & wait for response
//             - Start game if accepted
//                  OR show Toast if not accepted
//                  OR show Toast if player is unavailable (is in game or not online).

    }

    private ArrayList<Player> removeFriend(int playerListPosition) {
        friends.remove(playerListPosition);
        return friends;
    }

    private ArrayList<Player> getFriendsList() {

//        MOCK LIST of FRIENDS
        if (friends.isEmpty()) {

            friends.add(new Player("PuzzlePlayer123", "puzzler@gmail.com", "pass123", "http://imgur.com/", 320, 6, 2));
            friends.add(new Player("SlagalicaSlayer", "slagalac@yahoo.com", "pass123", "http://imgur.com/", 220, 4, 3));
            friends.add(new Player("Gamer697", "gamerr@gmail.com", "pass123", "http://imgur.com/", 440, 10, 3));
            friends.add(new Player("Hotstreak", "streaker@outlook.com", "pass123", "http://imgur.com/", 550, 12, 3));
            friends.add(new Player("MrSpeedrun101", "theycallmespeed@gmail.com", "pass123", "http://imgur.com/", 100, 10, 4));
            friends.add(new Player("Alexiiss_boss", "alex97@gmail.com", "pass123", "http://imgur.com/", 300, 12, 4));
            friends.add(new Player("Hotshot021", "shotty@yahoo.com", "pass123", "http://imgur.com/", 208, 3, 5));
            friends.add(new Player("JigsawMaker00", "hiimjiggy@outlook.com", "pass123", "http://imgur.com/", 164, 7, 5));
            friends.add(new Player("ThePuzzlePr0", "puzzler@yahoo.com", "pass123", "http://imgur.com/", 324, 8, 3));
            friends.add(new Player("5uper3g0", "mr530@gmail.com", "pass123", "http://imgur.com/", 282, 2, 4));
            //        Todo DB request Friends list for logged player
            //                  ...
        }

        return friends;
    }

}