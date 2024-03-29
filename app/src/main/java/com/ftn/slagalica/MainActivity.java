package com.ftn.slagalica;

import static com.ftn.slagalica.GameActivity.FIRST_ROUND_PLAYER_USERNAME;
import static com.ftn.slagalica.GameActivity.GAME_ID;
import static com.ftn.slagalica.GameActivity.MY_USERNAME_KEY;
import static com.ftn.slagalica.GameActivity.OPPONENT_USERNAME_KEY;
import static com.ftn.slagalica.util.AuthHandler.FIREBASE_URL;
import static com.ftn.slagalica.util.AuthHandler.Login.FILE_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.data.model.Player;
import com.ftn.slagalica.data.model.User;
import com.ftn.slagalica.ui.login.LoginActivity;
import com.ftn.slagalica.util.AuthHandler;
import com.ftn.slagalica.util.IThemeHandler;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements IThemeHandler {

    private DrawerLayout mDrawerLayout;
    private TabbedMainFragment tabbedMainFragment;
    private Fragment profileFragment;
//    Todo : load from Firebase in "onCreate"
    private Player loggedPlayer;
    private FirebaseDatabase database;
    private Timer matchStartTimerSyncer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Todo : load from Firebase here (this is for testing) :
//        loggedPlayer = new Player("Test", "test@gmail.com", "test", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/2w", 255, 11, 1);
        database = FirebaseDatabase.getInstance(FIREBASE_URL);

        AuthBearer ab = AuthHandler.Login.getLoggedPlayerCache(this);
        if (ab != null)
            loggedPlayer = new Player(ab.getUsername(), ab.getEmail(), ab.getPassword(), ab.getImageURI(), 0, 0, 0);
//        loggedPlayer = AuthHandler.Login.getLoggedPlayerCache(this);

        boolean darkThemeOn = setupTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AuthBearer loggedPlayerAuthData = AuthHandler.Login.getLoggedPlayerCache(this);

        setupSessionBasedUI(ab, darkThemeOn);

        setupToolbarAndActionbar();

        setupNavigationAndDrawer();

        loadMainFragment();
        Toast.makeText(MainActivity.this, "Po\u010detna", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (matchStartTimerSyncer != null)
            matchStartTimerSyncer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
//            case R.id.action_settings:
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMainFragment(){
        tabbedMainFragment = new TabbedMainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_main, tabbedMainFragment, "tabbedMainFrag").commit();
    }
    private void loadProfileFragment(){
//        TODO load profile data from database into some view template
//          profileFragment = new ProfileFragment(data_from_database);
        profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_main, profileFragment, "profileFrag").commit();
    }
    private void setupNavigationAndDrawer(){
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        Menu navMenu = navigationView.getMenu();
        MenuItem home = navMenu.findItem(R.id.nav_home);
        home.setChecked(true);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Toast.makeText(MainActivity.this, getString(R.string.on_homepage_toast), Toast.LENGTH_SHORT).show();
                    showMainFragment();
                    return true;
                case R.id.nav_profile:
                    Toast.makeText(MainActivity.this, getString(R.string.on_profile_toast), Toast.LENGTH_SHORT).show();
                    showProfileFragment();
                    return true;
                case R.id.nav_search_users:
                    toSearchUsers();
                    return true;
                case R.id.nav_logout:
                    Toast.makeText(MainActivity.this, getString(R.string.on_logout_toast), Toast.LENGTH_SHORT).show();
                    logout();
                    return true;
                case R.id.nav_login:
                    toLogin();
                    return true;
            }
            return true;
        });
    }

    //    * Loading MainActivity always inflates TabbedMainFragment onto it
    private void showMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (profileFragment != null) transaction.hide(profileFragment);

        transaction.show(tabbedMainFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_CLOSE).commit();
    }
