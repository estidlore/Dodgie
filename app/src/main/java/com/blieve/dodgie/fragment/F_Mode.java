package com.blieve.dodgie.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blieve.dodgie.R;
import com.blieve.dodgie.activity.A_Game;
import com.blieve.dodgie.activity.A_Options;
import com.blieve.dodgie.model.GameStats;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.util.Droid;

public class F_Mode extends Fragment {

    private static final String[] modes = {"clasic", "party", "overturned", "gravity"};
    private String[] modesText;
    private ImageView imgLvlMinus, imgLvlPlus, imgPlay;
    private ListView list;
    private TextView txtCostCoins, txtCostDiamonds, txtHighLvl, txtHighScore, txtInitLvl,
            txtInitLvlValue;
    private Intent _game;

    private int costCoins, costDiamonds, highLvl, highScore, initLvl, mode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_mode, container, false);
        imgLvlMinus = v.findViewById(R.id.mode_img_minus_lvl);
        imgLvlPlus = v.findViewById(R.id.mode_img_plus_lvl);
        imgPlay = v.findViewById(R.id.mode_img_play);
        list = v.findViewById(R.id.mode_list);
        txtHighLvl = v.findViewById(R.id.mode_txt_lvl);
        txtHighScore = v.findViewById(R.id.mode_txt_score);
        txtCostCoins = v.findViewById(R.id.mode_txt_coins);
        txtCostDiamonds = v.findViewById(R.id.mode_txt_diamonds);
        txtInitLvl = v.findViewById(R.id.mode_txt_init_lvl);
        txtInitLvlValue = v.findViewById(R.id.mode_txt_init_lvl_value);

        init();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectMode(mode);
    }

    private void init() {
        _game = new Intent(getActivity(), A_Game.class);
        modesText = new String[modes.length];
        list.setSelector(R.color.color_gray);
        clickListen();
        itemClickListen();
        initLangs();
    }

    private void clickListen() {
        View.OnClickListener clickListen = v -> {
            if(v == imgLvlMinus) {
                //if(initLvl > 1) {
                    --initLvl;
                    setCosts();
                //}
            } else if (v == imgLvlPlus) {
                //if(initLvl < highLvl) {
                    ++initLvl;
                    setCosts();
                //}
            } else if (v == imgPlay) {
                if (User.get().subtractCost(costCoins, costDiamonds)) {
                    _game.putExtra(GameStats.INIT_LVL, initLvl);
                    _game.putExtra(GameStats.MODE, mode);
                    startActivity(_game);
                } else {
                    // show an insuficient coins or diamonds message
                }
            }
        };
        imgLvlMinus.setOnClickListener(clickListen);
        imgLvlPlus.setOnClickListener(clickListen);
        imgPlay.setOnClickListener(clickListen);
    }

    private void itemClickListen() {
        AdapterView.OnItemClickListener itemClickListen = (parent, view, position, id)
                -> selectMode(position);
        list.setOnItemClickListener(itemClickListen);
    }

    private void setCosts() {
        txtInitLvlValue.setText(String.valueOf(initLvl));
        imgLvlMinus.setVisibility(initLvl > 1 ? View.VISIBLE : View.GONE);
        imgLvlPlus.setVisibility(initLvl < highLvl ? View.VISIBLE : View.GONE);
        costCoins = (initLvl - 1) * GameStats.PTS_PER_LVL;
        costDiamonds = (initLvl - 1);
        txtCostCoins.setText(String.valueOf(costCoins));
        txtCostDiamonds.setText(String.valueOf(costDiamonds));
    }

    private void selectMode(int mode) {
        this.mode = mode;
        initLvl = 1;
        User user = User.get();
        highLvl = user.highLvl(mode);
        highScore = user.highScore(mode);
        txtHighLvl.setText(String.valueOf(highLvl));
        txtHighScore.setText(String.valueOf(highScore));
        setCosts();
    }

    public static int modesCount() {
        return modes.length;
    }

    private void initLangs() {
        int enIndex = Droid.Lang.indexOf(A_Options.ENGLISH),
                esIndex = Droid.Lang.indexOf(A_Options.SPANISH);
        Droid.Lang lang = new Droid.Lang();
        // Initial lvl
        String initLvl = "initLvl";
        lang.addText(initLvl, enIndex, "Initial level");
        lang.addText(initLvl, esIndex, "Nivel inicial");
        txtInitLvl.setText(lang.getText(initLvl));
        // Modes
        String clasic = modes[0],
                party = modes[1],
                overturned = modes[2],
                gravity = modes[3];
        lang.addText(clasic, enIndex, "Clasic");
        lang.addText(clasic, esIndex, "Clásico");
        lang.addText(party, enIndex, "Party");
        lang.addText(party, esIndex, "Fiesta");
        lang.addText(overturned, enIndex, "Overturned");
        lang.addText(overturned, esIndex, "De cabeza");
        lang.addText(gravity, enIndex, "Anti-gravity");
        lang.addText(gravity, esIndex, "Antigravedad");
        for(int i = modes.length - 1; i >= 0; i--) {
            modesText[i] = lang.getText(modes[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.f_mode_list_item, modesText);
        list.setAdapter(adapter);
    }

}
