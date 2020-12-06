package com.blieve.dodgie.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blieve.dodgie.R;
import com.blieve.dodgie.activity.A_Game;

import static com.blieve.dodgie.activity.A_Options.langIndex;
import static com.blieve.dodgie.model.GameStats.INIT_LVL;
import static com.blieve.dodgie.model.GameStats.MODE;

public class F_Mode extends Fragment {

    private final static String[][] modes = {
            {"Clasic", "Inverted", "Party", "Gravity"},
            {"Clásico", "Invertido", "Fiesta", "Gravedad"}
    };
    private TextView[] mode_txts;
    private Intent _game;
    private int initLvl, mode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_mode, container, false);
        mode_txts = new TextView[]{
                v.findViewById(R.id.mode_txt_clasic),
                v.findViewById(R.id.mode_txt_vertical),
                v.findViewById(R.id.mode_txt_party),
                v.findViewById(R.id.mode_txt_overturned)
        };
        init();
        return v;
    }

    private void init() {
        _game = new Intent(getActivity(), A_Game.class);
        clickListen();
        initLvl = 1;
        mode = 0;
    }

    private void clickListen() {
        View.OnClickListener clickListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mode selection
                for(int i = mode_txts.length - 1; i >= 0; i--) {
                    if(v == mode_txts[i]) {
                        _game.putExtra(INIT_LVL, 1);
                        _game.putExtra(MODE, i);
                        startActivity(_game);
                        return;
                    }
                }
            }
        };
        for(int i = mode_txts.length - 1; i >= 0; i--) {
            mode_txts[i].setText(modes[langIndex][i]);
            mode_txts[i].setOnClickListener(clickListen);
        }
    }

    public static int modesCount() {
        return modes[0].length;
    }

}
