package com.ftn.slagalica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ftn.slagalica.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Fragment tabbedMainFragment;
    private Fragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        toolbar.setLogo(R.mipmap.ic_default_profile);
    }
    private void toLogin(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    private void logout(){
//        TODO brisanje tokena korisnika
//         iz sesije pre redirekcije na login
//          Reload Pocetne sa specificnostima za Neprijavljenog korisnika
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
}