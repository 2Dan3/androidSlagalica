package com.ftn.slagalica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

//    private static final Fragment[] GAMES_IN_ORDER = {new GameWhoKnowsKnowsFragment(), new GameConnectTwoFragment(),new GameAssociationsFragment(), new GameJumperFragment(), new GameStepByStepFragment(), new GameMyNumberFragment()};
    private static final int SECOND = 1000;
    private static final int MINUTE = 60*SECOND;

    private static final int COLOR_RIGHT_MATCH = 0xFF03DAC5;
    private static final int COLOR_WRONG_MATCH = 0XFFF44336;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        startMatch();
    }

    private void startMatch() {

//        Match postavke
//              ...
        prepFirstGame();

//        Timer timer = new Timer();

//        Thread thread = new Thread(){
//            public void run(){
//                runOnUiThread(
//                        () -> {
//                            for (Fragment nextGame : GAMES_IN_ORDER) {
//                                timer.schedule(new TimerTask() {
//                                    @Override
//                                    public void run() {
//                                        getSupportFragmentManager().beginTransaction().replace(R.id.game_fragment_container, nextGame).commit();
//                                        //                    Toast.makeText(GameActivity.this, "1. igra", Toast.LENGTH_SHORT).show();
//                                    }
//                                }, 5 * SECOND);
//                            }
//                        });
//            }
//        };
//        thread.start();
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
}