package com.blieve.dodgie.model;

import static java.lang.Math.abs;

public abstract class Obstacle extends GameObject {

    private static float boundX, boundY;

     public static void setBounds(float boundX, float boundY) {
         Obstacle.boundX = boundX;
         Obstacle.boundY = boundY;
     }

    public abstract boolean isColliding(float pX, float pY, float pR);

    public boolean isOut() {
        return abs(x) > boundX || abs(y) > boundY;
    }

}