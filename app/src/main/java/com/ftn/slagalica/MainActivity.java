package com.ftn.slagalica;

import static com.ftn.slagalica.util.LoginHandler.Login.FILE_NAME;
import static com.ftn.slagalica.util.LoginHandler.Login.USERNAME;
import static com.ftn.slagalica.util.LoginHandler.Login.EMAIL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.ftn.slagalica.ui.login.LoginActivity;
import com.ftn.slagalica.util.IThemeHandler;
import com.ftn.slagalica.util.LoginHandler;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements IThemeHandler {

    private DrawerLayout mDrawerLayout;
    private Fragment tabbedMainFragment;
    private Fragment profileFragment;
//    Todo : load from Firebase in "onCreate"
    private Player loggedPlayerFetched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Todo : load from Firebase here (this is for testing) :
        loggedPlayerFetched = new Player("Test", "test@gmail.com", "test", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/2w", 255, 11, 1);

        boolean darkThemeOn = setupTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthBearer loggedPlayerAuthData = LoginHandler.Login.getLoggedPlayerAuth(this);

        setupSessionBasedUI(loggedPlayerAuthData, darkThemeOn);

        setupToolbarAndActionbar();

        setupNavigationAndDrawer();

        loadMainFragment();
        Toast.makeText(MainActivity.this, "Po\u010detna", Toast.LENGTH_SHORT).show();
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
        LoginHandler.Login.forgetMe(getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE));
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    public void toGameActivity(View v){
        Activity currentParent = MainActivity.this;

        startActivity(new Intent(currentParent, GameActivity.class));
        currentParent.finish();
    }
    public void showAvailableFriends(View v){

        if (LoginHandler.Login.getLoggedPlayerAuth(this) == null) {
            Toast.makeText(MainActivity.this, getString(R.string.friendly_match_btn_alt), Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Dostupni prijatelji", Toast.LENGTH_SHORT).show();

            //        Todo
            //         - openDrawer with FriendList under Log out btn (inv icons next to each available-to-play friend)
            //        mDrawerLayout.openDrawer(GravityCompat.START);
            //              ...
            //              or
            //        Todo
            //         - show FriendList separately (either switch to Right Tab OR make a new Fragment on top of everything)
            //          ...
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
            loggedUserCreditsToolbar.setVisibility(View.VISIBLE);
            TextView toolbarLoggedUserPoints = loggedUserCreditsToolbar.findViewById(R.id.toolbarLoggedUserPoints);
            toolbarLoggedUserPoints.setText(String.valueOf(loggedPlayerFetched.getPointsCurrentRank()));
            TextView toolbarLoggedUserStars = loggedUserCreditsToolbar.findViewById(R.id.toolbarLoggedUserStars);
            toolbarLoggedUserStars.setText(String.valueOf(loggedPlayerFetched.getStars()));
            TextView toolbarLoggedUserTokens = loggedUserCreditsToolbar.findViewById(R.id.toolbarLoggedUserTokens);
            toolbarLoggedUserTokens.setText(String.valueOf(loggedPlayerFetched.getTokens()));

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
}