package com.blieve.dodgie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;

public class A_Shop extends Droid {

    private ImageView img_back;
    private Intent _home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_shop);

        img_back = findViewById(R.id.shop_back);

        init();
    }

    private void init() {
        _home = new Intent(A_Shop.this, A_Home.class);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == img_back) {
                    startActivity(_home);
                }
            }
        };
        img_back.setOnClickListener(clickListener);
    }

}