package com.blieve.dodgie.model;

import static java.lang.Math.abs;

public class Block extends Obstacle {

    private static float w, hW;

    public static void setWidth(float width) {
        w = width;
        hW = w / 2;
    }

    public static int width() {
        return (int) w;
    }

    public int left() {
        return (int) (x - hW);
    }

    public int top() {
        return (int) (y - hW);
    }

    public final boolean isColliding(float pX, float pY, float pR) {
        /*double dx = clamp(gVars.player.x(), left, right) - gVars.player.x(),
                dy = clamp(gVars.player.y(), top, bottom) - gVars.player.y();
        return (dx * dx + dy * dy) < gVars.player.r2();*/
        float dx = abs(pX - x) - hW; // - w / 2
        float dy = abs(pY - y) - hW; // - h / 2
        if (dx > pR || dy > pR) {
            return false;
        }
        if (dx <= 0 || dy <= 0) {
            return true;
        }
        return (dx * dx + dy * dy <= pR * pR);
    }

}