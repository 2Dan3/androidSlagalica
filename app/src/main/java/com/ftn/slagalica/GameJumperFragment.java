package com.ftn.slagalica;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GameJumperFragment extends Fragment {

    private static final int SECOND = 1000;
    private ImageView[][] answerAttemptSets;
    private TextView timer;
    private CountDownTimer countDownTimer;
    private int attemptNum = 1;
    private GameActivity gameActivity;

    public GameJumperFragment() { }

    public static GameJumperFragment newInstance(GameActivity gameActivity) {
        GameJumperFragment fragment = new GameJumperFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        fragment.gameActivity = gameActivity;
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
        return inflater.inflate(R.layout.fragment_game_jumper, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    private void prepNextGame() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, GameStepByStepFragment.newInstance(gameActivity)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 2*SECOND);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.imageButton19).setOnClickListener(this::onClick);
        view.findViewById(R.id.imageButton20).setOnClickListener(this::onClick);
        view.findViewById(R.id.imageButton21).setOnClickListener(this::onClick);
        view.findViewById(R.id.imageButton22).setOnClickListener(this::onClick);
        view.findViewById(R.id.imageButton23).setOnClickListener(this::onClick);
        view.findViewById(R.id.imageButton24).setOnClickListener(this::onClick);

        timer = getActivity().findViewById(R.id.textViewTimer);

        startTimerCountdown(10*SECOND);

        getAnswerAttemptFields(view);

        requestAndStoreGameData();
        prepareUpcomingRoundUI();
    }

    private void startTimerCountdown(int msTimeFrom) {
        countDownTimer = new CountDownTimer(msTimeFrom, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText( String.valueOf(millisUntilFinished / SECOND) );
//                Todo ?
//                if (finalSolutionIsGuessed()) {
//                    onFinish();
//                }
            }

            @Override
            public void onFinish() {
//                * Temporary :

//                if (round2Ongoing) {
//                if (attemptNum == 6) {
                if (attemptNum == 1) {
                    prepNextGame();
                    return;
                }
//                Toast.makeText(getActivity(), "2. Igra\u010d zapo\u010dinje", Toast.LENGTH_SHORT).show();
                requestAndStoreGameData();
                prepareUpcomingRoundUI();
                attemptNum += 1;
                this.start();
//                round2Ongoing = true;
            }
        }.start();
//        Toast.makeText(getActivity(), "1. Igra\u010d zapo\u010dinje", Toast.LENGTH_SHORT).show();
    }

    private void prepareUpcomingRoundUI() {
//      - RENDER previously loaded data into UI
//        showFieldsWithValues();

//      - RESET all fields - re-enable & re-equip with OnClickListeners

//        for ( TextView field : clueViewPairs.keySet() ) {
//            field.setOnClickListener(this::onClueFieldClick);
//            field.setText("");
////            field.setBackgroundColor(getResources().getColor(R.color.white_smoked));
//        }
//
//        for ( TextView field : solutionViewPairs.keySet() ) {
//            field.removeTextChangedListener(solutionWatcher);
//            field.addTextChangedListener(solutionWatcher);
//            field.setText("");
//            field.setBackgroundColor(getResources().getColor(R.color.white_smoked));
////            field.setBackgroundColor(getResources().getColor(R.color.white_smoked));
//        }
    }

    private void onClick(View v){
        String selectedName;

        switch (v.getId()) {
            case R.id.imageButton19:
                selectedName = "Herc";
                break;
            case R.id.imageButton20:
                selectedName = "Karo";
                break;
            case R.id.imageButton21:
                selectedName = "Tref";
                break;
            case R.id.imageButton22:
                selectedName = "Pik";
                break;
            case R.id.imageButton23:
                selectedName = "Sko\u010dko";
                break;
            case R.id.imageButton24:
                selectedName = "Zvezda";
                break;
            default:
                selectedName = "n/a";
                Toast.makeText(getContext(), selectedName, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void requestAndStoreGameData() {
//        TODO req data from DB or generate randomly from backend here
    }

    private void getAnswerAttemptFields(View view) {
        answerAttemptSets = new ImageView[6][4];

        answerAttemptSets[0] = new ImageView[]{ view.findViewById(R.id.imageView32), view.findViewById(R.id.imageView31), view.findViewById(R.id.imageView33), view.findViewById(R.id.imageView34) };
        answerAttemptSets[1] = new ImageView[]{ view.findViewById(R.id.imageView44), view.findViewById(R.id.imageView43), view.findViewById(R.id.imageView42), view.findViewById(R.id.imageView41) };
        answerAttemptSets[2] = new ImageView[]{ view.findViewById(R.id.imageView64), view.findViewById(R.id.imageView63), view.findViewById(R.id.imageView62), view.findViewById(R.id.imageView61) };
        answerAttemptSets[3] = new ImageView[]{ view.findViewById(R.id.imageView111), view.findViewById(R.id.imageView112), view.findViewById(R.id.imageView113), view.findViewById(R.id.imageView114) };
        answerAttemptSets[4] = new ImageView[]{ view.findViewById(R.id.imageView121), view.findViewById(R.id.imageView122), view.findViewById(R.id.imageView123), view.findViewById(R.id.imageView124) };
        answerAttemptSets[5] = new ImageView[]{ view.findViewById(R.id.imageView131), view.findViewById(R.id.imageView132), view.findViewById(R.id.imageView133), view.findViewById(R.id.imageView134) };
    }
}