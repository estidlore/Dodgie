package com.blieve.dodgie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;

public class A_Leaderboard extends Droid.BaseActivity {

    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_leaderboard);

        img_back = findViewById(R.id.leader_back);

        init();
    }

    private void init() {
        View.OnClickListener clickListener = v -> {
            if (v == img_back) {
                finish();
            }
        };
        img_back.setOnClickListener(clickListener);
    }

}
