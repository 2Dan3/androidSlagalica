package com.ftn.slagalica;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchFindingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchFindingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchFindingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchFindingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchFindingFragment newInstance(String param1, String param2) {
        MatchFindingFragment fragment = new MatchFindingFragment();
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

        CardView cardRandomMatch = getActivity().findViewById(R.id.cardViewRandomMatch);
        cardRandomMatch.setOnClickListener(view -> Toast.makeText(getActivity(), "Trazi se protivnik...", Toast.LENGTH_LONG).show());

        CardView cardFriendlyMatch = getActivity().findViewById(R.id.cardViewRandomMatch);
        cardFriendlyMatch.setOnClickListener(view -> Toast.makeText(getActivity(), "Odaberite prijatelja", Toast.LENGTH_LONG).show());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_finding, container, false);
    }
}