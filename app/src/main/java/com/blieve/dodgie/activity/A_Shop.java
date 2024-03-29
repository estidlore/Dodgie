package com.blieve.dodgie.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;

public class A_Shop extends BaseActivity {

    private ImageView img_back;

    private Droid.Lang lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_shop);

        img_back = findViewById(R.id.shop_back);

        init();
    }

    private void init() {
        clickListen();
    }

    private void clickListen() {
        View.OnClickListener clickListener = v -> {
            if(v == img_back) {
                media.play(Droid.Media.CLOSE);
                finish();
                return;
            }
            media.play(Droid.Media.CLICK);
        };
        img_back.setOnClickListener(clickListener);
    }

}
