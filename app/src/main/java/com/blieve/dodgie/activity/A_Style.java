package com.blieve.dodgie.activity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

import com.blieve.dodgie.R;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.util.Droid;
import com.blieve.dodgie.util.ImgAdapter;

public class A_Style extends BaseActivity {

    private final static int[][] styles = {
            // FACES
            {
                    R.drawable.player_happy,
                    R.drawable.player_poker,
                    R.drawable.player_sad,
                    R.drawable.player_cyclops,
                    R.drawable.player_pirate,
                    R.drawable.player_angel,
                    R.drawable.player_devil,
                    R.drawable.player_marshmello
            },
            // BLOCK FACES
            {
                    // R.drawable.block,
                    R.drawable.block_hollow,
                    R.drawable.block_spiral,
                    R.drawable.block_chess,
                    R.drawable.block_bomb,
                    R.drawable.block_tnt,
                    R.drawable.block_invader,
                    R.drawable.block_skull,
                    R.drawable.block_nazi,
                    R.drawable.block_creeper,
                    R.drawable.block_squeleton
            },
            // COLORS
            {
                    // primary
                    0x00000000,
                    0xFFFFFFFF,
                    0xFF000000,

                    0xFFFF0000,
                    0xFF00FF00,
                    0xFF0000FF,

                    0xFFFFFF00,
                    0xFF00FFFF,
                    0xFFFF00FF,
                    // secondary
                    0xFFFF8000,
                    0xFF80FF00,

                    0xFF00FF80,
                    0xFF0080FF,

                    0xFF8000FF,
                    0xFFFF0080,
                    // tertiary
                    0xFFFFC000,
                    0xFFC0FF00,

                    0xFF00FFC0,
                    0xFF00C0FF,

                    0xFFC000FF,
                    0xFFFF00C0,
                    // primary dark
                    0xFF303030,

                    0xFFD00000,
                    0xFF00D000,
                    0xFF0000D0,

                    0xFFD0D000,
                    0xFF00D0D0,
                    0xFFD000D0,
                    // secondary dark
                    0xFFD07000,
                    0xFF70D000,

                    0xFF00D070,
                    0xFF0070D0,

                    0xFF7000D0,
                    0xFFD00070,
                    // tertiary dark
                    0xFFD0A000,
                    0xFFA0D000,

                    0xFF00D0A0,
                    0xFF00A0D0,

                    0xFFA000D0,
                    0xFFD000A0,
                    // primary light
                    0xFFD0D0D0,

                    0xFFFF6060,
                    0xFF60FF60,
                    0xFF6060FF,

                    0xFFFFFF80,
                    0xFF80FFFF,
                    0xFFFF80FF,
                    // secondary light
                    0xFFFFB060,
                    0xFFB0FF60,

                    0xFF60FFB0,
                    0xFF60B0FF,

                    0xFFB060FF,
                    0xFFFF60B0,
                    // tertiary light
                    0xFFFFD060,
                    0xFFD0FF60,

                    0xFF60FFD0,
                    0xFF60D0FF,

                    0xFFD060FF,
                    0xFFFF60D0,
                    // primary dark 2
                    0xFF606060,

                    0xFFB00000,
                    0xFF00B000,
                    0xFF0000B0,

                    0xFFB0B000,
                    0xFF00B0B0,
                    0xFFB000B0,
                    // secondary dark 2
                    0xFFB06000,
                    0xFF60B000,

                    0xFF00B060,
                    0xFF0060B0,

                    0xFF6000B0,
                    0xFFB00060,
                    // tertiary dark 2
                    0xFFB08000,
                    0xFF80B000,

                    0xFF00B080,
                    0xFF0080B0,

                    0xFF8000B0,
                    0xFFB00080,
                    // primary light 2
                    0xFFB0B0B0,

                    0xFFFF8080,
                    0xFF80FF80,
                    0xFF8080FF,

                    0xFFFFFFD0,
                    0xFFD0FFFF,
                    0xFFFFD0FF,
                    // secondary light 2
                    0xFFFFD080,
                    0xFFD0FF80,

                    0xFF80FFD0,
                    0xFF80D0FF,

                    0xFFD080FF,
                    0xFFFF80D0,
                    // tertiary light 2
                    0xFFFFF080,
                    0xFFF0FF80,

                    0xFF80FFF0,
                    0xFF80F0FF,

                    0xFFF080FF,
                    0xFFFF80F0,
            }
    };

    public static int style(int style, int i) {
        return styles[style][i];
    }

    private GridView[] gridsStyle;
    private ImgAdapter[] imgAdaptsStyle;
    private ImageView[] imgsSection, imgsPreview;
    private ImageView imgBack;
    private LinearLayout lytPanel;

