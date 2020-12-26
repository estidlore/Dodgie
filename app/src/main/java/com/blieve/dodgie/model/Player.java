package com.blieve.dodgie.model;

import com.blieve.dodgie.controller.ControlGame;
import com.blieve.dodgie.util.Droid;

import static java.lang.Math.signum;
import static java.lang.Math.sqrt;

public class Player extends GameObject {

    private final float aXTime, aYTime;
    private float r, r2, maxX, maxY, maxVX, maxVY, aX, aY;

    public Player() {
        aXTime = 1500;
        aYTime = 3500;
    }

    public void set() {
        super.set(0, 0, 0, 0);
    }

    public final void setR(float r) {
        this.r = r;
        r2 = r * r;
        maxX = Droid.UI.SCREEN_W / 2f - r;
        maxY = Droid.UI.SCREEN_H / 2f - r;
    }

    public void setMaxVX(float v) {
        maxVX = v * ControlGame.CELL_W;
        aX = (float) sqrt(1 / aXTime) * maxVX;
    }

    public void setMaxVY(float v) {
        maxVY = v * ControlGame.CELL_W;
        aY = (float) sqrt(1 / aYTime) * maxVY;
    }

    public float r() {
        return r;
    }

    public void move(int aX, int aY, double dt) {
        if(aX == 0) {
            vX -= signum(vX) * this.aX / 2f;
        } else {
            vX = clamp(vX + this.aX * aX, -maxVX, maxVX);
        }
        vY = clamp(vY + this.aY * aY, -maxVY, maxVY);
        move(dt);
    }

    public boolean isOut(){
        float oldX = x, oldY = y;
        x = clamp(x, -maxX, maxX);
        y = clamp(y, -maxY, maxY);

        return(oldX != x || oldY != y);
    }

}