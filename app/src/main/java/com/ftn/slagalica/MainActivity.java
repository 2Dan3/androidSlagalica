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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ftn.slagalica.ui.login.LoginActivity;
import com.ftn.slagalica.util.LoginHandler;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Fragment tabbedMainFragment;
    private Fragment profileFragment;

//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.Theme_Slagalica_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        String loggedUsername = i.getStringExtra(USERNAME);
        String loggedEmail = i.getStringExtra(EMAIL);
        String loggedPic = i.getStringExtra("picture");

        setupSessionBasedUI(loggedUsername, loggedEmail, loggedPic);

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
            case R.id.action_settings:
                return true;
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
                    Toast.makeText(MainActivity.this, "Po\u010detna", Toast.LENGTH_SHORT).show();
                    showMainFragment();
                    return true;
                case R.id.nav_profile:
                    Toast.makeText(MainActivity.this, "Moj profil", Toast.LENGTH_SHORT).show();
                    showProfileFragment();
                    return true;
                case R.id.nav_logout:
                    Toast.makeText(MainActivity.this, "Odjavljeni ste", Toast.LENGTH_SHORT).show();
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

//    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupToolbarAndActionbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        replaceToolbarTextWithIcon(actionBar, toolbar);
    }
//    @RequiresApi(api = Build.VERSION_CODES.M)
    private void replaceToolbarTextWithIcon(ActionBar actionBar, Toolbar toolbar) {
        actionBar.setDisplayShowTitleEnabled(false);
//        toolbar.setBackgroundColor(getColor(R.color.blue_light_2));
        toolbar.setLogo(R.mipmap.ic_default_profile);
    }
    private void toLogin(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    private void logout(){
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

    private void setupSessionBasedUI(String loggedUsername, String loggedEmail, String loggedPic) {
        NavigationView navigationView = findViewById(R.id.navigation_view);

        View navHeader = navigationView.getHeaderView(0);
        ImageView profilePicView = navHeader.findViewById(R.id.drawer_profile_pic);

        Menu navMenu = navigationView.getMenu();
        MenuItem logItem;
        TextView usernameView = navHeader.findViewById(R.id.drawer_profile_username);
        TextView emailView = navHeader.findViewById(R.id.drawer_profile_email);

        if (loggedUsername == null || loggedUsername.equals("")) {
            logItem = navMenu.findItem(R.id.nav_logout);
            profilePicView.setImageResource(R.mipmap.ic_profile_icon_round);
            emailView.setText("Gost");
//            Todo test line :
//            navMenu.getItem(R.id.nav_profile).setVisible(false);
        }else{
            logItem = navMenu.findItem(R.id.nav_login);
//            Todo
//            profilePicView.setImageURI(Uri.parse(loggedPic));
            profilePicView.setImageResource(R.mipmap.ic_default_profile_round);
            emailView.setText(loggedEmail);
            usernameView.setText(loggedUsername);
        }
        logItem.setVisible(false);
    }
}