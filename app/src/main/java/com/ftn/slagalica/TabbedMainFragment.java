package com.ftn.slagalica;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ftn.slagalica.data.model.Player;
import com.ftn.slagalica.util.SearchPlayerRecyclerViewAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TabbedMainFragment extends Fragment {

    private DesignDemoPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static final int[] icons = {R.drawable.trophy, R.drawable.play, R.drawable.players};
    private static final String[] titles = {"Plasmani", "Igraj", "Igra\u010di"};

    private ArrayList<Player> friends = new ArrayList();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TabbedMainFragment() { }

    // TODO: Rename and change types and number of parameters
    public static TabbedMainFragment newInstance(String param1, String param2) {
        TabbedMainFragment fragment = new TabbedMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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

        adapter = new DesignDemoPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager = getActivity().findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout = getActivity().findViewById(R.id.tablayout);

        tabLayout.setupWithViewPager(viewPager);

        //        Tab Icons setup
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
        //      * Selects default "PLAY" Tab upon activity creation
        tabLayout.getTabAt(1).select();
        //      * Middle Tab "PLAY" is main (wider bottom scroll indicator than other tabs)
        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(1));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 1.4f;
        layout.setLayoutParams(layoutParams);
    }

    public static class DesignDemoFragment extends Fragment {
        private static final String TAB_POSITION = "tab_position";

        public DesignDemoFragment() {

        }

        public static DesignDemoFragment newInstance(int tabPosition) {
            DesignDemoFragment fragment = new DesignDemoFragment();
            Bundle args = new Bundle();
            args.putInt(TAB_POSITION, tabPosition);
            fragment.setArguments(args);
            return fragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            int tabPosition = args.getInt(TAB_POSITION);
            View v = null;
//            ArrayList<String> items = new ArrayList<String>();


//            *TODO: ubacivanje fragmenata u Tabove
//             TODO to ce resiti vizuelni Bug sa scroll-indicator pri horizontalnom skrolovanju prstom
            if (tabPosition==0) {
//                v = inflater.inflate(R.layout.fragment_rankings, container, false);
            }
            else if (tabPosition==1) {
                v = inflater.inflate(R.layout.fragment_match_finding, container, false);
            }
            else if (tabPosition==2) {
                v = inflater.inflate(R.layout.fragment_players, container, false);

                RecyclerView playersRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_players_search);


                playersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        Nothing's been searched yet, so Friend List is displayed as a default
//        Toast.makeText(getContext(), friends.get(9).toString(), Toast.LENGTH_LONG).show();

                SearchPlayerRecyclerViewAdapter srcPlayerAdapter = new SearchPlayerRecyclerViewAdapter( getActivity(), requestFriendsList() );
//                Todo uncomment & fix this/static problem :
//                srcPlayerAdapter.setClickListener(this);

                playersRecyclerView.setAdapter(srcPlayerAdapter);
            }

            return v;
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
    }

    static class DesignDemoPagerAdapter extends FragmentStatePagerAdapter {

        public DesignDemoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DesignDemoFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
//            return null;
        }
    }

//    private ArrayList<Player> requestFriendsList() {
//
////        MOCK LIST of FRIENDS
//        ArrayList<Player> resultList = new ArrayList<>();
//        resultList.add(new Player("PuzzlePlayer123", "puzzler@gmail.com", "pass123", "http://imgur.com/", 320, 6));
//        resultList.add(new Player("SlagalicaSlayer", "slagalac@yahoo.com", "pass123", "http://imgur.com/", 220, 4));
//        resultList.add(new Player("Gamer697", "gamerr@gmail.com", "pass123", "http://imgur.com/", 440, 10));
//        resultList.add(new Player("Hotstreak", "streaker@outlook.com", "pass123", "http://imgur.com/", 550, 12));
//        resultList.add(new Player("MrSpeedrun101", "theycallmespeed@gmail.com", "pass123", "http://imgur.com/", 100, 10));
//        resultList.add(new Player("Alexiiss_boss", "alex97@gmail.com", "pass123", "http://imgur.com/", 300, 12));
//        resultList.add(new Player("Hotshot021", "shotty@yahoo.com", "pass123", "http://imgur.com/", 208, 3));
//        resultList.add(new Player("JigsawMaker00", "hiimjiggy@outlook.com", "pass123", "http://imgur.com/", 164, 7));
//        resultList.add(new Player("ThePuzzlePr0", "puzzler@yahoo.com", "pass123", "http://imgur.com/", 324, 8));
//        resultList.add(new Player("5uper3g0", "mr530@gmail.com", "pass123", "http://imgur.com/", 282, 2));
//        //        Todo DB request Friends list for logged player
//        //                  ...
//
//        return resultList;
//    }
}