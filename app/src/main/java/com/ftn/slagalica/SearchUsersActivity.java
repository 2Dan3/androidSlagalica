package com.ftn.slagalica;

import android.content.Intent;
import android.os.Bundle;

import com.ftn.slagalica.data.model.Player;
import com.ftn.slagalica.util.IThemeHandler;
import com.ftn.slagalica.util.SearchPlayerRecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.slagalica.databinding.ActivitySearchUsersBinding;

import java.util.ArrayList;

public class SearchUsersActivity extends AppCompatActivity implements IThemeHandler, SearchPlayerRecyclerViewAdapter.ItemClickListener {

//    private AppBarConfiguration appBarConfiguration;
    private ActivitySearchUsersBinding binding;

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
        binding = ActivitySearchUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarSearchActivity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        adapter.setClickListener(this);

        playersRecyclerView.setAdapter(adapter);
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

            adapter.setPlayers(searchResultPlayers);
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(View view, int position) {
//        Player toBeAddedPlayer = searchResultPlayers.get(position);
//        Toast.makeText(getActivity(), "assertOnClickActionWorks", Toast.LENGTH_LONG).show();


//        TEMPORARILY Commented :
        Player newFriendToBeAdded = adapter.getPlayer(position);
//        Player newFriendToBeAdded = searchResultPlayers.get(position);

//        Todo
//          UI: set different view temporary background gradients (red/green) for different if branch actions (remove/add)
        if ( requestFriendsList().contains(newFriendToBeAdded) ) {
            Toast.makeText(this, newFriendToBeAdded.getUsername() + " je ve\u0107 prijatelj.", Toast.LENGTH_LONG).show();

        }else {
            if (requestAddFriend(newFriendToBeAdded)) {
                Toast.makeText(this, newFriendToBeAdded.getUsername() + " je dodat za prijatelja.", Toast.LENGTH_LONG).show();
                friends.add(newFriendToBeAdded);
            } else {
//                Toast.makeText(this, "An error occurred adding " + adapter.getPlayer(position).getUsername() + " as Friend", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Desila se gre\u0161ka pri dodavanju " + newFriendToBeAdded.getUsername() + " za prijatelja.", Toast.LENGTH_LONG).show();
            }
        }
        resetSearch();
    }

    public ArrayList<Player> requestFriendsList() {
//        MOCK LIST of FRIENDS
        if (friends.isEmpty()) {
            friends.add(new Player("PuzzlePlayer123", "puzzler@gmail.com", "pass123", "http://imgur.com/", 320, 6));
            friends.add(new Player("SlagalicaSlayer", "slagalac@yahoo.com", "pass123", "http://imgur.com/", 220, 4));
            friends.add(new Player("Gamer697", "gamerr@gmail.com", "pass123", "http://imgur.com/", 440, 10));
            friends.add(new Player("Hotstreak", "streaker@outlook.com", "pass123", "http://imgur.com/", 550, 12));
            friends.add(new Player("MrSpeedrun101", "theycallmespeed@gmail.com", "pass123", "http://imgur.com/", 100, 10));
            friends.add(new Player("Alexiiss_boss", "alex97@gmail.com", "pass123", "http://imgur.com/", 300, 12));
            friends.add(new Player("Hotshot021", "shotty@yahoo.com", "pass123", "http://imgur.com/", 208, 3));
            friends.add(new Player("JigsawMaker00", "hiimjiggy@outlook.com", "pass123", "http://imgur.com/", 164, 7));
            friends.add(new Player("ThePuzzlePr0", "puzzler@yahoo.com", "pass123", "http://imgur.com/", 324, 8));
            friends.add(new Player("5uper3g0", "mr530@gmail.com", "pass123", "http://imgur.com/", 282, 2));
            //        Todo DB request Friends list for logged player
            //                  ...
        }

        return friends;
    }
}