package com.ftn.slagalica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.slagalica.data.model.Player;
import com.ftn.slagalica.util.SearchPlayerRecyclerViewAdapter;

import java.util.ArrayList;

public class PlayersFragment extends Fragment implements SearchPlayerRecyclerViewAdapter.ItemClickListener {

    private SearchPlayerRecyclerViewAdapter adapter;
    private SearchView searchBar;

    private ArrayList<Player> searchResultPlayers;
    private ArrayList<Player> friends;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friends = requestFriendsList();

        RecyclerView playersRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_players_search);
//        getContext or getActivity ?
        playersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        Nothing's been searched yet, so Friend List is displayed as a default

        adapter = new SearchPlayerRecyclerViewAdapter(this.getContext(), friends);
        adapter.setClickListener(this);

        playersRecyclerView.setAdapter(adapter);
    }

//    Todo
//      if buggy replace with onStart
//    @Override
//    public void onResume() {
//        super.onResume();
//        searchBar.setQuery("", false);
//        searchResultPlayers = friends;
//        adapter.notifyDataSetChanged();
////        Todo clear search view of any text
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_players, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBar = getActivity().findViewById(R.id.sv_search_players);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return requestSearchedPlayers();
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private ArrayList<Player> requestFriendsList() {

//        MOCK LIST of FRIENDS
        ArrayList<Player> resultList = new ArrayList<>();
        resultList.add(new Player("PuzzlePlayer123", "puzzler@gmail.com", "pass123", "http://imgur.com/", 320, 6));
        resultList.add(new Player("SlagalicaSlayer", "slagalac@yahoo.com", "pass123", "http://imgur.com/", 220, 4));
        resultList.add(new Player("Gamer697", "gamerr@gmail.com", "pass123", "http://imgur.com/", 440, 10));
        resultList.add(new Player("Hotstreak", "streaker@outlook.com", "pass123", "http://imgur.com/", 550, 12));
        resultList.add(new Player("MrSpeedrun101", "theycallmespeed@gmail.com", "pass123", "http://imgur.com/", 100, 10));
        resultList.add(new Player("Alexiiss_boss", "alex97@gmail.com", "pass123", "http://imgur.com/", 300, 12));
        resultList.add(new Player("Hotshot021", "shotty@yahoo.com", "pass123", "http://imgur.com/", 208, 3));
        resultList.add(new Player("JigsawMaker00", "hiimjiggy@outlook.com", "pass123", "http://imgur.com/", 164, 7));
        resultList.add(new Player("ThePuzzlePr0", "puzzler@yahoo.com", "pass123", "http://imgur.com/", 324, 8));
        resultList.add(new Player("5uper3g0", "mr530@gmail.com", "pass123", "http://imgur.com/", 282, 2));
        //        Todo DB request Friends list for logged player
        //                  ...

        return resultList;
    }

    @Override
    public void onItemClick(View view, int position) {

//        Player toBeAddedPlayer = searchResultPlayers.get(position);
        Player newFriendToBeAdded = adapter.getPlayer(position);

        if (friends.contains(newFriendToBeAdded)) {
            Toast.makeText(getActivity(), adapter.getPlayer(position).getUsername() + " is removed from your Friends", Toast.LENGTH_LONG).show();

        } else {

            if (requestAddFriend(newFriendToBeAdded)) {
                Toast.makeText(getActivity(), "You added " + newFriendToBeAdded.getUsername() + " as your Friend", Toast.LENGTH_LONG).show();
                friends.add(newFriendToBeAdded);
            } else {
                Toast.makeText(getActivity(), "An error occurred adding " + adapter.getPlayer(position).getUsername() + " as Friend", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean requestAddFriend(Player playerToBeAdded) {
        int code = 200;
//        Todo
//          DB request for "playerToBeAdded" ->  "code" updated onResponse
        if (code == 404) {
            return false;
        }
        return true;
    }

    private boolean requestSearchedPlayers() {

        String criteria = searchBar.getQuery().toString().trim();

        if (criteria.length() != 0) {
//        Todo
//          DB request for list of Players whose usernames contain "criteria"

//        response = conn.getResponse();

//        MOCK SEARCH RESULTS
            ArrayList<Player> response = new ArrayList();
            response.add(new Player(criteria + "enko", criteria + "enko00@gmail.com", "pass123", "http://imgur.com/", 250, 4));
            response.add(new Player(criteria + "ica", criteria + "ica98@outlook.com", "pass123", "http://imgur.com/", 280, 3));
            response.add(new Player(criteria + "utin", criteria + "utin12@yahoo.com", "pass123", "http://imgur.com/", 110, 8));

            searchResultPlayers = response;
            adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "SUPERNILOTEST"+searchResultPlayers.get(2).toString(), Toast.LENGTH_LONG).show();

            return true;
        }
        return false;
    }

}