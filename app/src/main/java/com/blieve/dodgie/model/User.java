package com.blieve.dodgie.model;

import android.content.SharedPreferences;

import com.blieve.dodgie.activity.A_Skin;
import com.blieve.dodgie.fragment.F_Mode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class User {

    private static User instance;

    public static User get() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public static final String
            ALIAS = "user",
            COINS = "coins",
            GEMS = "gems",
            LEAGUE_POINTS = "lpts",
            STYLE = "style",
            HIGH_SCORE = "hs",
            HIGH_LEVEL = "hl";

    public String alias;
    public int coins, gems, lpts;
    private int[] styles;

    private final ArrayList<Integer> highScores, highLvls;
    private SharedPreferences prefs;

    private User() {
        highScores = new ArrayList<>();
        highLvls = new ArrayList<>();
    }

    public void init(SharedPreferences prefs) {
        highScores.clear();
        highLvls.clear();
        this.prefs = prefs;
        for(int i = F_Mode.modesCount() - 1; i >= 0; i--) {
            highScores.add(prefs.getInt(HIGH_SCORE + i, 0));
        }
        for(int i = F_Mode.modesCount() - 1; i >= 0; i--) {
            highLvls.add(prefs.getInt(HIGH_LEVEL + i, 0));
        }
        alias = prefs.getString(ALIAS, ALIAS);
        coins = prefs.getInt(COINS, 0);
        gems = prefs.getInt(GEMS, 0);
        lpts = prefs.getInt(LEAGUE_POINTS,0);
        styles = new int[6];
        for(int i = styles.length - 1; i >= 0; i--) {
            styles[i] = prefs.getInt(STYLE + i,0);
        }
    }

    public void update(@NotNull GameStats stats) {
        if(stats.score() > highScore(stats.mode())) {
            highScores.set(stats.mode(), stats.score());
            prefs.edit().putInt(HIGH_SCORE + stats.mode(), stats.score()).apply();
        }
        if(stats.lvl() > highLvls.get(stats.mode())) {
            highLvls.set(stats.mode(), stats.lvl());
            prefs.edit().putInt(HIGH_LEVEL + stats.mode(), stats.lvl()).apply();
        }
        coins += stats.coins();
        gems += stats.gems();
        prefs.edit().putInt(COINS, coins)
                .putInt(GEMS, gems).apply();
    }

    public int highScore(int mode) {
        return highScores.get(mode);
    }

    public int style(int i) {
        return A_Skin.style(i % 3 == 0 ? (i / 3) : 2, styles[i]);
    }

    public void setStyle(int i, int style) {
        styles[i] = style;
    }

}