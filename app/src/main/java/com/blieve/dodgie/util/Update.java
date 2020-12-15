package com.blieve.dodgie.util;

import static java.lang.System.nanoTime;
import static java.lang.Thread.sleep;

/**
 * Utility for threads which runs in loop a function
 *
 * @author estidlore
 */
public abstract class Update implements Runnable {

    private Thread thread;
    private boolean running;
    private int MS_PER_TICK;

    /**
     * Create a new instance of this class
     *
     * @param tps The number of times per second which the loop which function
     * is called
     */
    public Update(int tps) {
        setTPS(tps);
        running = false;
    }

    @Override
    public final void run() {
        long time, dt;
        while (running) {
            time = nanoTime();
            tick();
            dt = (nanoTime() - time) / 1000000;
            if (dt < MS_PER_TICK) {
                try {
                    sleep(MS_PER_TICK - dt);
                } catch (InterruptedException ignored) { }
            }
        }
    }

    /**
     * Loop function, called every tick
     */
    public abstract void tick();

    /**
     * Start a new thread
     */
    public final void start(String name) {
        if (thread == null) {
            running = true;
            thread = new Thread(this, name);
            thread.start();
        }
    }

    public final void start() {
        start(getClass().getSimpleName());
    }

    /**
     * Stop the thread
     */
    public final void stop() {
        if (thread == null) return;
        running = false;
        while (thread != null) {
            try {
                thread.join(/*5000*/);
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *  Returns whether the thread is running or not
     **/
    public final boolean isRunning() {
        return running;
    }

    /**
     * Set the number of ticks per second which the loop function is called
     *
     * @param tps is the number of ticks per second to set
     */
    public final void setTPS(int tps) {
        MS_PER_TICK = 1000 / tps;
    }

}
