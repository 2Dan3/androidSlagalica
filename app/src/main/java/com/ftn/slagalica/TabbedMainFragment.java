package com.ftn.slagalica;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.slagalica.data.model.AuthBearer;
import com.ftn.slagalica.data.model.Player;
import com.ftn.slagalica.util.LoginHandler;
import com.ftn.slagalica.util.RankComparator;
import com.ftn.slagalica.util.RanksRecyclerViewAdapter;
import com.ftn.slagalica.util.SearchPlayerRecyclerViewAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TabbedMainFragment extends Fragment {

    private DesignDemoPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static final int[] icons = {R.drawable.trophy, R.drawable.play, R.drawable.players};
    private static final String[] titles = {"Plasmani", "Igraj", "Prijatelji"};

    private static ArrayList<Player> friends = new ArrayList();
    private static ArrayList<Player> rankedPlayers = new ArrayList();
    private AuthBearer loggedUser;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;

    public TabbedMainFragment() { }

    // TODO: Rename and change types and number of parameters
    public static TabbedMainFragment newInstance(String param1, String param2) {
        TabbedMainFragment fragment = new TabbedMainFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        this.loggedUser = LoginHandler.Login.getLoggedPlayerAuth(getActivity());
//        friends.addAll( requestFriendsList() );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabbed_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new DesignDemoPagerAdapter(TabbedMainFragment.this);
        viewPager = getActivity().findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout = getActivity().findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);

        //        Tab Icons setup
        if (loggedUser == null) {
            tabLayout.getTabAt(0).setIcon(icons[1]);
            setWiderMainTabIndicator(0);
        }
        else {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(icons[i]);
            }
//      * Selects default "PLAY" Tab upon activity creation
            tabLayout.getTabAt(1).select();
            setWiderMainTabIndicator(1);
        }
    }

    private void setWiderMainTabIndicator(int tabPosition){
//              * Middle Tab "PLAY" is main (wider bottom scroll indicator than other tabs)
        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tabPosition));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 1.23f;
        layout.setLayoutParams(layoutParams);
    }

    public static class DesignDemoFragment extends Fragment {
        private static final String TAB_POSITION = "tab_position";
        private SearchPlayerRecyclerViewAdapter adapter;
        private RanksRecyclerViewAdapter ranksAdapter;
        private CountDownTimer cdTimerRefreshRanks;

        public DesignDemoFragment() {

        }

        public static DesignDemoFragment newInstance(int tabPosition) {
            DesignDemoFragment fragment = new DesignDemoFragment();
            Bundle args = new Bundle();
            args.putInt(TAB_POSITION, tabPosition);
            fragment.setArguments(args);
            return fragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int tabPosition = args.getInt(TAB_POSITION);
            View v = null;

//             ubacivanje fragmenata u Tabove resava
//             vizuelni Bug sa scroll-indicator pri horizontalnom skrolovanju prstom
//            Todo* evaluate logged user & lock side Tabs ("locks" for icons) & hide profileNav in drawer
            if (tabPosition==0) {
                v = inflater.inflate(R.layout.fragment_rankings, container, false);

                RecyclerView playersRecyclerView = v.findViewById(R.id.recycler_ranking);
                playersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                ranksAdapter = new RanksRecyclerViewAdapter( getAllRankedPlayers() );
                playersRecyclerView.setAdapter(ranksAdapter);

//                TextView tvRanksUpdateCDTimer = getActivity().findViewById(R.id.tvRanksUpdateCDTimer);
//
//                cdTimerRefreshRanks = new CountDownTimer(2*60*1000 + 1000, 1000) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        tvRanksUpdateCDTimer.setText(String.format("%s : %s", millisUntilFinished / (60000), millisUntilFinished / 1000));
//                    }
//
//                    @Override
//                    public void onFinish() {
////                Todo : rank lists are updated ( Call db fromhere OR app will receive from Firebase's auto update feature)
//                        this.start();
//                    }
//                }.start();
            }
            else if (tabPosition==1) {
                v = inflater.inflate(R.layout.fragment_match_finding, container, false);
            }
            else if (tabPosition==2) {
                v = inflater.inflate(R.layout.fragment_players, container, false);

                RecyclerView playersRecyclerView = v.findViewById(R.id.recycler_friends);


                playersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

////        Nothing's been searched yet, so Friend List is displayed as a default
////        Toast.makeText(getContext(), friends.get(9).toString(), Toast.LENGTH_LONG).show();
//
////                Toast.makeText(getContext(), "Friends loaded", Toast.LENGTH_LONG).show();

                adapter = new SearchPlayerRecyclerViewAdapter( getActivity(), requestFriendsList() );

                adapter.setClickListener(this::onItemClick);

                playersRecyclerView.setAdapter(adapter);
            }

            return v;
        }

//        @Override
//        public void onDestroyView() {
//            super.onDestroyView();
//            cdTimerRefreshRanks.cancel();
//        }

        private void onItemClick(View view) {
            SearchPlayerRecyclerViewAdapter.ViewHolder viewHolder = (SearchPlayerRecyclerViewAdapter.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            Player selectedFriend = adapter.getPlayer(position);
//        TEMPORARILY Commented :
//        Player selectedFriend = searchResultPlayers.get(position);
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
    }

    static class DesignDemoPagerAdapter extends FragmentStatePagerAdapter {
        static TabbedMainFragment fragment;

        public DesignDemoPagerAdapter(TabbedMainFragment frag) {
            super(frag.getActivity().getSupportFragmentManager());
            fragment = frag;
        }

        @Override
        public Fragment getItem(int position) {
            return fragment.loggedUser == null ? DesignDemoFragment.newInstance(1) : DesignDemoFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return fragment.loggedUser == null ? 1 : 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragment.loggedUser == null ? titles[1] : titles[position];
//            return null;
        }
    }

    public static ArrayList<Player> requestFriendsList() {

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
    public static ArrayList<Player> removeFriend(int playerListPosition) {
        friends.remove(playerListPosition);
        return friends;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Player> getAllRankedPlayers() {

//        MOCK LIST of ranked players
        if (rankedPlayers.isEmpty()) {

            rankedPlayers.add(new Player("PuzzlePlayer123", "puzzler@gmail.com", "pass123", "http://imgur.com/", 320, 6, 2));
            rankedPlayers.add(new Player("SlagalicaSlayer", "slagalac@yahoo.com", "pass123", "http://imgur.com/", 220, 4, 3));
            rankedPlayers.add(new Player("Gamer697", "gamerr@gmail.com", "pass123", "http://imgur.com/", 440, 10, 3));
            rankedPlayers.add(new Player("Hotstreak", "streaker@outlook.com", "pass123", "http://imgur.com/", 550, 12, 3));
            rankedPlayers.add(new Player("MrSpeedrun101", "theycallmespeed@gmail.com", "pass123", "http://imgur.com/", 100, 10, 4));
            rankedPlayers.add(new Player("Alexiiss_boss", "alex97@gmail.com", "pass123", "http://imgur.com/", 300, 12, 4));
            rankedPlayers.add(new Player("Hotshot021", "shotty@yahoo.com", "pass123", "http://imgur.com/", 208, 3, 5));
            rankedPlayers.add(new Player("JigsawMaker00", "hiimjiggy@outlook.com", "pass123", "http://imgur.com/", 164, 7, 5));
            rankedPlayers.add(new Player("ThePuzzlePr0", "puzzler@yahoo.com", "pass123", "http://imgur.com/", 324, 8, 3));
            rankedPlayers.add(new Player("5uper3g0", "mr530@gmail.com", "pass123", "http://imgur.com/", 282, 2, 4));
            //        Todo DB request rankedPlayers JSON list
            //                  ...
        }

        rankedPlayers.sort(new RankComparator());

        return rankedPlayers;
    }
}