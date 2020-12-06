package com.blieve.dodgie.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;

import static com.blieve.dodgie.util.Droid.SCREEN_H;
import static com.blieve.dodgie.util.Droid.SCREEN_W;

public class ViewCanvas extends View {

    protected final float originX, originY;
    protected final Resources res;
    private final Update update;

    public ViewCanvas(Context ctx, AttributeSet attrs, int fps) {
        super(ctx, attrs);
        originX = SCREEN_W / 2.0f;
        originY = SCREEN_H / 2.0f;
        res = getResources();
        update = new Update(fps) {
            @Override
            public void tick() {
                invalidate();
            }
        };
        update.start();
    }

    public void stop() { update.stop(); }

    public void start() { update.start(); }

}
