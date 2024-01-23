package com.ftn.slagalica;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GameConnectTwoFragment extends Fragment {

//    private String mParam1;
//    private String mParam2;
    private static final int SECOND = 1000;
    private static final int POINTS_PER_PAIR_CONNECTED = 2;
//    private static final int COLOR_CORRECT_MATCH = 0xFF03DAC5;
//    private static final int COLOR_WRONG_MATCH = 0XFFF44336;
    private boolean round2Ongoing = false;
    private boolean stealRound = false;
    private View selectedLeftField;
    public TextView timer;
    private CountDownTimer countDownTimer;
    private Timer rightColumnFieldRepaintTimer;
    private boolean everythingIsPaired;
    private GameActivity gameActivity;
//    private final FragmentActivity gameActivity = getActivity();

    private Map solutionViewPairs;

    public GameConnectTwoFragment() {
        // Required empty public constructor
    }

    public static GameConnectTwoFragment newInstance(GameActivity gameActivity) {
        GameConnectTwoFragment fragment = new GameConnectTwoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        fragment.gameActivity = gameActivity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestAndStoreGameData();

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_connect_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timer = getActivity().findViewById(R.id.textViewTimer);

        prepareUpcomingRoundUI();

        startTimerCountdown(30*SECOND);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        if (rightColumnFieldRepaintTimer != null)
            rightColumnFieldRepaintTimer.cancel();
    }

    private void showFieldsWithValues() {
//        *TODO
//          - render fields with real values from temporarily stored game data
//          * data was already received upon calling "requestAndStoreGameData()"

//      *NOTE: Old map values are overrun :
        solutionViewPairs = new HashMap<View, View>(){{
            put(getActivity().findViewById(R.id.leftFirstField), getActivity().findViewById(R.id.rightFourthField));
            put(getActivity().findViewById(R.id.leftSecondField), getActivity().findViewById(R.id.rightFifthField));
            put(getActivity().findViewById(R.id.leftThirdField), getActivity().findViewById(R.id.rightFirstField));
            put(getActivity().findViewById(R.id.leftFourthField), getActivity().findViewById(R.id.rightSecondField));
            put(getActivity().findViewById(R.id.leftFifthField), getActivity().findViewById(R.id.rightThirdField));
        }};

    }

    private void prepNextGame() {
//        showSolution();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, GameAssociationsFragment.newInstance(gameActivity)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 3*SECOND);
    }

    private void startTimerCountdown(int msTimeFrom){
//        showFieldValues();

        countDownTimer = new CountDownTimer(msTimeFrom + SECOND, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / SECOND));
            }
            @Override
            public void onFinish() {

                if( everythingIsPaired ) {

                    if(!round2Ongoing) {
                        requestAndStoreGameData();
//                      *TODO
//                        - on received response from DB server ->  wrap call below "prepareUpcomingRoundUI()"
                        prepareUpcomingRoundUI();

//                    START the 2nd Round, *** with Switching to the 2nd Player
                        this.start();
                        round2Ongoing = true;
                        stealRound = false;
                        Toast.makeText(getActivity(), "2. Igra\u010d je na redu", Toast.LENGTH_SHORT).show();

//                    return so that "prepNextGame()" does not get called
                        return;
                    }
                    prepNextGame();

//                NOT everything is paired yet (but time is up)
                } else {

//                all ROUNDS have passed after both Players' turns - time is up
                    if(round2Ongoing && stealRound) {
                        prepNextGame();
                        return;
                    }

//                else CONTINUE CURRENT Round, *** for OTHER Player (whichever he is)
                    this.start();
                    stealRound = true;
                }

            }
        }.start();
        Toast.makeText(getActivity(), "1. Igra\u010d je na redu", Toast.LENGTH_SHORT).show();
    }

    private void prepareUpcomingRoundUI() {
//      - RENDER previously loaded data into UI
        this.everythingIsPaired = false;
        showFieldsWithValues();

//      - RESET all fields - re-enable & re-equip with OnClickListeners

        Set<View> leftColumnFields = solutionViewPairs.keySet();
        for (View field : leftColumnFields) {
            field.setEnabled(true);
            field.setOnClickListener(this::onLeftFieldClick);
            ((TextView)field).setTextColor(getResources().getColor(R.color.white_smoked));
        }

        Collection<View> rightColumnFields = solutionViewPairs.values();
        for (View field : rightColumnFields) {
            field.setEnabled(true);
            field.setOnClickListener(this::onRightFieldClick);
            ((TextView)field).setTextColor(getResources().getColor(R.color.white_smoked));
        }
    }

    private boolean checkEverythingIsPaired() {
        for (View v : (Set<View>)solutionViewPairs.keySet()) {
            if(v.isEnabled()) return false;
        }
        everythingIsPaired = true;
        return true;
    }

    private void requestAndStoreGameData() {
        //      *TODO
        //          - use AsyncTask to request data from FireBase
        //          - temporarily store received data in a Collection of choice
        //          ...
    }

    public void onLeftFieldClick(View clickedLeftField) {
        selectedLeftField = clickedLeftField;

        Set<View> leftColumnFields = solutionViewPairs.keySet();

        for (View leftField : leftColumnFields) {
            if(leftField.isEnabled())
                ((TextView)leftField).setTextColor(getResources().getColor(R.color.white_smoked));
        }

        ((TextView)selectedLeftField).setTextColor(getResources().getColor(R.color.blue_light_3));
    }

    public void onRightFieldClick(View clickedRightField) {
        if (selectedLeftField != null) {

            if (solutionViewPairs.get(selectedLeftField).equals(clickedRightField)) {

                if (rightColumnFieldRepaintTimer != null)
                    rightColumnFieldRepaintTimer.cancel();

                selectedLeftField.setOnClickListener(null);
                ((TextView)selectedLeftField).setTextColor(getResources().getColor(R.color.teal_200));
                selectedLeftField.setEnabled(false);

                clickedRightField.setOnClickListener(null);
                ((TextView)clickedRightField).setTextColor(getResources().getColor(R.color.teal_200));
                clickedRightField.setEnabled(false);

//                TODO add POINTS_PER_PAIR_CONNECTED to Player instance currently on turn
                assignPoints(null);

//                TODO refresh points UI with current Player's points (instead of how it currently works below)
//                  ...

            }else {
                selectedLeftField.setOnClickListener(null);
                ((TextView)selectedLeftField).setTextColor(getResources().getColor(R.color.orange_dark));
                selectedLeftField.setEnabled(false);
                ((TextView)clickedRightField).setTextColor(getResources().getColor(R.color.orange_dark));

                rightColumnFieldRepaintTimer = new Timer();
                rightColumnFieldRepaintTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ((TextView)clickedRightField).setTextColor(getResources().getColor(R.color.white_smoked));
                    }
                }, SECOND/2);
            }

            selectedLeftField = null;

            if (checkEverythingIsPaired())
                countDownTimer.onFinish();
        }
    }

    //    *TODO
//          - add points to real instance of the receiving player;
//              * Total match points (for both players) are stored in DB after all the games end *
    private void assignPoints(Object player){

//        player is assigned amount of points based on Game rules

        GameActivity gameActivity = (GameActivity) getActivity();

        if (round2Ongoing == stealRound) {
            int playerCurrentTotal = gameActivity.getPlayer1PointsView() + POINTS_PER_PAIR_CONNECTED;
            //        GameActivity's points-display update
            gameActivity.setPlayer1PointsView(playerCurrentTotal);
        }
        else{
            int playerCurrentTotal = gameActivity.getPlayer2PointsView() + POINTS_PER_PAIR_CONNECTED;
            //        GameActivity's points-display update
            gameActivity.setPlayer2PointsView(playerCurrentTotal);
        }
    }

}