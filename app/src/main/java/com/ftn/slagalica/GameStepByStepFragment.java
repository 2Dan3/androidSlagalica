package com.ftn.slagalica;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class GameStepByStepFragment extends Fragment {
    private static final int SECOND = 1000;
    private static final int MAX_POINTS_ACHIEVABLE = 20;
    private static final int POINTS_LOST_PER_CLUE_SHOWN = 2;
    private static final int COLOR_RIGHT_MATCH = 0xFF03DAC5;

//    MOCK GAME VALUES - LATER WILL BE LOADED FROM DataBase
    Object[] gameValues = {"Ima veze sa Savom i Dunavom", "Svih devet slova ove reci je razlicito", "Ima veze sa zdravljem", "Moze se odnositi na zivot", "Ima svog agenta i svoju premiju", "Za vozila je obavezno", "Postoji i Kasko varijanta", "Osiguranje"};
    TextView[] fieldTextViews;

    public TextView timer;
    private int step = 1;
    private CountDownTimer countDownTimer;

    private final TextWatcher solutionWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            if( s.length() != 0 && solutionIsGuessed() ) {
                assignPoints(step, null);
                prepNextGame();
            }
        }
    };

    public GameStepByStepFragment() {
        // Required empty public constructor
    }

    public static GameStepByStepFragment newInstance(String param1, String param2) {
        GameStepByStepFragment fragment = new GameStepByStepFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//      TODO* - use AsyncTask to request data from FireBase
//              - store received data in "gameValues" variable


//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_step_by_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timer = getActivity().findViewById(R.id.textViewTimer);
        fieldTextViews = new TextView[]{getActivity().findViewById(R.id.clue1), getActivity().findViewById(R.id.clue2), getActivity().findViewById(R.id.clue3), getActivity().findViewById(R.id.clue4), getActivity().findViewById(R.id.clue5), getActivity().findViewById(R.id.clue6), getActivity().findViewById(R.id.clue7), getActivity().findViewById(R.id.stepFinalSolution)};
        startTimerCountdown(10*SECOND);
        setupSolutionListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    private void setupSolutionListener() {
        TextView solutionField = fieldTextViews[fieldTextViews.length-1];
        solutionField.addTextChangedListener(solutionWatcher);
    }

    private void prepNextGame() {
        fieldTextViews[fieldTextViews.length - 1].removeTextChangedListener(solutionWatcher);
        countDownTimer.cancel();
        showSolution();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, new GameMyNumberFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 3 * SECOND);
    }

    private void startTimerCountdown(int msTimeFrom){
        showStep(step++);

        countDownTimer = new CountDownTimer(msTimeFrom + SECOND, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / SECOND));
            }
            @Override
            public void onFinish() {

                if(step > 7) {
                    prepNextGame();
                }else {
                    showStep(step++);
                    this.start();
                }
            }
        }.start();
    }

    private boolean solutionIsGuessed() {
        return fieldTextViews[fieldTextViews.length-1].getText().toString().trim().equalsIgnoreCase( (gameValues[gameValues.length-1].toString()) );
    }

    private void showStep(int step){
        TextView clueView = fieldTextViews[step-1];
        clueView.setText(gameValues[step-1].toString());
    }
    private void showSolution(){
        TextView solutionField = fieldTextViews[fieldTextViews.length-1];
        solutionField.setText(gameValues[gameValues.length-1].toString());
        solutionField.setInputType(InputType.TYPE_NULL);
        solutionField.clearFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(solutionField.getWindowToken(), 0);
        solutionField.setBackgroundColor(COLOR_RIGHT_MATCH);
    }

//    *TODO
//      - add points to real instance of the receiving player;
//          * Total match points (for both players) are stored in DB after all the games end *
    private void assignPoints(int step, Object player){

//        player is assigned amount of points based on Game rules

        GameActivity gameActivity = (GameActivity) getActivity();

        int playersCurrentTotal = gameActivity.getPlayer1PointsView();

//        *NOTE: changes
//          STEP - 1 -> STEP - 2; fixed an offset when assigning points
        int playersNewTotal = playersCurrentTotal + (MAX_POINTS_ACHIEVABLE - ((step-2) * POINTS_LOST_PER_CLUE_SHOWN));

//        GameActivity's points-display update
        gameActivity.setPlayer1PointsView(playersNewTotal);
    }
}