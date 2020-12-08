package com.blieve.dodgie.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.blieve.dodgie.R;
import com.blieve.dodgie.model.Block;
import com.blieve.dodgie.model.GameStats;
import com.blieve.dodgie.model.Obstacle;
import com.blieve.dodgie.model.Player;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.util.Droid;
import com.blieve.dodgie.util.Update;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Align.CENTER;
import static android.graphics.Paint.Align.LEFT;
import static android.graphics.PorterDuff.Mode.MULTIPLY;
import static com.blieve.dodgie.model.GameStats.PTS_PER_LVL;
import static com.blieve.dodgie.util.Droid.SCREEN_H;
import static com.blieve.dodgie.util.Droid.SCREEN_W;
import static java.lang.Integer.signum;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.String.valueOf;

public class ControlGame extends View {

    public static float CELL_W, CELLS_X, CELLS_Y;

    private final ArrayList<Block> blocks;
    private final GameStats stats;
    private final Droid.Listen gameOverListen;
    private final Player player;
    private final Update update;
    private final float d, originX, originY, spt, x, y;
    private final boolean[] keys;
    private float speed;
    private int blocksN, aX, aY, highScore;
    private boolean play;

    private final Bitmap skin_bmp, face_bmp, block_bmp;
    private final Paint score_pnt, lvl_pnt;
    private final boolean rotate;
    private float scoreX, highScoreY, scoreY, prgY, prg;

    public ControlGame(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        int fps = 60;
        spt = 1f / fps;
        Resources res = getResources();
        update = new Update(fps) {
            @Override
            public void tick() {
                calc();
                invalidate();
            }
        };

        stats = new GameStats();
        player = new Player();
        keys = new boolean[3];
        blocks = new ArrayList<>();
        gameOverListen = new Droid.Listen();

        d = CELL_W * (stats.mode() == 2 ? 4 : 3);
        originX = SCREEN_W / 2.0f;
        originY = SCREEN_H / 2.0f;
        x = (CELLS_X + 1) / 2;
        y = (CELLS_Y + 1) / 2;

        score_pnt = new Paint(ANTI_ALIAS_FLAG);
        lvl_pnt = new Paint(ANTI_ALIAS_FLAG);

        player.setR(CELL_W / 3);
        int diameter = (int) player.r() * 2,
                size = Block.width();
        // User drawable
        User user = User.get();
        Drawable skin = res.getDrawable(R.drawable.circle),
                face = res.getDrawable(user.style(0));
        skin.setColorFilter(user.style(1), MULTIPLY);
        face.setColorFilter(user.style(2), MULTIPLY);
        skin_bmp = Droid.Img.drawToBmp(skin, diameter, diameter);
        face_bmp = Droid.Img.drawToBmp(face, diameter, diameter);
        // Block drawable
        Drawable block = res.getDrawable(R.drawable.block),
                blockFace = res.getDrawable(user.style(3));
        block.setColorFilter(user.style(4), MULTIPLY);
        blockFace.setColorFilter(user.style(5), MULTIPLY);
        block_bmp = Droid.Img.bmpMerge(
                Droid.Img.drawToBmp(block, size, size),
                Droid.Img.drawToBmp(blockFace, size, size)
        );

        rotate = stats.mode() == 1;
    }

    public void init() {
        play = false;
        float textSize = CELL_W / 2.5f,
                stroke = CELL_W / 10;
        scoreX = textSize / 2 - originX;
        highScoreY = textSize * 1.5f - originY;
        scoreY = textSize * 3f - originY;
        prgY = originY - stroke;
        prg = originX / PTS_PER_LVL / 2;

        score_pnt.setColor(WHITE);
        score_pnt.setTextSize(textSize);
        score_pnt.setTextAlign(LEFT);
        lvl_pnt.setColor(WHITE);
        lvl_pnt.setTextSize(textSize);
        lvl_pnt.setTextAlign(CENTER);
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
        for(int i = blocksN - 1; i >= 0; i--) {
            if(blocks.get(i).isColliding(player.x(), player.y(), player.r())) {
                gameOverListen.call();
                return;
            }
            blocks.get(i).move(spt);
        }
        player.move(aX, aY, spt);
        if(player.isOut()) {
            gameOverListen.call();
            return;
        }
        if(blocks.get(blocksN - 1).dx() > d) {
            addBlocks();
        }
    }

    @Override
    public void onDraw(@NotNull Canvas cvs) {
        cvs.translate(originX, originY);
        if(rotate) {
            cvs.rotate(180);
        }
        cvs.drawColor(BLACK);
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
        if(rotate) {
            cvs.rotate(-180);
        }
        // draw text
        cvs.drawText(valueOf(highScore), scoreX, highScoreY, score_pnt);
        cvs.drawText(valueOf(stats.score()), scoreX, scoreY, score_pnt);
        cvs.drawText("LVL " + stats.lvl(),0, originY * 0.9f, lvl_pnt);
        // progress bar
        float prgX = prg * (stats.points() % PTS_PER_LVL);
        cvs.drawLine(-prgX, prgY, prgX, prgY, lvl_pnt);
    }

    public final void addBlock(float x, float y, float vX, float vY) {
        if(blocksN == blocks.size()) {
            blocks.add(new Block());
        }
        blocks.get(blocksN).set(x, y, vX, vY);
        blocksN++;
    }

    public final boolean addPoints() {
        stats.addPoints(1);
        stats.addScore(stats.lvl());
        stats.addCoins(stats.lvl());
        removeBlocks();
        if(stats.points() % PTS_PER_LVL == 0) {
            stats.addLvl(1);
            stats.addGems(stats.lvl());
            return true;
        }
        return false;
    }

    private void removeBlocks() {
        while(blocks.get(0).isOut()) {
            blocksN--;
            blocks.add(blocks.remove(0));
        }
    }

    private void setSpeed() {
        float SPEED_INIT = 2.5f;
        speed = (float) (SPEED_INIT + pow(stats.lvl() - 1, 0.7) / 10);
        player.setMaxVX(speed * 2f);
        player.setMaxVY(speed * 4f);
    }

    private void addBlocks() {
        if(stats.mode() == 2) {
            addBlock((float) ((random() - 0.5) * (CELLS_X - 1)), -y, 0, speed);
            addBlock((float) ((random() - 0.5) * (CELLS_X - 1)), y, 0, -speed);
            if(addPoints()) {
                setSpeed();
            }
        }
        addBlock(-x, (float) ((random() - 0.5) * (CELLS_Y - 1)), speed,0);
        addBlock(x, (float) ((random() - 0.5) * (CELLS_Y - 1)), -speed,0);
        if(addPoints()) {
            setSpeed();
        }
    }

    private int mV(double vel) { // (pow n of |x|) / m * sign of x
        return (int)(pow(abs(vel), 0.5) / 2.5 * signum((int)vel));
    }

    public final void setKey(int key, boolean pressed) {
        if(stats.mode() == 3 && key == 0) {
            if(!pressed) {
                return;
            }
            keys[key] = !keys[key];
        } else if(pressed != keys[key]) {
            keys[key] = pressed;
        } else {
            return;
        }
        aX = keys[1] ? -1 : (keys[2] ? 1 : 0);
        aY = keys[0] ? -1 : 1;
    }

    public Droid.Listen gameOverListen() {
        return gameOverListen;
    }

    public boolean play() {
        return play;
    }

    public void setPlay(boolean val) {
        this.play = val;
    }

    public GameStats stats() {
        return stats;
    }

}