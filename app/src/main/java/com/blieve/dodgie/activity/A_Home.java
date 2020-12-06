package com.blieve.dodgie.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blieve.dodgie.controller.ControlHome;
import com.blieve.dodgie.fragment.F_Mode;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;

import static android.graphics.PorterDuff.Mode.MULTIPLY;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.String.valueOf;

public class A_Home extends Droid {

    private ConstraintLayout pop;
    private ControlHome control;
    private ImageView imgClose, imgInfo, imgLeaderBoard, imgMode, imgOptions, imgSkin;
    private Intent _info, _leaderBoard, _options, _skin;
    private TextView txtCoins, txtGems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_home);
        control = findViewById(R.id.home_control);
        imgClose = findViewById(R.id.home_close);
        imgInfo = findViewById(R.id.home_info);
        imgLeaderBoard = findViewById(R.id.home_leaderboard);
        imgMode = findViewById(R.id.home_mode);
        imgOptions = findViewById(R.id.home_options);
        imgSkin = findViewById(R.id.home_skin);
        pop = findViewById(R.id.home_pop);
        txtCoins = findViewById(R.id.home_txt_coins);
        txtGems = findViewById(R.id.home_txt_diamonds);
        init();
    }

    public void onResume() {
        super.onResume();
        control.start();
    }

    public void onPause() {
        super.onPause();
        control.stop();
    }

    private void init() {
        pop.setVisibility(GONE);
        imgMode.setVisibility(VISIBLE);
        txtCoins.setText(valueOf(User.get().coins));
        txtGems.setText(valueOf(User.get().gems));

        int diameter = (int) (SCREEN_W * 0.08);
        User user = User.get();
        Resources res = getResources();
        Drawable skin = res.getDrawable(R.drawable.circle),
                face = res.getDrawable(user.style(0));
        skin.setColorFilter(user.style(1), MULTIPLY);
        face.setColorFilter(user.style(2), MULTIPLY);
        imgSkin.setImageBitmap(bitmapMerge(
                drawableToBitmap(skin, diameter, diameter),
                drawableToBitmap(face, diameter, diameter)
        ));

        _info = new Intent(A_Home.this, A_Info.class);
        _leaderBoard = new Intent(A_Home.this, A_Leaderboard.class);
        _options = new Intent(A_Home.this, A_Options.class);
        _skin = new Intent(A_Home.this, A_Skin.class);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_pop_frg, new F_Mode()).commit();

        clickListener();
    }

    private void clickListener() {
        View.OnClickListener clickListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == imgClose) {
                    imgClose.setVisibility(GONE);
                    pop.setVisibility(GONE);
                    imgMode.setVisibility(VISIBLE);
                    imgSkin.setVisibility(VISIBLE);
                } else if(v == imgMode) {
                    imgMode.setVisibility(GONE);
                    imgSkin.setVisibility(GONE);
                    pop.setVisibility(VISIBLE);
                    imgClose.setVisibility(VISIBLE);
                } else {
                    //sub-menus
                    control.stop();
                    if(v == imgInfo) {
                        startActivity(_info);
                    } else if(v == imgLeaderBoard) {
                        startActivity(_leaderBoard);
                    } else if(v == imgOptions) {
                        startActivity(_options);
                    }else if(v == imgSkin) {
                        startActivity(_skin);
                    }
                }
            }
        };
        imgClose.setOnClickListener(clickListen);
        imgMode.setOnClickListener(clickListen);
        imgInfo.setOnClickListener(clickListen);
        imgLeaderBoard.setOnClickListener(clickListen);
        imgOptions.setOnClickListener(clickListen);
        imgSkin.setOnClickListener(clickListen);
    }

    /*private boolean pointInCircle(float pX, float pY, float cX, float cY, float cR) {
        float dx = pX - cX,
                dy = pY - cY;
        return Math.sqrt(dx * dx + dy * dy) <= cR;
    }*/

}
