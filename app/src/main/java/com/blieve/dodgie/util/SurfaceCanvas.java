package com.blieve.dodgie.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static java.lang.System.nanoTime;
import static java.lang.Thread.sleep;

public abstract class SurfaceCanvas extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Thread thread;
    private boolean ready = false;
    private boolean drawing = false;
    private static final int MS = 1000 / 60;
    private final Context ctx;

    protected final float originX, originY;
    protected final Resources res;

    public SurfaceCanvas(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.ctx = ctx;
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        originX = Droid.UI.SCREEN_W / 2.0f;
        originY = Droid.UI.SCREEN_H / 2.0f;
        res = getResources();
    }

    public abstract void render(Canvas c);

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // if (width == 0 || height == 0) return;
        // resize your UI
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        this.holder = holder;
        if (thread != null){
            drawing = false;
            try{
                thread.join();
            } catch (InterruptedException ignored) { }
        }
        ready = true;
        start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        stop();
        holder.getSurface().release();
        this.holder = null;
        ready = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    public final void stop(){
        if (thread == null) return;
        drawing = false;
        while (true){
            try{
                thread.join(5000);
                break;
            } catch (Exception ignored) { }
        }
        thread = null;
    }

    public final void start(){
        if (ready && thread == null){
            thread = new Thread(this, "surface");
            drawing = true;
            thread.start();
        }
    }

    @Override
    public void run() {
        long time, dt;
        /*if (android.os.Build.BRAND.equalsIgnoreCase("google")
                && android.os.Build.MANUFACTURER.equalsIgnoreCase("asus")
                && android.os.Build.MODEL.equalsIgnoreCase("Nexus 7")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) { }
        }*/
        while (drawing) {
            if (holder == null) return;
            time = nanoTime();
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                try {
                    synchronized (holder) {
                        render(canvas);
                    }
                } finally {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            dt = (nanoTime() - time) / 1000000;
            if (dt < MS){
                try {
                    sleep(MS - dt);
                } catch (InterruptedException ignored) { }
            }
        }
    }

}