//    * Loading MainActivity never implicitly inflates ProfileFragment
    private void showProfileFragment() {
        if (profileFragment == null) loadProfileFragment();

        getSupportFragmentManager().beginTransaction().hide(tabbedMainFragment).show(profileFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
    }

    private void setupToolbarAndActionbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        replaceToolbarTextWithIcon(actionBar, toolbar);
    }
    private void replaceToolbarTextWithIcon(ActionBar actionBar, Toolbar toolbar) {
        actionBar.setDisplayShowTitleEnabled(false);
//        toolbar.setLogo(R.mipmap.ic_default_profile);
    }

    private void toSearchUsers() {
        startActivity(new Intent(MainActivity.this, SearchUsersActivity.class));
        finish();
    }
    private void toLogin(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    public void logout(){
        AuthHandler.Login.forgetMe(getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE));
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    public void toGameActivity(View v){
        if (hasTokens()) {
            v.setEnabled(false);
            ((View)v.getParent()).setBackgroundColor(getResources().getColor(R.color.grey));
            DatabaseReference queueReference = database.getReference("queue");
//            String uidKey = reference.push().getKey();
            String matchRepresentingUsername = loggedPlayer != null ? loggedPlayer.getUsername() : ("gost" + System.currentTimeMillis()).substring(0, 14);

            queueReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    for (DataSnapshot queuertest : snapshot.getChildren()) {
//                        Toast.makeText(MainActivity.this, ((DataSnapshot)queuertest.getValue()).getValue().toString(), Toast.LENGTH_SHORT).show();
//                    }

//                    if (previousChildName != null) {
//                        Toast.makeText(MainActivity.this, "2nd:" + previousChildName,Toast.LENGTH_SHORT).show();
//                        for (DataSnapshot queuer : snapshot.getChildren()) {
//                            Toast.makeText(MainActivity.this, queuer.getValue().toString(),Toast.LENGTH_SHORT).show();
//                            if (previousChildName.equals(queuer.getKey())) {
//                                matchUpWithOpponent(snapshot, queuer.getValue().toString(), matchRepresentingUsername);
//                                break;
//                            }
//                        }
//                    }else{
//                        Iterator<DataSnapshot> queuersIter = snapshot.getChildren().iterator();
//                        Toast.makeText(MainActivity.this, "1st:" + previousChildName,Toast.LENGTH_SHORT).show();
//                        while (queuersIter.hasNext()){
//                            if (!matchRepresentingUsername.equals(queuersIter.next().getValue().toString())) {
//
//                                matchUpWithOpponent(snapshot, queuersIter.next().getValue().toString(), matchRepresentingUsername);
//                                break;
//                            }
//                        }
//                    }
                    String newlyAddedQueuer = snapshot.getValue(String.class);
//                    Toast.makeText(MainActivity.this, "prev "+ previousChildName, Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "new "+ snapshot.getKey(), Toast.LENGTH_LONG).show();

                    if (matchRepresentingUsername.equals(newlyAddedQueuer) && previousChildName != null) {
//                        Toast.makeText(MainActivity.this, "2.",Toast.LENGTH_LONG).show();
                        queueReference.child(previousChildName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String opponentUsername = snapshot.getValue(String.class);
//                                Toast.makeText(MainActivity.this, "oppo: " + opponentUsername,Toast.LENGTH_LONG).show();

                                if (!matchRepresentingUsername.equals(opponentUsername)) {
                                    queueReference.removeEventListener(this);
                                    matchUpWithOpponent(matchRepresentingUsername, opponentUsername);
    //                              Todo uncomment
                                    queueReference.child(snapshot.getKey()).removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
//                    }
                    }else{
                        if (!matchRepresentingUsername.equals(newlyAddedQueuer)) {
                            queueReference.removeEventListener(this);
    //                      Todo uncomment
                            if (matchRepresentingUsername.equals(newlyAddedQueuer))
                                queueReference.child(snapshot.getKey()).removeValue();
//                            Toast.makeText(MainActivity.this, "1.", Toast.LENGTH_LONG).show();
                            matchUpWithOpponent(matchRepresentingUsername, newlyAddedQueuer);
                        }
                    }
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
                    queueReference.push().setValue(matchRepresentingUsername)
                            .addOnCompleteListener(
                                    task2 -> {
                                        if (task2.isSuccessful()) {

                                            Toast.makeText(MainActivity.this, "Pronalazimo protivnika...", Toast.LENGTH_LONG).show();

                                        }
                                    }
                            );
//                }
//            }, 1000);

        }else{
            Toast.makeText(MainActivity.this, getString(R.string.not_enough_tokens_to_play), Toast.LENGTH_SHORT).show();
        }
    }

    private void matchUpWithOpponent(String myUsername, String opponentUsername) {
        DatabaseReference activeMatchRef = database.getReference("active_matches");
        final String[] firstRoundPlayerUsername = {null};
        database.getReference("queue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                matchStartTimerSyncer = new Timer();
                matchStartTimerSyncer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                    activeMatchRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            for (DataSnapshot activeMatch : snapshot.getChildren()) {
                                String activeMatchPlayerUsername = activeMatch.getValue(String.class);
//                                Toast.makeText(MainActivity.this, activeMatch.getValue(String.class), Toast.LENGTH_LONG).show();
                                if ( firstRoundPlayerUsername[0] == null ) {
                                    firstRoundPlayerUsername[0] = activeMatchPlayerUsername;
                                }

                                if (myUsername.equals(activeMatchPlayerUsername)) {

                                    activeMatchRef.removeEventListener(this);

                                    Activity currentParent = MainActivity.this;

                                    Intent toGameIntent = new Intent(currentParent, GameActivity.class);
                                    toGameIntent.putExtra(MY_USERNAME_KEY, myUsername);
                                    toGameIntent.putExtra(OPPONENT_USERNAME_KEY, opponentUsername);
                                    toGameIntent.putExtra(GAME_ID, snapshot.getKey());
                                    toGameIntent.putExtra(FIRST_ROUND_PLAYER_USERNAME, firstRoundPlayerUsername[0]);

                                    startActivity(toGameIntent);
                                    if (loggedPlayer != null)
                                        loggedPlayer.spendToken(database.getReference("users").child(loggedPlayer.getUsername()).child("tokens"));

                                    currentParent.finish();
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        }
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    }
                }, 4000);

                DataSnapshot playerInMatch = snapshot.getChildren().iterator().next();
                if (myUsername.equals(playerInMatch.getValue(String.class))) {
                    String activeMatchKey = activeMatchRef.push().getKey();
                    activeMatchRef.child(activeMatchKey).push().setValue(myUsername);
                    activeMatchRef.child(activeMatchKey).push().setValue(opponentUsername);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "active_match writing canceled...", Toast.LENGTH_LONG).show();
            }
        });
//        Query matchedPlayerFinder = activeMatchRef.orderByChild("username").equalTo(loggedUser.getUsername());
    }

    private boolean hasTokens() {
        return loggedPlayer == null ? true : loggedPlayer.getTokens() > 0;
    }

    public void showAvailableFriends(View v){

        if (AuthHandler.Login.getLoggedPlayerCache(this) == null) {
            Toast.makeText(MainActivity.this, getString(R.string.friendly_match_btn_alt), Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Dostupni prijatelji", Toast.LENGTH_SHORT).show();

        //         - openDrawer with FriendList under Log out btn (inv icons next to each available-to-play friend)
        //        mDrawerLayout.openDrawer(GravityCompat.START);
        //              ...
        //              or
        //         - show FriendList separately (either switch to Right Tab OR make a new Fragment on top of everything)
        //          ...
            tabbedMainFragment.showFriendsFragment();
        }
    }

    private void setupSessionBasedUI(AuthBearer loggedPlayerAuthData, boolean darkThemeOn) {

        View loggedUserCreditsToolbar = findViewById(R.id.toolbarLoggedUserCreditsContainer);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        View navHeader = navigationView.getHeaderView(0);
        ImageView profilePicView = navHeader.findViewById(R.id.drawer_profile_pic);

        Menu navMenu = navigationView.getMenu();
        MenuItem logItem;
        TextView usernameView = navHeader.findViewById(R.id.drawer_profile_username);
        TextView emailView = navHeader.findViewById(R.id.drawer_profile_email);
        SwitchCompat darkThemeSwitch = navHeader.findViewById(R.id.drawer_theme_light);

        if (loggedPlayerAuthData == null) {
            loggedUserCreditsToolbar.setVisibility(View.INVISIBLE);

            logItem = navMenu.findItem(R.id.nav_logout);
            profilePicView.setImageResource(R.mipmap.ic_profile_icon_round);
            emailView.setText(R.string.guest_user);
            navMenu.findItem(R.id.nav_profile).setVisible(false);
            navMenu.findItem(R.id.nav_search_users).setVisible(false);
        }else{
            setupLoggedUserCredits(loggedUserCreditsToolbar);

            logItem = navMenu.findItem(R.id.nav_login);
//            Todo
//            profilePicView.setImageURI(Uri.parse(loggedPlayerAuthData.getImageURI()));
            profilePicView.setImageResource(R.mipmap.ic_default_profile_round);
            emailView.setText(loggedPlayerAuthData.getEmail());
            usernameView.setText(loggedPlayerAuthData.getUsername());
        }
        logItem.setVisible(false);
        darkThemeSwitch.setChecked(darkThemeOn);
        darkThemeSwitch.setOnCheckedChangeListener(
                (compoundButton, darkModeChecked) -> {

                    compoundButton.setEnabled(false);
                    SharedPreferences.Editor spEditor = getSharedPreferences("theme", Context.MODE_PRIVATE).edit();
                    spEditor.putBoolean("dark", darkModeChecked);
                    spEditor.commit();

                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                }
        );
    }

    private void setupLoggedUserCredits(View loggedUserCreditsToolbar){
        Query loggedUserFinder = database.getReference("users").orderByChild("username").equalTo(loggedPlayer.getUsername());
        loggedUserFinder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User fetchedUser = snapshot.child(loggedPlayer.getUsername()).getValue(User.class);
//
                    loggedPlayer.setPointsCurrentRank(fetchedUser.getPoints());
                    loggedPlayer.setStars(fetchedUser.getStars());
                    loggedPlayer.setTokens(fetchedUser.getTokens());
//
                    loggedUserCreditsToolbar.setVisibility(View.VISIBLE);
                    TextView toolbarLoggedUserPoints = loggedUserCreditsToolbar.findViewById(R.id.toolbarLoggedUserPoints);
                    toolbarLoggedUserPoints.setText(String.valueOf(fetchedUser.getPoints()));
                    TextView toolbarLoggedUserStars = loggedUserCreditsToolbar.findViewById(R.id.toolbarLoggedUserStars);
                    toolbarLoggedUserStars.setText(String.valueOf(fetchedUser.getStars()));
                    TextView toolbarLoggedUserTokens = loggedUserCreditsToolbar.findViewById(R.id.toolbarLoggedUserTokens);
                    toolbarLoggedUserTokens.setText(String.valueOf(fetchedUser.getTokens()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}