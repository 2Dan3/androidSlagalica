package com.ftn.slagalica;

import android.content.Intent;
import android.os.Bundle;

import com.ftn.slagalica.data.model.Player;
import com.ftn.slagalica.util.IThemeHandler;
import com.ftn.slagalica.util.SearchPlayerRecyclerViewAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class SearchUsersActivity extends AppCompatActivity implements IThemeHandler {

//    private AppBarConfiguration appBarConfiguration;

    private RecyclerView playersRecyclerView;
    private SearchPlayerRecyclerViewAdapter adapter;
    private SearchView searchBar;
    private ArrayList<Player> searchResultPlayers;
    private ArrayList<Player> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupTheme(this);
        super.onCreate(savedInstanceState);

        initSetup();

        searchAndResultViewSetup();
    }

//    @Override
//    public void onStart() {
//        Toast.makeText(this, "on start started", Toast.LENGTH_SHORT).show();
//        super.onStart();
//        resetSearch();
//    }

    private void resetSearch() {
        searchBar.setQuery("", false);
        searchResultPlayers.clear();
        adapter.setPlayers(searchResultPlayers);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_search_users);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    private void initSetup() {
        setContentView(R.layout.activity_search_users);

        setSupportActionBar(findViewById(R.id.toolbarSearchActivity));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.search_players);
        Toast.makeText(SearchUsersActivity.this, getString(R.string.on_search_users_toast), Toast.LENGTH_LONG).show();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(SearchUsersActivity.this, MainActivity.class));
                finish();
            }
        });

//          Todo check lines :
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_search_users);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    private void searchAndResultViewSetup() {
        friends = new ArrayList<Player>();
        searchResultPlayers = new ArrayList<Player>();

        searchBar = findViewById(R.id.sv_search_players);
        playersRecyclerView = findViewById(R.id.recycler_players_search);

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

        playersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchPlayerRecyclerViewAdapter(this, this.searchResultPlayers);
        adapter.setClickListener(this::onItemClick);

        playersRecyclerView.setAdapter(adapter);
    }

    private boolean requestAddFriend(Player playerToBeAdded) {
        int code = 200;
        friends.add(playerToBeAdded);
//        Todo : Save new friend
//          - send request with updated "friends" OR "playerToBeAdded" only ->  "code" var should be updated onResponse

        if (code == 200) {
            return true;
        }else {
            friends.remove(playerToBeAdded);
            return false;
        }
    }

    private boolean requestSearchedPlayers() {

        searchResultPlayers.clear();
        String criteria = searchBar.getQuery().toString().trim();

        if (criteria.length() != 0) {
//        Todo
//          DB request for list of Players whose usernames contain "criteria"

//        response = conn.getResponse();

//        MOCK SEARCH RESULTS
            ArrayList<Player> response = new ArrayList();
            response.add(new Player("milenko", "milenko00@gmail.com", "pass123", "http://imgur.com/", 250, 4, 2));
            response.add(new Player("milica", "milica98@outlook.com", "pass123", "http://imgur.com/", 280, 3, 3));
            response.add(new Player("milutin", "milutin12@yahoo.com", "pass123", "http://imgur.com/", 110, 8, 5));

            for (Player found : response) {
                if (found.getUsername().contains(criteria))
                    searchResultPlayers.add(found);
            }

//            searchResultPlayers = response;

            adapter.setPlayers(searchResultPlayers);
            return true;
        }
        return false;
    }


    public void onItemClick(View view) {
//        Player toBeAddedPlayer = searchResultPlayers.get(position);
//        Toast.makeText(getActivity(), "assertOnClickActionWorks", Toast.LENGTH_LONG).show();
        SearchPlayerRecyclerViewAdapter.ViewHolder viewHolder = (SearchPlayerRecyclerViewAdapter.ViewHolder) view.getTag();
        int position = viewHolder.getAdapterPosition();
//        TEMPORARILY Commented :
//        Player newFriendToBeAdded = searchResultPlayers.get(position);
        Player newFriendToBeAdded = adapter.getPlayer(position);

        makePopup(view, newFriendToBeAdded);
    }

    private void makePopup(View anchor, Player newFriendToBeAdded) {
            PopupMenu popupMenu = new PopupMenu(SearchUsersActivity.this, anchor);
            popupMenu.getMenuInflater().inflate(R.menu.searched_players_actions_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(
                    menuItem -> {

                        switch (menuItem.getItemId()){
//                            case R.id.show_user_profile:
//                                return true;
                            case R.id.add_friend:
                                addFriend(newFriendToBeAdded);
                                return true;
                        }
                        return true;
                    }
            );
            popupMenu.show();
    }

    private void addFriend(Player newFriendToBeAdded) {
        boolean playerIsFriend = false;
//        Todo
//          UI: set different view temporary background gradients (red/green) for different if branch actions (remove/add)
        for (Player friend : requestFriendsList()) {
            if (friend.getUsername().equals(newFriendToBeAdded.getUsername())) {
                playerIsFriend = true;
                break;
            }
        }
//        if ( requestFriendsList().contains(newFriendToBeAdded) ) {
        if (playerIsFriend) {
            Toast.makeText(SearchUsersActivity.this, newFriendToBeAdded.getUsername() + " je ve\u0107 prijatelj.", Toast.LENGTH_SHORT).show();

        }else {
            if (requestAddFriend(newFriendToBeAdded)) {
                Toast.makeText(SearchUsersActivity.this, newFriendToBeAdded.getUsername() + getString(R.string.added_friend_msg), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SearchUsersActivity.this, "Desila se gre\u0161ka pri dodavanju " + newFriendToBeAdded.getUsername() + " za prijatelja.", Toast.LENGTH_SHORT).show();
            }
        }
//        resetSearch();
    }

    public ArrayList<Player> requestFriendsList() {
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