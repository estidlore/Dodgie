package com.blieve.dodgie.model;

import androidx.annotation.CallSuper;

import com.blieve.dodgie.controller.ControlGame;

import static com.blieve.dodgie.controller.ControlGame.CELL_W;
import static java.lang.Math.abs;

public abstract class GameObject {

    private float xi, yi;
    float x, y, vX, vY;

     public final void set(float xi, float yi, float vX, float vY) {
         this.xi = xi * CELL_W;
         this.yi = yi * CELL_W;
         this.x = this.xi;
         this.y = this.yi;
         this.vX = vX * CELL_W;
         this.vY = vY * CELL_W;
     }

    @CallSuper
    public void move(double dt){
        x += vX * dt;
        y += vY * dt;
    }

    public abstract boolean isOut();

    public final float x() { return x; }

    public final float y() { return y; }

    public final float vX() { return vX; }

    public final float vY() { return vY; }

    public float dx() { return abs(x - xi); }

    // public float dy() { return abs(y - yi); }

    float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

}
