package com.blieve.dodgie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;

import static android.content.Intent.ACTION_VIEW;
import static android.net.Uri.parse;
import static com.blieve.dodgie.activity.A_Options.langIndex;

public class A_Info extends Droid {

    private ImageView back_img, blieve_img, game_img;
    private TextView about_txt;
    private Intent _home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_info);

        back_img = findViewById(R.id.info_img_back);
        about_txt = findViewById(R.id.info_about);
        blieve_img = findViewById(R.id.info_blieve);
        game_img = findViewById(R.id.info_game);
        init();
    }

    private void init() {
        int[] txts = {R.string.about, R.string.about_es};
        about_txt.setText(txts[langIndex]);
        _home = new Intent(A_Info.this, A_Home.class);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == back_img) {
                    startActivity(_home);
                } else if(v == blieve_img) {
                    String page = "http://www.facebook.com/blieve_games/";
                    startActivity(new Intent(ACTION_VIEW, parse(page)));
                } else if(v == game_img) {
                    String page = "http://www.facebook.com/Dodgie-101790741479191/";
                    startActivity(new Intent(ACTION_VIEW, parse(page)));
                }
            }
        };
        back_img.setOnClickListener(onClickListener);
        blieve_img.setOnClickListener(onClickListener);
        game_img.setOnClickListener(onClickListener);
    }



}
