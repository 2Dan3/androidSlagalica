package com.ftn.slagalica;

import static com.ftn.slagalica.util.AuthHandler.FIREBASE_URL;

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
import android.widget.Toast;

import com.ftn.slagalica.data.model.DTO.WhoKnowsQuestionsDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameWhoKnowsKnowsFragment extends Fragment {

    private static final int SECOND = 1000;
    private TextView textViewQuestionNum;
    private TextView textViewQuestion;
    private Button[] answerButtons;
    private TextView timer;
    private CountDownTimer countDownTimer;
    private WhoKnowsQuestionsDTO questionsValues;

    private int currentQuestionNum = 1;
    private String currentQuestion;

    public GameWhoKnowsKnowsFragment() { }

    public static GameWhoKnowsKnowsFragment newInstance(String param1, String param2) {
        GameWhoKnowsKnowsFragment fragment = new GameWhoKnowsKnowsFragment();
//        Bundle args = new Bundle();
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
        requestAndStoreGameData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_who_knows_knows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timer = getActivity().findViewById(R.id.textViewTimer);

        textViewQuestionNum = view.findViewById(R.id.who_knows_question_num);
        textViewQuestion = view.findViewById(R.id.who_knows_question);

        answerButtons = new Button[]{
            view.findViewById(R.id.who_knows_answer1),
            view.findViewById(R.id.who_knows_answer2),
            view.findViewById(R.id.who_knows_answer3),
            view.findViewById(R.id.who_knows_answer4)
        };

//        prepareUpcomingRoundUI();
//
//        startTimerCountdown(6*SECOND);
    }

    private void startTimerCountdown(int msTimeFrom) {
        countDownTimer = new CountDownTimer(msTimeFrom, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText( String.valueOf(millisUntilFinished / SECOND) );
            }

            @Override
            public void onFinish() {
                if (currentQuestionNum == 5) {
                    prepNextGame();
                    return;
                }
                currentQuestionNum += 1;
                prepareUpcomingRoundUI();
                this.start();
            }
        }.start();
    }

    private void prepNextGame() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, new GameConnectTwoFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 2*SECOND);
    }

    private void prepareUpcomingRoundUI() {
        textViewQuestionNum.setText(String.format("Pitanje %d / 5", currentQuestionNum));

        currentQuestion = questionsValues.get(currentQuestionNum-1).keySet().toArray()[0].toString();

        textViewQuestion.setText(currentQuestion);

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(questionsValues.get(currentQuestionNum-1).get(currentQuestion)[i]);
            answerButtons[i].setEnabled(true);
            answerButtons[i].setTextColor(getResources().getColor(R.color.white_clear));
            answerButtons[i].setOnClickListener(this::onAnswerClick);
        }
    }

    private void requestAndStoreGameData() {
//        Todo : request actual Game values from Firebase for all 5 questions
        questionsValues = new WhoKnowsQuestionsDTO();

        DatabaseReference gameDataRef = FirebaseDatabase.getInstance(FIREBASE_URL).getReference("whoknows").child("-NoHjK2hB9eLjLY3kYFq");
        gameDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot questionRoundSnapshot : snapshot.getChildren()) {
                        HashMap<String, String[]> questionPack = new HashMap<String, String[]>();

                        String[] answers = new String[6];
                        Iterable<DataSnapshot> answersUnfiltered = questionRoundSnapshot.getChildren();
                        int counter = 0;
                        while (answersUnfiltered.iterator().hasNext()){
                            answers[counter++] = answersUnfiltered.iterator().next().getValue().toString();
                        }

                        questionPack.put(questionRoundSnapshot.getKey(), answers);
                        questionsValues.add(questionPack);
                    }
//                    Toast.makeText(getActivity(), gameValues.toString(), Toast.LENGTH_SHORT).show();
                    prepareUpcomingRoundUI();

                    startTimerCountdown(6*SECOND);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

//        Mock/Test data initialized :
//        questionsValues = new WhoKnowsQuestionsDTO();
    }

    private void onAnswerClick(View v){
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setEnabled(false);
            answerButtons[i].setOnClickListener(null);
        }

        Button clickedAnswerBtn = (Button) v;
//        Validates answer with real solution
        if (clickedAnswerBtn.getText().toString().equals(questionsValues.get(currentQuestionNum-1).get(currentQuestion)[4])){
            clickedAnswerBtn.setTextColor(getResources().getColor(R.color.teal_200));
//          Todo :  assign points to this logged player if he was first to answer correctly
        }else{
            clickedAnswerBtn.setTextColor(getResources().getColor(R.color.orange_dark));
        }
    }

}