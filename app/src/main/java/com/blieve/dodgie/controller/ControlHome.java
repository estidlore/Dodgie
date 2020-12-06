package com.blieve.dodgie.controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.blieve.dodgie.R;
import com.blieve.dodgie.model.Block;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.util.Update;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.PorterDuff.Mode.MULTIPLY;
import static com.blieve.dodgie.controller.ControlGame.CELLS_X;
import static com.blieve.dodgie.controller.ControlGame.CELLS_Y;
import static com.blieve.dodgie.controller.ControlGame.CELL_W;
import static com.blieve.dodgie.util.Droid.SCREEN_H;
import static com.blieve.dodgie.util.Droid.SCREEN_W;
import static com.blieve.dodgie.util.Droid.bitmapMerge;
import static com.blieve.dodgie.util.Droid.drawableToBitmap;
import static java.lang.Math.random;

public class ControlHome extends View {

    public final ArrayList<Block> blocks = new ArrayList<>();
    public static int blocksN;

    private final float d, speed, spt, originX, originY;
    private final Update update;

    private final Bitmap block_bmp;

    public ControlHome(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        int fps = 30;
        d = CELL_W * 3;
        speed = 2;
        spt = 1f / fps;
        originX = SCREEN_W / 2.0f;
        originY = SCREEN_H / 2.0f;
        Resources res = getResources();
        update = new Update(fps) {
            @Override
            public void tick() {
                calc();
                invalidate();
            }
        };
        User user = User.get();
        Drawable block = res.getDrawable(R.drawable.block),
                blockFace = res.getDrawable(user.style(3));
        block.setColorFilter(user.style(4), MULTIPLY);
        blockFace.setColorFilter(user.style(5), MULTIPLY);

        int size = Block.width();
        block_bmp = bitmapMerge(
                drawableToBitmap(block, size, size),
                drawableToBitmap(blockFace, size, size)
        );
    }

    private void calc() {
        for(int i = blocksN - 1; i >= 0; i--) {
            blocks.get(i).move(spt);
        }
        if(blocks.get(blocksN - 1).dx() > d) {
            addBlocks();
        }
    }

    @Override
    public void onDraw(@NotNull Canvas cvs) {
        cvs.drawColor(BLACK);
        cvs.translate(originX, originY);
        Block b;
        for(int i = 0; i < blocksN; i++) {
            b = blocks.get(i);
            cvs.drawBitmap(block_bmp, b.left(), b.top(), null);
        }
    }

    private void addBlocks() { // left and right
        float x = (CELLS_X + 1) / 2;
        addBlock(-x, (float) ((random() - 0.5) * (CELLS_Y - 1)), speed);
        addBlock(x, (float) ((random() - 0.5) * (CELLS_Y - 1)), -speed);
        // remove the blocks which got out
        while(blocks.get(0).isOut()) {
            blocks.add(blocks.remove(0));
            blocksN--;
        }
    }

    private void addBlock(float x, float y, float vX) {
        if(blocksN == blocks.size()) {
            blocks.add(new Block());
        }
        blocks.get(blocksN).set(x, y, vX, 0);
        blocksN++;
    }

    public void start() {
        blocksN = 0;
        addBlocks();
        update.start();
    }

    public void stop() {
        update.stop();
    }

}
