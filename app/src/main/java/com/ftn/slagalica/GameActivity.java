package com.ftn.slagalica;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.slagalica.util.IThemeHandler;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements IThemeHandler {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60*SECOND;
    private static final String LEAVE_GAME_DIALOG_TAG = "LEAVE_GAME_DIALOG";

    private OnBackPressedCallback callback;
    public void resetCallback() {
//        callback.remove();

//        Fragment df;
//        if ( (df = getFragmentManager().findFragmentByTag(LEAVE_GAME_DIALOG_TAG)) != null ) {
//            ((android.app.DialogFragment) df).dismiss();
//        }
        callback.setEnabled(false);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() { }
        });
    }

    private TextView player1PointsView;
    private TextView player2PointsView;

    private static final int COLOR_RIGHT_MATCH = 0xFF03DAC5;
    private static final int COLOR_WRONG_MATCH = 0XFFF44336;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        player1PointsView = findViewById(R.id.textViewPlayer1Points);
        player2PointsView = findViewById(R.id.textViewPlayer2Points);
        startMatch();

        // This callback is only called when MyFragment is at least started
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new LeaveGameDialogFragment().show(getSupportFragmentManager(), LEAVE_GAME_DIALOG_TAG);
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()
//        callback.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Todo : send signal to other client in current match, that the game was quit ->  then (if match is still unfinished) some of games' rounds can be
//         skipped for Player that left, OR game can end for the remaining Player who gets the won points!
    }

    private void startMatch() {
//        Match postavke
//              ...
        player1PointsView.setText("0");
        player2PointsView.setText("0");

        prepFirstGame();
    }
    private void prepFirstGame() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, new GameWhoKnowsKnowsFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();
            }
        }, 2*SECOND);
    }

    public void changeFieldColor(View v) {
        v.setBackgroundColor(COLOR_RIGHT_MATCH);
    }

    public void openField(View v) {
//        TODO
//         Calculations upon field opening
        revealWord(v);
    }
    private void revealWord(View v) {
//        TODO
//         change text color white_smoked -> dark_blue to reveal it
    }

    public void revealNextStepText(View v) {
//        TODO
//         change textView's text color from white to dark_blue
    }


    public int getPlayer1PointsView() {
        return Integer.valueOf(String.valueOf(player1PointsView.getText()));
    }
    public void setPlayer1PointsView(int player1PointsView) {
        this.player1PointsView.setText(String.valueOf(player1PointsView));
    }
    public int getPlayer2PointsView() {
        return Integer.valueOf(String.valueOf(player2PointsView.getText()));
    }
    public void setPlayer2PointsView(int player2PointsView) {
        this.player2PointsView.setText(String.valueOf(player2PointsView));
    }

    public static class LeaveGameDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.leave_game_dialog_title)
                    .setMessage(R.string.leave_game_dialog)
                    .setPositiveButton(R.string.leave_game_confirm, (dialog, id) -> {
                        Toast.makeText(getActivity(), R.string.leave_game_successful_info, Toast.LENGTH_SHORT).show();

//                        Todo : instead of MainActivity, switch to MatchSummaryFragment(gameWasQuit = true) ->  with 0 points, stars & tokens won shown
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, new MatchSummaryFragment(true)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).commit();

                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    })
                    .setNegativeButton(R.string.leave_game_cancel, (dialog, id) -> {
                        this.dismiss();
                    });
            return builder.create();
        }
    }
}