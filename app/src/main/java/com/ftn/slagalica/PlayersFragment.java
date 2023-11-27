package com.ftn.slagalica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

//    private SearchPlayerRecyclerViewAdapter adapter;
//    private String[] array;

//    private ArrayList<Player> searchResultPlayers = new ArrayList();
    private ArrayList<Player> friends = new ArrayList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friends = getFriendsList();

//        Experimental line :
//        searchResultPlayers = friends;

//        searchResultPlayers.addAll(friends);

//        array = new String[]{"PuzzlePlayer123", "SlagalicaSlayer", "Gamer697", "Hotstreak"};
//        Toast.makeText(getContext(), array[3], Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_players, container, false);

//        RecyclerView playersRecyclerView = rootView.findViewById(R.id.recycler_friends);
//        SearchPlayerRecyclerViewAdapter adapter = (SearchPlayerRecyclerViewAdapter)playersRecyclerView.getAdapter();
//        adapter.setClickListener(this);
//
//        playersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
////        Nothing's been searched yet, so Friend List is displayed as a default
////        Toast.makeText(getContext(), friends.get(9).toString(), Toast.LENGTH_LONG).show();
//
//        adapter = new SearchPlayerRecyclerViewAdapter(this.getContext(), friends);
//        adapter.setClickListener(this);
//
//        playersRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        searchBar = view.findViewById(R.id.sv_search_players);
//        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return requestSearchedPlayers();
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
    }

    private ArrayList<Player> getFriendsList() {

        return TabbedMainFragment.requestFriendsList();
    }

    @Override
    public void onItemClick(View view, int position) {

//        Player toBeAddedPlayer = searchResultPlayers.get(position);
//        Toast.makeText(getActivity(), "assertOnClickActionWorks", Toast.LENGTH_LONG).show();


//        TEMPORARILY Commented :
//        Player newFriendToBeAdded = adapter.getPlayer(position);
        Player newFriendToBeAdded = friends.get(position);

//        Todo
//          UI: set different view temporary background gradients (red/green) for different if branch actions (remove/add)
        if (friends.contains(newFriendToBeAdded)) {
//            Toast.makeText(getActivity(), adapter.getPlayer(position).getUsername() + " is removed from your Friends", Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), friends.get(position).getUsername() + " is removed from your Friends", Toast.LENGTH_LONG).show();
        } else {

            if (requestAddFriend(newFriendToBeAdded)) {
                Toast.makeText(getActivity(), "You added " + newFriendToBeAdded.getUsername() + " as your Friend", Toast.LENGTH_LONG).show();
                friends.add(newFriendToBeAdded);
            } else {
//                Toast.makeText(getActivity(), "An error occurred adding " + adapter.getPlayer(position).getUsername() + " as Friend", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "An error occurred adding " + friends.get(position).getUsername() + " as Friend", Toast.LENGTH_LONG).show();
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

//    private boolean requestSearchedPlayers() {
//
//        String criteria = searchBar.getQuery().toString().trim();
//
//        if (criteria.length() != 0) {
////        Todo
////          DB request for list of Players whose usernames contain "criteria"
//
////        response = conn.getResponse();
//
////        MOCK SEARCH RESULTS
//            ArrayList<Player> response = new ArrayList();
//            response.add(new Player(criteria + "enko", criteria + "enko00@gmail.com", "pass123", "http://imgur.com/", 250, 4));
//            response.add(new Player(criteria + "ica", criteria + "ica98@outlook.com", "pass123", "http://imgur.com/", 280, 3));
//            response.add(new Player(criteria + "utin", criteria + "utin12@yahoo.com", "pass123", "http://imgur.com/", 110, 8));
//
//            searchResultPlayers.addAll(response);
////            adapter.notifyDataSetChanged();
////            Todo uncomment :
//            Toast.makeText(getActivity(), "testic" + searchResultPlayers.get(2).toString(), Toast.LENGTH_LONG).show();
//
//            return true;
//        }
//        return false;
//    }

}