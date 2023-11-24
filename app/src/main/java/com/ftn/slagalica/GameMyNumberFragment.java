package com.ftn.slagalica;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameMyNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameMyNumberFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int SECOND = 1000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameMyNumberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameMyNumberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameMyNumberFragment newInstance(String param1, String param2) {
        GameMyNumberFragment fragment = new GameMyNumberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        showMatchSummary();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_my_number, container, false);
    }

//    Todo : uncomment onDestroy when Timer is implemented like in other GameFragments
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        countDownTimer.cancel();
//    }

    private void showMatchSummary() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, new MatchSummaryFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 7*SECOND);
    }
}