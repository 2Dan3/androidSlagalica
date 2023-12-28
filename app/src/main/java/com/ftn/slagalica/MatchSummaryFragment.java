package com.ftn.slagalica;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.slagalica.data.model.Player;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MatchSummaryFragment extends Fragment {

    private boolean gameWasQuit = false;
    private TextView timer;
    private CountDownTimer countDownTimer;

    private int player1Points;
    private int player2Points;

    private int player1Stars;
    private int player2Stars;

    private int player1Tokens;
    private int player2Tokens;

    private Player winner;
    private Player loser;

    public MatchSummaryFragment() { }

    public MatchSummaryFragment(boolean gameWasQuit) { this.gameWasQuit = gameWasQuit; }

    public static MatchSummaryFragment newInstance(String param1, String param2) {
        MatchSummaryFragment fragment = new MatchSummaryFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameActivity gameActivity = (GameActivity) getActivity();
//        player1Points = gameActivity.getPlayer1PointsView();
//        player2Points = gameActivity.getPlayer2PointsView();
        gameActivity.resetCallback();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_summary, container, false);

//        TextView finalPointsPlayer1 = (TextView) v.findViewById(R.id.tvPointsWonPlayer1);
//        finalPointsPlayer1.setText("+" + player1Points);
//
//        TextView finalPointsPlayer2 = (TextView) v.findViewById(R.id.tvPointsWonPlayer2);
//        finalPointsPlayer2.setText("+" + player2Points);
//
//        processPlayerResults(v);
//
//        TextView wonStarsPlayer1 = (TextView) v.findViewById(R.id.tvStarsWonPlayer1);
//        wonStarsPlayer1.setText(String.valueOf());
        if (gameWasQuit) {
            TextView finalPointsPlayer1 = (TextView) v.findViewById(R.id.tvPointsWonPlayer1);
            finalPointsPlayer1.setText("+0");
            TextView wonStarsPlayer1 = (TextView) v.findViewById(R.id.tvStarsWonPlayer1);
            wonStarsPlayer1.setText("+0");
            TextView wonTokensPlayer1 = (TextView) v.findViewById(R.id.tvTokensWonPlayer1);
            wonTokensPlayer1.setText("+0");
        }

        return v;
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

        startTimerCountdown(11*1000);
    }

    private void processPlayerResults(View currentView) {
//        winner.
    }

    private void findWinnerAndLoser(View currentView) {
//        Todo * get current Match session's Players-instances :
//          Player[] players = get from session  ...

//        TEST-DATA :
//        TextView tvP1Username = (TextView) currentView.findViewById(R.id.textViewPlayer1Username);
//        TextView tvP2Username = (TextView) currentView.findViewById(R.id.textViewPlayer2Username);
//
//        Player p1 = new Player(tvP1Username.getText().toString(),"","","", 250, 6);
//        p1.setCurrentPointsInMatch(player1Points);
//        Player p2 = new Player(tvP2Username.getText().toString(),"","","",160, 4);
//        p2.setCurrentPointsInMatch(player2Points);
//        *


//        Todo * who wins if P1 points == P2 points ? ? ?
//        if (p1.getCurrentPointsInMatch() > p2.getCurrentPointsInMatch()){
//            winner = p1;
//            loser = p2;
//        }else {
//            winner = p2;
//            loser = p1;
//        }
    }

    public void endMatch() {
        savePlayerAccomplishments();

//        if (successful Save):
        toMainActivity();
    }

    private void savePlayerAccomplishments() {
//        Todo save match results for both players to Database
    }

    private void toMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    private void startTimerCountdown(int msTimeFrom) {
        countDownTimer = new CountDownTimer(msTimeFrom, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText( String.valueOf(millisUntilFinished / 1000) );
            }
            @Override
            public void onFinish() {
                endMatch();
            }
        }.start();
        Toast.makeText(getActivity(), "Igra je zavr\u0161ena", Toast.LENGTH_SHORT).show();
    }
}