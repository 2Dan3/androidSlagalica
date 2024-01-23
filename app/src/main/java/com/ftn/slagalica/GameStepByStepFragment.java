package com.ftn.slagalica;

import static com.ftn.slagalica.util.AuthHandler.FIREBASE_URL;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.slagalica.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameStepByStepFragment extends Fragment {
    private static final int SECOND = 1000;
    private static final int MAX_POINTS_ACHIEVABLE = 20;
    private static final int POINTS_LOST_PER_CLUE_SHOWN = 2;
    private static final int COLOR_RIGHT_MATCH = 0xFF03DAC5;
    ArrayList<String> gameValuesRound2;
    ArrayList<String> gameValuesRound1;
    //    MOCK GAME VALUES - LATER WILL BE LOADED FROM DataBase
    //    = {"Ima veze sa Savom i Dunavom", "Svih devet slova ove reci je razlicito", "Ima veze sa zdravljem", "Moze se odnositi na zivot", "Ima svog agenta i svoju premiju", "Za vozila je obavezno", "Postoji i Kasko varijanta", "Osiguranje"};
    TextView[] fieldTextViews;

    public TextView timer;
    private Timer round2StartTimer = new Timer();
    private int step = 1;
    private boolean round2 = false;
    private CountDownTimer countDownTimer;
    private GameActivity gameActivity;
    private DatabaseReference answerRef;

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
                submitSolution();
                assignPoints(step, null);
                step = 8;
                countDownTimer.onFinish();
            }
        }
    };

    private void submitSolution() {
        String solution = fieldTextViews[fieldTextViews.length-1].getText().toString().trim();
        answerRef.setValue(solution);
    }

    public GameStepByStepFragment() {
        // Required empty public constructor
    }

    public static GameStepByStepFragment newInstance(GameActivity gameActivity) {
        GameStepByStepFragment fragment = new GameStepByStepFragment();
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
        DatabaseReference gameDataRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("stepbystep").child("-NoHLDSqsU6Njxmi5laB");

        answerRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("active_matches").child(gameActivity.getGameID()).child("answer");

        answerRef.removeValue().addOnCompleteListener(gameActivity,
                task -> {
                    if (task.isSuccessful()) {
                        setupSolutionSubmitDBListener();
                        gameDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    gameValuesRound1 = (ArrayList<String>) snapshot.child("round1").getValue();
                                    gameValuesRound2 = (ArrayList<String>) snapshot.child("round2").getValue();
//                    Toast.makeText(getActivity(), gameValues.toString(), Toast.LENGTH_SHORT).show();
                                    startTimerCountdown(10*SECOND);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    }
                });

//      TODO* - use AsyncTask to request data from FireBase
//              - store received data in "gameValues" variable

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
    }

    private void setupSolutionSubmitDBListener() {
        answerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue(String.class) != null && !"".equals(snapshot.getValue(String.class))) {
                    String solutionFetched = snapshot.getValue(String.class);
                    Toast.makeText(gameActivity, solutionFetched, Toast.LENGTH_SHORT).show();
                    fieldTextViews[fieldTextViews.length-1].setText(solutionFetched);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
//        startTimerCountdown(10*SECOND);
        disableSolutionInteraction();

        setupSolutionFieldListener();
    }

    private void disableSolutionInteraction() {
        fieldTextViews[fieldTextViews.length-1].setEnabled(false);
    }
    private void enableSolutionInteraction() {
        fieldTextViews[fieldTextViews.length-1].setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        round2StartTimer.cancel();
    }

    private void setupSolutionFieldListener() {
        TextView solutionField = fieldTextViews[fieldTextViews.length-1];
        solutionField.addTextChangedListener(solutionWatcher);
    }

    private void prepNextGame() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, GameMyNumberFragment.newInstance(gameActivity)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 2 * SECOND);
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
                    fieldTextViews[fieldTextViews.length - 1].removeTextChangedListener(solutionWatcher);
                    showSolution();

                    if (round2) {
                        prepNextGame();
                    }else{
                        round2StartTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                step = 1;
                                resetFields();
                                setupSolutionFieldListener();
                                round2 = true;
                                gameValuesRound1 = gameValuesRound2;

                                if (gameActivity.switchAndGetPlayerOnTurn().equals(gameActivity.getLoggedPlayer()))
                                    gameActivity.runOnUiThread(() -> enableSolutionInteraction());
                                else
                                    gameActivity.runOnUiThread(() -> disableSolutionInteraction());

                                countDownTimer.start();
                                showStep(step++);
                            }
                        }, SECOND*3);
                    }
                }else {
                    showStep(step++);
                    this.start();
                }
            }
        };
        gameActivity.setPlayerOnTurn(gameActivity.getFirstRoundPlayer());
        if (gameActivity.getFirstRoundPlayer().equals(gameActivity.getLoggedPlayer())) {
            gameActivity.runOnUiThread(() -> enableSolutionInteraction());
        }
        countDownTimer.start();
    }

    private boolean solutionIsGuessed() {
        return fieldTextViews[fieldTextViews.length-1].getText().toString().trim().equalsIgnoreCase( (gameValuesRound1.get(gameValuesRound1.size()-1).toString()) );
    }

    private void showStep(int step){
        TextView clueView = fieldTextViews[step-1];
        clueView.setText(gameValuesRound1.get(step-1).toString());
    }
    private void showSolution(){
        TextView solutionField = fieldTextViews[fieldTextViews.length-1];
        solutionField.setText(gameValuesRound1.get(gameValuesRound1.size()-1).toString());
//        solutionField.setInputType(InputType.TYPE_NULL);
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

//        *NOTE: changes
//          STEP - 1 -> STEP - 2; fixed an offset when assigning points
        int points = MAX_POINTS_ACHIEVABLE - ((step-2) * POINTS_LOST_PER_CLUE_SHOWN);

        User playerOnTurn = gameActivity.getPlayerOnTurn();
        playerOnTurn.setPointsStepByStep(playerOnTurn.getPointsStepByStep() + points);
        gameActivity.addOverallPointsForCurrentMatchToPlayer(points, playerOnTurn);
//        GameActivity's points-display update
        if (playerOnTurn.equals(gameActivity.getLoggedPlayer()))
            gameActivity.setPlayer1PointsView(playerOnTurn.getPointsCurrentMatch());
        else
            gameActivity.setPlayer2PointsView(playerOnTurn.getPointsCurrentMatch());
    }

    private void resetFields(){
        for (TextView tv : fieldTextViews) {
            tv.setText("");
        }
        TextView solutionField = fieldTextViews[fieldTextViews.length-1];
//        solutionField.setInputType(InputType.TYPE_CLASS_TEXT);
        solutionField.setBackgroundColor(getResources().getColor(R.color.white_smoked));
    }
}