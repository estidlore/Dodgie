package com.blieve.dodgie.model;

import android.content.SharedPreferences;

import com.blieve.dodgie.activity.A_Style;
import com.blieve.dodgie.fragment.F_Mode;

import java.util.ArrayList;

public class User {

    private static User instance;

    public static User get() {
        if(instance == null) instance = new User();
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
    /*
    * 0: player skin
    * 1: player color 1
    * 2: player color 2
    * 3: block skin
    * 4: block color 1
    * 5: block color 2
    * */
    private final int[] styles;

    private final ArrayList<Integer> highScores, highLvls;
    private SharedPreferences prefs;

    private User() {
        highScores = new ArrayList<>();
        highLvls = new ArrayList<>();
        styles = new int[6];
    }

    public void init(SharedPreferences prefs) {
        highScores.clear();
        highLvls.clear();
        this.prefs = prefs;
        // Adds the high scores and levels in order ascendant
        for(int n = F_Mode.modesCount(), i = 0; i < n; i++) {
            highScores.add(prefs.getInt(HIGH_SCORE + i, 0));
        }
        for(int n = F_Mode.modesCount(), i = 0; i < n; i++) {
            highLvls.add(prefs.getInt(HIGH_LEVEL + i, 0));
        }
        alias = prefs.getString(ALIAS, ALIAS);
        coins = prefs.getInt(COINS, 0);
        gems = prefs.getInt(GEMS, 0);
        lpts = prefs.getInt(LEAGUE_POINTS,0);
        for(int i = styles.length - 1; i >= 0; i--) {
            // if is color 2 of player or block, the default style is 2, else is 1
            styles[i] = prefs.getInt(STYLE + i, i % 3 == 2 ? 2 : 1);
        }
    }

    public void update(GameStats stats) {
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

    public boolean subtractCost(int coins, int gems) {
        if(this.coins >= coins && this.gems >= gems) {
            this.coins -= coins;
            this.gems -= gems;
            prefs.edit().putInt(COINS, coins)
                    .putInt(GEMS, gems).apply();
            return true;
        }
        return false;
    }

    public int highScore(int mode) {
        return highScores.get(mode);
    }

    public int highLvl(int mode) {
        return highLvls.get(mode);
    }

    public int style(int i) {
        // if is skin of player or block, style is 0 or 1, else is 2 (color)
        return A_Style.style(i % 3 == 0 ? (i / 3) : 2, styles[i]);
    }

    public void setStyle(int i, int style) {
        styles[i] = style;
    }

}
