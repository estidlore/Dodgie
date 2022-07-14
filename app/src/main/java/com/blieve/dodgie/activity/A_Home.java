package com.blieve.dodgie.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.blieve.dodgie.controller.ControlHome;
import com.blieve.dodgie.fragment.F_Mode;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;

public class A_Home extends BaseActivity {

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

    @Override
    public void onResume() {
        super.onResume();
        setPlayerStyle();
        control.setBlockStyle();
        control.start();
        txtCoins.setText(String.valueOf(User.get().coins));
        txtGems.setText(String.valueOf(User.get().gems));
    }

    @Override
    public void onPause() {
        super.onPause();
        control.stop();
        closePop();
    }

    private void init() {
        _info = new Intent(A_Home.this, A_Info.class);
        _leaderBoard = new Intent(A_Home.this, A_Leaderboard.class);
        _options = new Intent(A_Home.this, A_Options.class);
        _skin = new Intent(A_Home.this, A_Style.class);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_pop_frg, new F_Mode()).commit();

        clickListener();
    }

    private void clickListener() {
        View.OnClickListener clickListen = v -> {
            if(v == imgClose) {
                media.play(Droid.Media.CLOSE);
                closePop();
                return;
            }
            media.play(Droid.Media.CLICK);
            if(v == imgMode) {
                imgMode.setVisibility(View.GONE);
                imgSkin.setVisibility(View.GONE);
                pop.setVisibility(View.VISIBLE);
                imgClose.setVisibility(View.VISIBLE);
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
        };
        imgClose.setOnClickListener(clickListen);
        imgMode.setOnClickListener(clickListen);
        imgInfo.setOnClickListener(clickListen);
        imgLeaderBoard.setOnClickListener(clickListen);
        imgOptions.setOnClickListener(clickListen);
        imgSkin.setOnClickListener(clickListen);
    }

    private void closePop() {
        imgClose.setVisibility(View.GONE);
        pop.setVisibility(View.GONE);
        imgMode.setVisibility(View.VISIBLE);
        imgSkin.setVisibility(View.VISIBLE);
    }

    private void setPlayerStyle() {
        Resources res = getResources();
        Drawable skin = ResourcesCompat.getDrawable(res, R.drawable.player, null),
                face = ResourcesCompat.getDrawable(res, User.get().getStyleDrawable(0), null);
        skin.setColorFilter(User.get().getStyleDrawable(1), PorterDuff.Mode.MULTIPLY);
        face.setColorFilter(User.get().getStyleDrawable(2), PorterDuff.Mode.MULTIPLY);

        int diameter = Droid.UI.width(8);
        imgSkin.setImageBitmap(Droid.Img.bmpMerge(
                Droid.Img.drawToBmp(skin, diameter, diameter),
                Droid.Img.drawToBmp(face, diameter, diameter)
        ));
    }

}
