package com.blieve.dodgie.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blieve.dodgie.R;
import com.blieve.dodgie.activity.A_Game;
import com.blieve.dodgie.model.GameStats;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.util.Droid;
import com.blieve.dodgie.util.TxtAdapter;

public class F_Mode extends Fragment {

    private static final String[] modes = {"clasic", "party", "overturned", "gravity"};

    private final String initLvl = "initLvl";

    private ImageView imgLvlMinus, imgLvlPlus, imgPlay;
    private Intent _game;
    private TextView txtCostCoins, txtCostDiamonds, txtHighLvl, txtHighScore, txtInitLvl,
            txtInitLvlValue;
    private TxtAdapter txtAdapter;

    private String[] modesText;
    private Droid.Lang lang;
    private ListView list;
    private Droid.Media media;

    private int costCoins, costDiamonds, highLvl, highScore, initLvlValue, mode, lastLang;

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
        setModeValues();
        if (lastLang != Droid.Lang.getLang()) {
            setTextLangs();
        }
    }

    private void init() {
        _game = new Intent(getActivity(), A_Game.class);
        modesText = new String[modes.length];
        txtAdapter = new TxtAdapter(getContext(), R.layout.f_mode_list_item, modesText);
        list.setAdapter(txtAdapter);
        media = Droid.Media.get();
        selectMode(0);
        clickListen();
        itemClickListen();
        initLangs();
    }

    private void clickListen() {
        View.OnClickListener clickListen = v -> {
            media.play(Droid.Media.CLICK);
            if(v == imgLvlMinus) {
                initLvlValue--;
                setCosts();
            } else if (v == imgLvlPlus) {
                initLvlValue++;
                setCosts();
            } else if (v == imgPlay) {
                if (User.get().subtractCost(costCoins, costDiamonds)) {
                    _game.putExtra(GameStats.INIT_LVL, initLvlValue);
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
        AdapterView.OnItemClickListener itemClickListen = (parent, view, position, id) -> {
            if(position != txtAdapter.getSelection()) {
                media.play(Droid.Media.CLICK);
                selectMode(position);
            }
        };
        list.setOnItemClickListener(itemClickListen);
    }

    private void setModeValues() {
        User user = User.get();
        highLvl = user.highLvl(mode);
        highScore = user.highScore(mode);
        txtHighLvl.setText(String.valueOf(highLvl));
        txtHighScore.setText(String.valueOf(highScore));
    }

    private void setCosts() {
        txtInitLvlValue.setText(String.valueOf(initLvlValue));
        imgLvlMinus.setVisibility(initLvlValue > 1 ? View.VISIBLE : View.GONE);
        imgLvlPlus.setVisibility(initLvlValue < highLvl ? View.VISIBLE : View.GONE);
        costCoins = (initLvlValue - 1) * GameStats.PTS_PER_LVL;
        costDiamonds = (initLvlValue - 1);
        txtCostCoins.setText(String.valueOf(costCoins));
        txtCostDiamonds.setText(String.valueOf(costDiamonds));
    }

    private void selectMode(int mode) {
        this.mode = mode;
        initLvlValue = 1;
        setModeValues();
        setCosts();
        txtAdapter.setSelection(mode);
    }

    public static int modesCount() {
        return modes.length;
    }

    private void initLangs() {
        int enIndex = Droid.Lang.indexOf(Droid.Lang.ENGLISH),
                esIndex = Droid.Lang.indexOf(Droid.Lang.SPANISH);
        lang = new Droid.Lang();
        // Initial lvl
        lang.addText(initLvl, enIndex, "Initial level");
        lang.addText(initLvl, esIndex, "Nivel inicial");
        // Modes
        lang.addText(modes[0], enIndex, "Clasic");
        lang.addText(modes[0], esIndex, "Clásico");
        lang.addText(modes[1], enIndex, "Party");
        lang.addText(modes[1], esIndex, "Fiesta");
        lang.addText(modes[2], enIndex, "Overturned");
        lang.addText(modes[2], esIndex, "De cabeza");
        lang.addText(modes[3], enIndex, "Anti-gravity");
        lang.addText(modes[3], esIndex, "Antigravedad");
        setTextLangs();
    }

    private void setTextLangs() {
        lastLang = Droid.Lang.getLang();
        txtInitLvl.setText(lang.getText(initLvl));
        for(int i = modes.length - 1; i >= 0; i--) {
            modesText[i] = lang.getText(modes[i]);
        }
        txtAdapter.notifyDataSetChanged();
    }

}
