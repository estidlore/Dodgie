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
import com.blieve.dodgie.activity.A_Options;
import com.blieve.dodgie.util.Droid;

import static com.blieve.dodgie.model.GameStats.INIT_LVL;
import static com.blieve.dodgie.model.GameStats.MODE;

public class F_Mode extends Fragment {

    private static final String[] modes = {"clasic", "party", "overturned", "gravity"};
    private TextView[] mode_txts;
    private Intent _game;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_mode, container, false);
        mode_txts = new TextView[]{
                v.findViewById(R.id.mode_txt_clasic),
                v.findViewById(R.id.mode_txt_party),
                v.findViewById(R.id.mode_txt_overturned),
                v.findViewById(R.id.mode_txt_gravity)
        };
        init();
        return v;
    }

    private void init() {
        _game = new Intent(getActivity(), A_Game.class);
        clickListen();
        initLangs();
        int initLvl = 1;
        int mode = 0;
    }

    private void clickListen() {
        View.OnClickListener clickListen = v -> {
            // mode selection
            for(int i = mode_txts.length - 1; i >= 0; i--) {
                if(v == mode_txts[i]) {
                    _game.putExtra(INIT_LVL, 1);
                    _game.putExtra(MODE, i);
                    startActivity(_game);
                    return;
                }
            }
        };
        for(int i = mode_txts.length - 1; i >= 0; i--) {
            mode_txts[i].setOnClickListener(clickListen);
        }
    }

    public static int modesCount() {
        return modes.length;
    }

    private void initLangs() {
        int enIndex = Droid.Lang.indexOf(A_Options.ENGLISH),
                esIndex = Droid.Lang.indexOf(A_Options.SPANISH);
        String clasic = modes[0],
                party = modes[1],
                overturned = modes[2],
                gravity = modes[3];
        Droid.Lang lang = new Droid.Lang();
        lang.addText(clasic, enIndex, "Clasic");
        lang.addText(clasic, esIndex, "Clásico");
        lang.addText(party, enIndex, "Party");
        lang.addText(party, esIndex, "Fiesta");
        lang.addText(overturned, enIndex, "Overturned");
        lang.addText(overturned, esIndex, "De cabeza");
        lang.addText(gravity, enIndex, "Anti-gravity");
        lang.addText(gravity, esIndex, "Antigravedad");
        for(int i = mode_txts.length - 1; i >= 0; i--) {
            mode_txts[i].setText(lang.getText(modes[i]));
        }
    }

}
