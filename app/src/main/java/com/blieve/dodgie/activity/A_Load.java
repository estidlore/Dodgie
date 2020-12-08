package com.blieve.dodgie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blieve.dodgie.controller.ControlGame;
import com.blieve.dodgie.model.Block;
import com.blieve.dodgie.model.Obstacle;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;
import com.blieve.dodgie.util.Update;

public class A_Load extends Droid.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_load);

        final ConstraintLayout layout = findViewById(R.id.load);
        //final ImageView load_bar = findViewById(R.id.load_bar);
        final ImageView advance_bar = findViewById(R.id.load_advance);

        final Intent _home = new Intent(this, A_Home.class);
        final float[] load = {0};
        User.get().init(getSharedPreferences(User.ALIAS, MODE_PRIVATE));

        Droid.initApp(this, true);
        ControlGame.CELLS_Y = 9;
        ControlGame.CELL_W = Droid.SCREEN_H / ControlGame.CELLS_Y;
        ControlGame.CELLS_X = Droid.SCREEN_W / ControlGame.CELL_W;
        Obstacle.setBounds(
                Droid.SCREEN_W / 2f + ControlGame.CELL_W * 2,
                Droid.SCREEN_H / 2f + ControlGame.CELL_W * 2
        );
        Block.setWidth(ControlGame.CELL_W);

        SharedPreferences prefs = getSharedPreferences(A_Options.PREF_CONFIG, MODE_PRIVATE);
        Droid.Lang.addLang(A_Options.ENGLISH);
        Droid.Lang.addLang(A_Options.SPANISH);
        Droid.Lang.setLang(prefs.getInt(A_Options.LANGUAGE, 0));

        final int tps = 60, seconds = 2;
        advance_bar.setPivotX(0);
        final Runnable r = () -> {
            load[0] += 100f / tps / seconds;
            advance_bar.setScaleX(load[0] / 100);
        };
        final Update update = new Update(tps) {
            @Override
            public void tick() {
                if(load[0] >= 100) {
                    startActivity(_home);
                    stop();
                }
                layout.post(r);
            }
        };
        update.start();
    }
}
