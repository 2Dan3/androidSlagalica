package com.ftn.slagalica;

import static com.ftn.slagalica.util.AuthHandler.FIREBASE_URL;

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
import com.ftn.slagalica.data.model.User;
import com.google.firebase.database.FirebaseDatabase;

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
    private GameActivity gameActivity;

    public MatchSummaryFragment() { }

    public MatchSummaryFragment(boolean gameWasQuit) { this.gameWasQuit = gameWasQuit; }

    public static MatchSummaryFragment newInstance(GameActivity gameActivity) {
        MatchSummaryFragment fragment = new MatchSummaryFragment();
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
//        player1Points = gameActivity.getPlayer1PointsView();
//        player2Points = gameActivity.getPlayer2PointsView();
        gameActivity.resetCallback();
        removeCurrentMatch();
        saveLoggedPlayerAccomplishments();
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
        TextView finalPointsPlayer1 = v.findViewById(R.id.tvPointsWonPlayer1);
        TextView wonStarsPlayer1 = v.findViewById(R.id.tvStarsWonPlayer1);
        TextView wonTokensPlayer1 = v.findViewById(R.id.tvTokensWonPlayer1);
        TextView tvWinnerUsername = v.findViewById(R.id.tvWinnerUsername);

        String winnerUsername;

        if (gameWasQuit) {
            winnerUsername = gameActivity.getOpponentPlayer().getUsername();
            tvWinnerUsername.setText(String.format("Pobednik: %s!", winnerUsername));
            finalPointsPlayer1.setText("+0");
            wonStarsPlayer1.setText("+0");
            wonTokensPlayer1.setText("+0");
        }else{
            User loggedPlayer = gameActivity.getLoggedPlayer();
            User opponentPlayer = gameActivity.getOpponentPlayer();
            if (loggedPlayer.getPointsCurrentMatch() > gameActivity.getOpponentPlayer().getPointsCurrentMatch()) {
                winnerUsername = loggedPlayer.getUsername();
                tvWinnerUsername.setText(String.format("Pobednik: %s!", winnerUsername));
            }else if (loggedPlayer.getPointsCurrentMatch() < opponentPlayer.getPointsCurrentMatch()) {
                winnerUsername = opponentPlayer.getUsername();
                tvWinnerUsername.setText(String.format("Pobednik: %s!", winnerUsername));
            }else {
                tvWinnerUsername.setText("Izjedna\u010deno!");
            }

            String scoreFormat = "%d";
            int pointsCurrMatch = gameActivity.getLoggedPlayer().getPointsCurrentMatch();
            if (pointsCurrMatch > 0)
                scoreFormat = "+%d";
            else
                scoreFormat = "%d";
            finalPointsPlayer1.setText(String.format(scoreFormat, pointsCurrMatch));
            int starAdditionByPoints = gameActivity.getLoggedPlayer().getPointsCurrentMatch() / 40;
            int starAdditionByWin = gameActivity.getLoggedPlayer().getPointsCurrentMatch() >= gameActivity.getOpponentPlayer().getPointsCurrentMatch() ? 10 : -10;
            int starTotalCalc = starAdditionByPoints + starAdditionByWin;
            if (starTotalCalc > 0)
                scoreFormat = "+%d";
            else
                scoreFormat = "%d";
            wonStarsPlayer1.setText(String.format(scoreFormat, starTotalCalc));
//          //          //
            wonTokensPlayer1.setText(String.format("+%d", 0));
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
        toMainActivity();
    }

    private void saveLoggedPlayerAccomplishments() {
//        Todo save match results for logged (local session) player to Database
        User loggedPlayer = gameActivity.getLoggedPlayer();
        if ( !"".equals(loggedPlayer.getEmail())) {
            loggedPlayer.setPlayed(loggedPlayer.getPlayed() + 1);
            if (loggedPlayer.getPointsCurrentMatch() > gameActivity.getOpponentPlayer().getPointsCurrentMatch()) {
                loggedPlayer.setWon(loggedPlayer.getWon() + 1);
            }
            FirebaseDatabase.getInstance(FIREBASE_URL).getReference("users").child(gameActivity.getLoggedPlayer().getUsername()).setValue(gameActivity.getLoggedPlayer());
        }
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

    private void removeCurrentMatch() {
        FirebaseDatabase db = FirebaseDatabase.getInstance(FIREBASE_URL);

        db.getReference("active_matches").child(gameActivity.getGameID()).removeValue();

        db.getReference("queue").removeValue();
    }
}