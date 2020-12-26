package com.blieve.dodgie.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.blieve.dodgie.R;
import com.blieve.dodgie.model.Block;
import com.blieve.dodgie.model.GameStats;
import com.blieve.dodgie.model.Player;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.util.Droid;
import com.blieve.dodgie.util.Update;

import java.util.ArrayList;

public class ControlGame extends View {

    public static float CELL_W, CELLS_X, CELLS_Y;

    private final ArrayList<Block> blocks;
    private final Droid.Listen gameOverListen;
    private final GameStats stats;
    private final Player player;
    private final Update update;

    private final boolean[] keys;
    private final float originX, originY, spt, x, y;
    private boolean play, rotate;
    private int blocksN, aX, aY, highScore;
    private float speed, d;

    private final Bitmap skin_bmp, face_bmp, block_bmp;
    private final Paint score_pnt, lvl_pnt;
    private float scoreX, highScoreY, scoreY, prgY, prg;

    public ControlGame(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        int fps = 60;
        spt = 1f / fps;
        Resources res = getResources();
        update = new Update(fps) {
            @Override
            public void tick() {
                if (play) {
                    calc();
                    invalidate();
                }
            }
        };

        stats = new GameStats();
        player = new Player();
        keys = new boolean[3];
        blocks = new ArrayList<>();
        gameOverListen = new Droid.Listen();

        originX = Droid.UI.SCREEN_W / 2.0f;
        originY = Droid.UI.SCREEN_H / 2.0f;
        x = (CELLS_X + 1) / 2;
        y = (CELLS_Y + 1) / 2;

        score_pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        lvl_pnt = new Paint(Paint.ANTI_ALIAS_FLAG);

        player.setR(CELL_W / 3);
        int diameter = (int) player.r() * 2,
                size = Block.width();
        // User drawable
        User user = User.get();
        Drawable skin = ResourcesCompat.getDrawable(res, R.drawable.player, null),
                face = ResourcesCompat.getDrawable(res, user.getStyleDrawable(0), null);
        assert skin != null && face != null;
        skin.setColorFilter(user.getStyleDrawable(1), PorterDuff.Mode.MULTIPLY);
        face.setColorFilter(user.getStyleDrawable(2), PorterDuff.Mode.MULTIPLY);
        skin_bmp = Droid.Img.drawToBmp(skin, diameter, diameter);
        face_bmp = Droid.Img.drawToBmp(face, diameter, diameter);
        // Block drawable
        Drawable block = ResourcesCompat.getDrawable(res, R.drawable.block, null),
                blockFace = ResourcesCompat.getDrawable(res, user.getStyleDrawable(3), null);
        assert block != null && blockFace != null;
        block.setColorFilter(user.getStyleDrawable(4), PorterDuff.Mode.MULTIPLY);
        blockFace.setColorFilter(user.getStyleDrawable(5), PorterDuff.Mode.MULTIPLY);
        block_bmp = Droid.Img.bmpMerge(
                Droid.Img.drawToBmp(block, size, size),
                Droid.Img.drawToBmp(blockFace, size, size)
        );
    }

    public void init(int initLvl, int mode) {
        stats.setInitLvl(initLvl);
        stats.setMode(mode);
        rotate = stats.mode() == 2; // overturned
        d = CELL_W * (stats.mode() == 1 ? 4 : 3); // party

        float textSize = CELL_W / 2.5f,
                stroke = CELL_W / 10;
        scoreX = textSize / 2 - originX;
        highScoreY = textSize * 1.5f - originY;
        scoreY = textSize * 3f - originY;
        prgY = originY - stroke;
        prg = originX / GameStats.PTS_PER_LVL / 2;
        play = false;

        score_pnt.setColor(Color.WHITE);
        score_pnt.setTextSize(textSize);
        score_pnt.setTextAlign(Paint.Align.LEFT);
        lvl_pnt.setColor(Color.WHITE);
        lvl_pnt.setTextSize(textSize);
        lvl_pnt.setTextAlign(Paint.Align.CENTER);
        lvl_pnt.setStrokeWidth(stroke);
    }

    public final void start() {
        stats.reset();
        player.set();
        blocksN = 0;
        play = true;
        highScore = User.get().highScore(stats.mode());
        setSpeed();
        addBlocks();
        aX = 0;
        aY = 1;
        resume();
    }

    public final void resume() {
        for(int i = keys.length - 1; i >= 0; i--) {
            setKey(i, false);
        }
        update.start();
    }

