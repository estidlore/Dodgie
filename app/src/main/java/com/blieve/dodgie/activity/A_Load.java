package com.blieve.dodgie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blieve.dodgie.model.Block;
import com.blieve.dodgie.model.Obstacle;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;
import com.blieve.dodgie.util.Update;

import static com.blieve.dodgie.controller.ControlGame.CELLS_X;
import static com.blieve.dodgie.controller.ControlGame.CELLS_Y;
import static com.blieve.dodgie.controller.ControlGame.CELL_W;

public class A_Load extends Droid {

    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_load);

        layout = findViewById(R.id.load);

        init();
    }

    private void init() {
        Droid.init(this, true);
        CELLS_Y = 9;
        CELL_W = SCREEN_H / CELLS_Y;
        CELLS_X = SCREEN_W / CELL_W;
        Obstacle.setBounds(
                SCREEN_W / 2f + CELL_W * 2,
                SCREEN_H / 2f + CELL_W * 2
        );
        Block.setWidth(CELL_W);

        final ImageView load_bar = findViewById(R.id.load_bar);
        final ImageView advance_bar = findViewById(R.id.load_advance);
        final Intent _home = new Intent(this, A_Home.class);
        final float[] load = {0};
        User.get().init(getSharedPreferences(User.ALIAS, MODE_PRIVATE));

        final int tps = 60, seconds = 2;
        advance_bar.setPivotX(0);
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                load[0] += 100f / tps / seconds;
                advance_bar.setScaleX(load[0] / 100);
            }
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