    private int style, section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_style);

        gridsStyle = new GridView[]{
                findViewById(R.id.style_player),
                findViewById(R.id.style_block),
                findViewById(R.id.style_color)
        };
        imgBack = findViewById(R.id.style_back);
        imgsPreview = new ImageView[]{
                findViewById(R.id.style_preview_player),
                findViewById(R.id.style_preview_block)
        };
        imgsSection = new ImageView[]{
                findViewById(R.id.style_tab_skin),
                findViewById(R.id.style_tab_color1),
                findViewById(R.id.style_tab_color2)
        };
        lytPanel = findViewById(R.id.style_panel);
        init();
    }

    private void init() {
        imgAdaptsStyle = new ImgAdapter[3];
        for(ImageView i : imgsPreview) {
            Droid.UI.setPadding(i, Droid.UI.height(2));
        }
        iniStyles();
        style = -1;
        setStyle(0);
        section = -1;
        setSection(0);
        clickListen();
    }

    private void iniStyles() {
        Resources res = getResources();
        SharedPreferences prefs = getSharedPreferences(User.ALIAS, MODE_PRIVATE);
        User user = User.get();
        int size = Droid.UI.width(6), padding = size / 6, bmpSize = size - padding * 2;
        Droid.UI.setPadding(lytPanel, size / 2);
        // styles
        for(int i = gridsStyle.length - 1; i >= 0; i--) {
            Bitmap[] bmps = new Bitmap[styles[i].length];
            for(int j = 0, n = styles[i].length; j < n; j++) {
                Drawable d = ResourcesCompat.getDrawable(res, i == 1 ? R.drawable.block :
                        R.drawable.player, null); // Either for player or color
                assert d != null;
                if(i == 2) {
                    d.setColorFilter(styles[2][j], PorterDuff.Mode.MULTIPLY);
                    bmps[j] = Droid.Img.drawToBmp(d, bmpSize, bmpSize);
                } else {
                    Drawable d2 = ResourcesCompat.getDrawable(res, style(i, j), null);
                    assert d2 != null;
                    d2.setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);
                    bmps[j] = Droid.Img.bmpMerge(
                            Droid.Img.drawToBmp(d, bmpSize, bmpSize),
                            Droid.Img.drawToBmp(d2, bmpSize, bmpSize)
                    );
                }
            }
            imgAdaptsStyle[i] = new ImgAdapter(this, bmps, size, padding);
            gridsStyle[i].setAdapter(imgAdaptsStyle[i]);
            int finalI = i;
            gridsStyle[i].setOnItemClickListener((parent, view, position, id) -> {
                media.play(Droid.Media.CLICK);
                if(position != imgAdaptsStyle[finalI].getSelection()) {
                    int styleIndex = style * 3 + section;
                    user.setStyle(styleIndex, position);
                    prefs.edit().putInt(User.STYLE + styleIndex, position).apply();
                    imgAdaptsStyle[finalI].setSelection(position);
                    setPreviews();
                }
            });
        }
        setPreviews();
    }

    private void setPreviews() {
        Resources res = getResources();
        User user = User.get();
        int prevSize = Droid.UI.width(7);
        // player
        Drawable skin = ResourcesCompat.getDrawable(res, R.drawable.player, null),
                face = ResourcesCompat.getDrawable(res, user.getStyleDrawable(0), null);
        assert skin != null && face != null;
        skin.setColorFilter(user.getStyleDrawable(1), PorterDuff.Mode.MULTIPLY);
        face.setColorFilter(user.getStyleDrawable(2), PorterDuff.Mode.MULTIPLY);
        imgsPreview[0].setImageBitmap(Droid.Img.bmpMerge(
                Droid.Img.drawToBmp(skin, prevSize, prevSize),
                Droid.Img.drawToBmp(face, prevSize, prevSize)
        ));
        // block
        Drawable block = ResourcesCompat.getDrawable(res, R.drawable.block, null),
                blockFace = ResourcesCompat.getDrawable(res, user.getStyleDrawable(3), null);
        assert block!= null && blockFace != null;
        block.setColorFilter(user.getStyleDrawable(4), PorterDuff.Mode.MULTIPLY);
        blockFace.setColorFilter(user.getStyleDrawable(5), PorterDuff.Mode.MULTIPLY);

        imgsPreview[1].setImageBitmap(Droid.Img.bmpMerge(
                Droid.Img.drawToBmp(block, prevSize, prevSize),
                Droid.Img.drawToBmp(blockFace, prevSize, prevSize)
        ));
    }

    private void setStyle(int style) {
        if(this.style == style) return;
        this.style = style;
        for(int i = imgsPreview.length - 1; i >= 0; i--) {
            imgsPreview[i].setBackgroundColor(i == style ? 0x44000000 : 0);
        }
        showPanel();
    }

    private void setSection(int section) {
        if(this.section ==  section) return;
        this.section = section;
        for(int i = imgsSection.length - 1; i >= 0; i--) {
            imgsSection[i].setBackgroundColor(i == section ? 0x44000000 : 0);
        }
        showPanel();
    }

    private void showPanel() {
        // 0 = face   1 = block   2 = color
        int panel = section < 1 ? style : 2;
        for(int i = gridsStyle.length - 1; i >= 0; i--) {
            gridsStyle[i].setVisibility(i == panel ? View.VISIBLE : View.GONE);
            if(i == panel) {
                int a = style * 3 + section, b = User.get().getStyle(a);
                Log.d("debug", "setSelection" + a + ": " + b);
                imgAdaptsStyle[i].setSelection(b);
            }
        }
    }

    private void clickListen() {
        View.OnClickListener clickListener = v -> {
            if (v == imgBack) {
                media.play(Droid.Media.CLOSE);
                finish();
                return;
            }
            media.play(Droid.Media.CLICK);
            for(int i = imgsPreview.length - 1; i >= 0; i--) {
                if(v == imgsPreview[i]) {
                    setStyle(i);
                    return;
                }
            }
            for(int i = imgsSection.length - 1; i >= 0; i--) {
                if(v == imgsSection[i]) {
                    setSection(i);
                    return;
                }
            }
        };
        imgBack.setOnClickListener(clickListener);
        for(ImageView i : imgsPreview) {
            i.setOnClickListener(clickListener);
        }
        for(ImageView i : imgsSection) {
            i.setOnClickListener(clickListener);
        }
    }

}
