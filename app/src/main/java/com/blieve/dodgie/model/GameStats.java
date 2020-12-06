package com.blieve.dodgie.model;

public class GameStats {

    public final static String
            INIT_LVL = "initLvl",
            MODE = "mode";
    public final static int
            PTS_PER_LVL = 50;

    private int coins, gems, initLvl, lvl, mode, points, score;

    public void reset() {
        coins = 0;
        gems = 0;
        lvl = initLvl;
        points = 0;
        score = 0;
    }

    public int coins() {
        return coins;
    }

    public int gems() {
        return gems;
    }

    public void setInitLvl(int val) {
        initLvl = val;
    }

    public int lvl() {
        return lvl;
    }

    public int mode() {
        return mode;
    }

    public void setMode(int val) {
        mode = val;
    }

    public int points() {
        return points;
    }

    public int score() {
        return score;
    }

    public void addCoins(int val) {
        coins += val;
    }

    public void addGems(int val) {
        gems += val;
    }

    public void addLvl(int val) {
        lvl += val;
    }

    public void addPoints(int val) {
        points += val;
    }

    public void addScore(int val) {
        score += val;
    }

}