    public final void stop() {
        update.stop();
    }

    private void calc() {
        for (int i = blocksN - 1; i >= 0; i--) {
            if (blocks.get(i).isColliding(player.x(), player.y(), player.r())) {
                gameOver();
                return;
            }
            blocks.get(i).move(spt);
        }
        player.move(aX, aY, spt);
        if (player.isOut()) {
            gameOver();
            return;
        }
        if (blocks.get(blocksN - 1).dx() > d) {
            addBlocks();
        }
    }

    @Override
    public void onDraw(Canvas cvs) {
        cvs.translate(originX, originY);
        if(rotate) cvs.rotate(180);
        cvs.drawColor(Color.BLACK);
        // draw player
        Player p = player;
        int dx = mV(p.vX()),
                dy = mV(p.vY()),
                left = (int)(p.x() - p.r()),
                top = (int)(p.y() - p.r());
        cvs.drawBitmap(skin_bmp, left, top, null);
        cvs.drawBitmap(face_bmp, left + dx, top + dy, null);
        // draw blocks
        Block b;
        for(int i = blocksN - 1; i >= 0; i--) {
            b = blocks.get(i);
            cvs.drawBitmap(block_bmp, b.left(), b.top(), null);
        }
        if(rotate) cvs.rotate(-180);
        // draw text
        cvs.drawText(String.valueOf(highScore), scoreX, highScoreY, score_pnt);
        cvs.drawText(String.valueOf(stats.score()), scoreX, scoreY, score_pnt);
        cvs.drawText("LVL " + stats.lvl(),0, originY * 0.9f, lvl_pnt);
        // progress bar
        float prgX = prg * (stats.points() % GameStats.PTS_PER_LVL);
        cvs.drawLine(-prgX, prgY, prgX, prgY, lvl_pnt);
    }

    public final void addBlock(float x, float y, float vX, float vY) {
        if(blocksN == blocks.size()) {
            blocks.add(new Block());
        }
        blocks.get(blocksN).set(x, y, vX, vY);
        blocksN++;
    }

    private void addBlocks() {
        if(stats.mode() == 1) { // If mode is party
            addBlock((float) ((Math.random() - 0.5) * (CELLS_X - 1)), -y, 0, speed);
            addBlock((float) ((Math.random() - 0.5) * (CELLS_X - 1)), y, 0, -speed);
            // if(addPoints()) setSpeed();
        }
        addBlock(-x, (float) ((Math.random() - 0.5) * (CELLS_Y - 1)), speed,0);
        addBlock(x, (float) ((Math.random() - 0.5) * (CELLS_Y - 1)), -speed,0);
        if(addPoints()) setSpeed();
    }

    private void removeBlocks() {
        while(blocks.get(0).isOut()) {
            blocksN--;
            blocks.add(blocks.remove(0));
        }
    }

    private void setSpeed() {
        float SPEED_INIT = 2.5f;
        speed = (float) (SPEED_INIT + Math.pow(stats.lvl() - 1, 0.7) / 10);
        player.setMaxVX(speed * 2f);
        player.setMaxVY(speed * 4f);
    }

    /**
     * Returns true if the level increments
     * */
    public final boolean addPoints() {
        stats.addPoints(1);
        stats.addScore(stats.lvl());
        stats.addCoins(stats.lvl());
        removeBlocks();
        if(stats.points() % GameStats.PTS_PER_LVL == 0) {
            stats.addLvl(1);
            stats.addGems(stats.lvl());
            return true;
        }
        return false;
    }

    private int mV(double vel) { // (pow n of |x|) / m * sign of x
        return (int)(Math.pow(Math.abs(vel), 0.5) / 2.5 * Math.signum((int)vel));
    }

    public final void setKey(int key, boolean pressed) {
        if(stats.mode() == 3 && key == 0) {
            if(!pressed) return;
            keys[key] = !keys[key];
        } else if(pressed != keys[key]) {
            keys[key] = pressed;
        } else return;
        aX = keys[1] ? -1 : (keys[2] ? 1 : 0);
        aY = keys[0] ? -1 : 1;
    }

    private void gameOver() {
        play = false;
        User.get().update(stats);
        gameOverListen.call();
    }

    public Droid.Listen gameOverListen() {
        return gameOverListen;
    }

    public GameStats stats() {
        return stats;
    }

    public boolean play() {
        return play;
    }

}