package com.ftn.slagalica;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.InputType;
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

    Object[] gameValues = {"Ima veze sa Savom i Dunavom", "Svih devet slova ove reci je razlicito", "Ima veze sa zdravljem", "Moze se odnositi na zivot", "Ima svog agenta i svoju premiju", "Za vozila je obavezno", "Postoji i Kasko varijanta", "Osiguranje"};
    TextView[] fieldTextViews;
//    TextView solutionView;
//    boolean solutionGuessed = false;

    public TextView timer;
    private int timeGlobal = 10;
    private int step = 1;
//    public Runnable updater;
//    public final Handler timerHandler = new Handler();

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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        timer = getActivity().findViewById(R.id.textViewTimer);
//        }
//        prepNextGame();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_step_by_step, container, false);
//        timer = getActivity().findViewById(R.id.textViewTimer);
//        fieldTextViews = new TextView[]{getActivity().findViewById(R.id.clue1), getActivity().findViewById(R.id.clue2), getActivity().findViewById(R.id.clue3), getActivity().findViewById(R.id.clue4), getActivity().findViewById(R.id.clue5), getActivity().findViewById(R.id.clue6), getActivity().findViewById(R.id.clue7)};
//
//        startUpdatingTimer(10);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        fieldTextViews = new TextView[]{getActivity().findViewById(R.id.clue1), getActivity().findViewById(R.id.clue2), getActivity().findViewById(R.id.clue3), getActivity().findViewById(R.id.clue4), getActivity().findViewById(R.id.clue5), getActivity().findViewById(R.id.clue6), getActivity().findViewById(R.id.clue7)};
//        solutionView = getActivity().findViewById(R.id.stepFinalSolution);
        super.onViewCreated(view, savedInstanceState);

        timer = getActivity().findViewById(R.id.textViewTimer);
        fieldTextViews = new TextView[]{getActivity().findViewById(R.id.clue1), getActivity().findViewById(R.id.clue2), getActivity().findViewById(R.id.clue3), getActivity().findViewById(R.id.clue4), getActivity().findViewById(R.id.clue5), getActivity().findViewById(R.id.clue6), getActivity().findViewById(R.id.clue7), getActivity().findViewById(R.id.stepFinalSolution)};
//        solutionView = getActivity().findViewById(R.id.stepFinalSolution);
        startUpdatingTimer(10);
    }

    private void prepNextGame() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, new GameMyNumberFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
    }

//    TODO check occasional point assignment miscalculation (likely is a step++ bug)
    private void startUpdatingTimer(int time){
        showStep(step++);

        Timer tt = new Timer();
        tt.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread( () -> {
                    if (timeGlobal < 0){
                        if(step > 7){
                            showSolution();
                            assignPoints(step, null);
//                            tt.cancel();
////                        return needed?
//                            return;
                        }
                        timeGlobal = 10;
                        showStep(step++);
                    }
                    if (solutionIsGuessed() ){
                        showSolution();
                        assignPoints(step, null);

                    }

                    timer.setText(String.valueOf(timeGlobal));
                    timeGlobal -= 1;
                });

                if (solutionIsGuessed()){

                    tt.cancel();
//                  TODO  test thread.sleep possible unwanted side-effects
                    try {
                        Thread.sleep(3*SECOND);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    prepNextGame();
//                        return needed?
                    return;
                }

            }
        }, SECOND, SECOND);

//
//        updater = new Runnable(){
//            @Override
//            public void run(){
//                int time2 = time;
//                if (time2 <= 0){
//                    if(solutionGuessed){
//                        timerHandler.removeCallbacks(updater);
////                        assignPoints(step);
//                        return;
//                    }
//                    time2 = 10;
////                    showStep(step++);
//                }
//                time2 -= 1;
//                timer.setText(time2);
//                timerHandler.postDelayed(updater,1000);
//            }
//        };
//        timerHandler.post(updater);
    }

    private boolean solutionIsGuessed() {
        return gameValues[gameValues.length-1].toString().equals(fieldTextViews[fieldTextViews.length-1].getText().toString().trim());
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // STOP executing timer update
//        timerHandler.removeCallbacks(updater);
//        // ALWAYS ADD TO onDestroy() TO PREVENT MEMORY LEAKS
//    }

    private void showStep(int step){
        TextView clueView = fieldTextViews[step-1];
        clueView.setText(gameValues[step-1].toString());
    }
    private void showSolution(){
        fieldTextViews[fieldTextViews.length-1].setText(gameValues[gameValues.length-1].toString());
        fieldTextViews[fieldTextViews.length-1].setInputType(InputType.TYPE_NULL);
        fieldTextViews[fieldTextViews.length-1].clearFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fieldTextViews[fieldTextViews.length-1].getWindowToken(), 0);
        ( (GameActivity) getActivity() ).changeFieldColor(fieldTextViews[fieldTextViews.length-1]);
    }
    private void assignPoints(int step, Object player){
        //        player is assigned amount of points based on Game rules

        //        ...

        //        GameActivity's points-display update
        GameActivity gameActivity = (GameActivity) getActivity();
        TextView playerPointsView = gameActivity.getPlayer1PointsView();

        int playersCurrentTotal = Integer.valueOf(String.valueOf(playerPointsView.getText()));
        int playersNewTotal = playersCurrentTotal + (MAX_POINTS_ACHIEVABLE - ((step-1) * POINTS_LOST_PER_CLUE_SHOWN));

        playerPointsView.setText(String.valueOf(playersNewTotal));
    }
}