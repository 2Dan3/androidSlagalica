package com.ftn.slagalica;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GameAssociationsFragment extends Fragment {
    private static final int SECOND = 1000;
    private static final int COLOR_CORRECT_MATCH = 0xFF03DAC5;
    private Map<TextView, String> clueViewPairs;
    private Map<TextView, String> solutionViewPairs;
    private String[] clueWords;
    private String[] solutionWords;
    public TextView timer;
    private CountDownTimer countDownTimer;
    private boolean round2Ongoing = false;

    private final TextWatcher solutionWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            if (s.length() != 0) {

                for (TextView solutionField : solutionViewPairs.keySet()) {
                    if (solutionField.getText().hashCode() == s.hashCode() && solutionIsGuessed(solutionField)) {
                        removeTextListeners(solutionField);
                        assignPoints(null);
                        showSolution(solutionField);
//                     *Todo
//                        openGuessedColumn(solutionField);
                    }
                }
            }
        }
    };

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
        return inflater.inflate(R.layout.fragment_game_associations, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timer = getActivity().findViewById(R.id.textViewTimer);

        startTimerCountdown(15*SECOND);

        requestAndStoreGameData();
        prepareUpcomingRoundUI();
    }

    private void startTimerCountdown(int msTimeFrom) {
        countDownTimer = new CountDownTimer(msTimeFrom, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText( String.valueOf(millisUntilFinished / SECOND) );
//                Todo
//                if (finalSolutionIsGuessed()) {
//                    onFinish();
//                }
            }

            @Override
            public void onFinish() {
                if (round2Ongoing) {
                    prepNextGame();
                    return;
                }
                Toast.makeText(getActivity(), "2. Igra\u010d zapo\u010dinje", Toast.LENGTH_SHORT).show();
                requestAndStoreGameData();
                prepareUpcomingRoundUI();
                this.start();
                round2Ongoing = true;
            }
        }.start();
        Toast.makeText(getActivity(), "1. Igra\u010d zapo\u010dinje", Toast.LENGTH_SHORT).show();
    }

    private void prepareUpcomingRoundUI() {
//      - RENDER previously loaded data into UI
        showFieldsWithValues();

//      - RESET all fields - re-enable & re-equip with OnClickListeners

        for ( TextView field : clueViewPairs.keySet() ) {
            field.setOnClickListener(this::onClueFieldClick);
            field.setText("");
//            field.setBackgroundColor(getResources().getColor(R.color.white_smoked));
        }

        for ( TextView field : solutionViewPairs.keySet() ) {
            field.removeTextChangedListener(solutionWatcher);
            field.addTextChangedListener(solutionWatcher);
            field.setText("");
            field.setBackgroundColor(getResources().getColor(R.color.white_smoked));
//            field.setBackgroundColor(getResources().getColor(R.color.white_smoked));
        }
    }

    private void onClueFieldClick(View view) {
        TextView field = (TextView) view;
        field.setText(clueViewPairs.get(field));

//        TODO
//         - give turn to other player
    }

    private void showFieldsWithValues() {
//          * data was already received upon calling "requestAndStoreGameData()"

//      *NOTE: Old values are overrun :
        clueViewPairs = new HashMap<TextView, String>(){{
            put(getActivity().findViewById(R.id.asocCol1Field1), clueWords[0]);
            put(getActivity().findViewById(R.id.asocCol1Field2), clueWords[1]);
            put(getActivity().findViewById(R.id.asocCol1Field3), clueWords[2]);
            put(getActivity().findViewById(R.id.asocCol1Field4), clueWords[3]);

            put(getActivity().findViewById(R.id.asocCol2Field1), clueWords[4]);
            put(getActivity().findViewById(R.id.asocCol2Field2), clueWords[5]);
            put(getActivity().findViewById(R.id.asocCol2Field3), clueWords[6]);
            put(getActivity().findViewById(R.id.asocCol2Field4), clueWords[7]);

            put(getActivity().findViewById(R.id.asocCol3Field1), clueWords[8]);
            put(getActivity().findViewById(R.id.asocCol3Field2), clueWords[9]);
            put(getActivity().findViewById(R.id.asocCol3Field3), clueWords[10]);
            put(getActivity().findViewById(R.id.asocCol3Field4), clueWords[11]);

            put(getActivity().findViewById(R.id.asocCol4Field1), clueWords[12]);
            put(getActivity().findViewById(R.id.asocCol4Field2), clueWords[13]);
            put(getActivity().findViewById(R.id.asocCol4Field3), clueWords[14]);
            put(getActivity().findViewById(R.id.asocCol4Field4), clueWords[15]);
        }};

        //      *NOTE: Old values are overrun :
        solutionViewPairs = new HashMap<TextView, String>(){{
            put(getActivity().findViewById(R.id.asocFirstSolution), solutionWords[0]);
            put(getActivity().findViewById(R.id.asocSecondSolution), solutionWords[1]);
            put(getActivity().findViewById(R.id.asocThirdSolution), solutionWords[2]);
            put(getActivity().findViewById(R.id.asocFourthSolution), solutionWords[3]);
            put(getActivity().findViewById(R.id.asocFinalSolution), solutionWords[4]);
        }};

    }

    private void prepNextGame() {
        removeTextListeners( solutionViewPairs.keySet().toArray(new TextView[]{}) );
        countDownTimer.cancel();
//        show final solution before ending game
//        showSolution();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, new GameJumperFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 3*SECOND);
    }

    private void requestAndStoreGameData() {
        //      *TODO
        //          - use AsyncTask to request data from FireBase
        //          - store received data in "clueWords" & "solutionWords"
        //          ...

//        MOCK DATA LIST
        clueWords = new String[]
        {
                "Promene", "Uredjaj", "Servis", "Planina",
                "Mario", "Lepak", "Liga", "Nova",
                "Radost", "Unija", "Komisija", "Parlament",
                "Juzni pol", "Pingvin", "Erebus", "Vinson"
        };
//        MOCK DATA LIST
        solutionWords = new String[]
        {
                "Klima", "Super", "Evropa", "Antarktik", "Kontinent"
        };
    }


    //    *TODO
//          - add points to real instance of the receiving player;
//              * Total match points (for both players) are stored in DB after all the games end *
    private void assignPoints(Object player){

//        player is assigned amount of points based on Game rules

        GameActivity gameActivity = (GameActivity) getActivity();

//        if (round2Ongoing ?) {
//            int playerCurrentTotal = gameActivity.getPlayer1PointsView() + POINTS_PER_PAIR_CONNECTED;
//            //        GameActivity's points-display update
//            gameActivity.setPlayer1PointsView(playerCurrentTotal);
//        }
//        else{
//            int playerCurrentTotal = gameActivity.getPlayer2PointsView() + POINTS_PER_PAIR_CONNECTED;
//            //        GameActivity's points-display update
//            gameActivity.setPlayer2PointsView(playerCurrentTotal);
//        }
    }

    private void showSolution(TextView field){
        field.setText(solutionViewPairs.get(field));
        field.setInputType(InputType.TYPE_NULL);
        field.clearFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(field.getWindowToken(), 0);
        field.setBackgroundColor(COLOR_CORRECT_MATCH);
    }

    private boolean solutionIsGuessed(TextView solutionField) {
        return solutionField.getText().toString().trim().equalsIgnoreCase(solutionViewPairs.get(solutionField));
    }

//    private boolean finalSolutionIsGuessed() {
////        Todo
//        return false;
//    }

    private void removeTextListeners(TextView ...solutionViews) {
        for ( TextView solutionField : solutionViews ) {
            solutionField.removeTextChangedListener(solutionWatcher);
        }
    }
}