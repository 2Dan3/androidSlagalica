package com.ftn.slagalica;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.slagalica.data.model.Player;
import com.ftn.slagalica.util.RankComparator;
import com.ftn.slagalica.util.RanksRecyclerViewAdapter;

import java.util.ArrayList;

public class RankingsFragment extends Fragment {
    private static final int SECOND = 1000;
    private static final int RANK_REFRESH_INTERVAL_MS = 2 * 60 * SECOND;
    private CountDownTimer cdTimerRefreshRanks;
    private TextView tvRanksUpdateCDTimer;
    private RanksRecyclerViewAdapter adapter;
    private ArrayList<Player> rankedPlayers;

    public RankingsFragment() { }

//    public static RankingsFragment newInstance(String param1, String param2) {
//        RankingsFragment fragment = new RankingsFragment();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rankedPlayers = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rankings, container, false);

        RecyclerView playersRecyclerView = v.findViewById(R.id.recycler_ranking);
        adapter = new RanksRecyclerViewAdapter( getAllRankedPlayers() );

        playersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        playersRecyclerView.setAdapter(adapter);

//        Todo request from Firebase:
//         Remaining Time (of 2 minutes total) until Rank lists are updated
//         + create Background Service here, to run the Timer with fetched remaining time

//        Todo consider switching TabPagerAdapter parent (not to call destroyView on every TabSwitch) :
//         FragmentStatePagerAdapter ->  FragmentPagerAdapter
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvRanksUpdateCDTimer = view.findViewById(R.id.tvRanksUpdateCDTimer);

//        Todo : extract Timer into Background service that starts in onCreateView & ends in onDestroyView
        cdTimerRefreshRanks = new CountDownTimer(RANK_REFRESH_INTERVAL_MS, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvRanksUpdateCDTimer.setText(String.format("%s : %02d", millisUntilFinished / (60 * SECOND), (millisUntilFinished / SECOND) % 60));
            }

            @Override
            public void onFinish() {
//                Todo : rank lists are updated ( Call db fromhere OR app will receive from Firebase's auto update feature)
                this.start();
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cdTimerRefreshRanks.cancel();
//        Todo : stop Background service that runs the Timer
    }

//    Todo : Firebase will auto update & this becomes "onReceive" type of method
//      OR      it should request explicitly from Database all ranked users
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Player> getAllRankedPlayers() {

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