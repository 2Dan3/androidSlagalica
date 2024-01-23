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
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class GameMyNumberFragment extends Fragment {

    private static final int SECOND = 1000;
    private CountDownTimer countDownTimer;
    private TextView timer;
    private TextView tvSolutionAttempt;
    private TextView tvGoalNumber;
    private StringBuilder solutionAttempt;
    private Button[] buttons;
    private Button btnDeleteLastInput;
    private Button btnCheckSolution;
    private boolean lastClickedWasOperand;
    private boolean lastClickedWasNum;
    private GameActivity gameActivity;

    public static GameMyNumberFragment newInstance(GameActivity gameActivity) {
        GameMyNumberFragment fragment = new GameMyNumberFragment();
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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        solutionAttempt = new StringBuilder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_my_number, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvGoalNumber = view.findViewById(R.id.textView14);
        tvSolutionAttempt = view.findViewById(R.id.myNumberSolutionAttempt);
        btnCheckSolution = view.findViewById(R.id.button14);
        btnCheckSolution.setOnClickListener(this::onConfirmSolutionClick);

        btnDeleteLastInput = view.findViewById(R.id.btnDeleteLastInput);

        timer = getActivity().findViewById(R.id.textViewTimer);

        startTimerCountdown(20*SECOND);

        buttons = new Button[]{
                view.findViewById(R.id.button2),
                view.findViewById(R.id.button3),
                view.findViewById(R.id.button4),
                view.findViewById(R.id.button5),
                view.findViewById(R.id.button6),
                view.findViewById(R.id.button7),
                view.findViewById(R.id.button8),
                view.findViewById(R.id.button9),
                view.findViewById(R.id.button10),
                view.findViewById(R.id.button11),
                view.findViewById(R.id.button12),
                view.findViewById(R.id.button13)
        };

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
//                if (round2Ongoing) {
                    showMatchSummary();
//                    return;
//                }
//                Toast.makeText(getActivity(), "2. Igra\u010d zapo\u010dinje", Toast.LENGTH_SHORT).show();
//                requestAndStoreGameData();
//                prepareUpcomingRoundUI();
//                this.start();
//                round2Ongoing = true;
            }
        }.start();
//        Toast.makeText(getActivity(), "1. Igra\u010d zapo\u010dinje", Toast.LENGTH_SHORT).show();
    }

    private void prepareUpcomingRoundUI() {
        for (Button btn : buttons) {
            btn.setEnabled(true);
            btn.setOnClickListener(null);
            btn.setOnClickListener(this::onValueClick);
        }
        btnDeleteLastInput.setOnClickListener(null);
        btnDeleteLastInput.setOnClickListener(this::onDeleteLastInputClick);

        tvSolutionAttempt.setText("");
        tvSolutionAttempt.setTextColor(getResources().getColor(R.color.white_clear));
        lastClickedWasOperand = false;
        lastClickedWasNum = false;
//        ToDo
    }

    private void onDeleteLastInputClick(View v) {
        tvSolutionAttempt.setText(solutionAttempt.deleteCharAt(solutionAttempt.length() - 1).toString());
    }

    private void requestAndStoreGameData() {
//        ToDo
    }

    private void showMatchSummary() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, MatchSummaryFragment.newInstance(gameActivity)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 2*SECOND);
    }

    private void onValueClick(View v) {
        String characterOfClickedBtn = ( (Button) v).getText().toString();

        switch (characterOfClickedBtn){
            case "(":
            case ")":
                tvSolutionAttempt.setText(solutionAttempt.append(characterOfClickedBtn).toString());
                lastClickedWasOperand = false;
                lastClickedWasNum = false;
                break;

            case "*":
            case "/":
            case "-":
            case "+":
                if (solutionAttempt.length() == 0)
                    break;
                if (lastClickedWasOperand)
                    tvSolutionAttempt.setText(solutionAttempt.replace(solutionAttempt.length()-1, solutionAttempt.length()-1, String.valueOf(characterOfClickedBtn)).toString());
                lastClickedWasOperand = true;
                lastClickedWasNum = false;
                break;

            default:
//                In case a number was selected :
                if (lastClickedWasNum)
                    break;
                v.setEnabled(false);
                lastClickedWasOperand = false;
                lastClickedWasNum = true;
                tvSolutionAttempt.setText(solutionAttempt.append(characterOfClickedBtn).toString());
                break;
        }
    }

    private void onConfirmSolutionClick(View v) {
//        Todo :
//         evaluate "tvSolutionAttempt.getText().toString()" with https://www.baeldung.com/java-evaluate-math-expression-string
//          & compare calculated expression with "tvGoalNumber.getText().toString()" ;
        tvSolutionAttempt.setTextColor(getResources().getColor(R.color.teal_200));
    }
}