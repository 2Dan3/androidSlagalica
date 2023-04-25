package com.ftn.slagalica;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameAssociationsFragment extends Fragment {

//    private static final String ARG_PARAM2 = "param2";

//    private String mParam2;

    public GameAssociationsFragment() {
        // Required empty public constructor
    }

    public static GameAssociationsFragment newInstance(String param1, String param2) {
        GameAssociationsFragment fragment = new GameAssociationsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_associations, container, false);
    }
    public void openField(View v) {
//        TODO
//         Calculations upon field opening
        revealWord(v);
    }
    private void revealWord(View v) {
//        TODO
//         change text color white_smoked -> dark_blue to reveal it
    }
}