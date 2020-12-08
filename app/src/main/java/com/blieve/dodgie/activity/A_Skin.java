package com.blieve.dodgie.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blieve.dodgie.R;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.util.Droid;

import static android.graphics.PorterDuff.Mode.MULTIPLY;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.blieve.dodgie.model.User.ALIAS;
import static com.blieve.dodgie.model.User.STYLE;

public class A_Skin extends Droid.BaseActivity {

    private final static int[][] styles = {
            // FACES
            {
                    R.drawable.face_happy,
                    R.drawable.face_poker,
                    R.drawable.face_sad,
                    R.drawable.face_cyclops,
                    R.drawable.face_pirate,
                    R.drawable.face_angel,
                    R.drawable.face_devil,
                    R.drawable.face_marshmello
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

    private LinearLayout skin_styles;
    private LinearLayout[] style_lyts;
    private ImageView back_img, skin_img, block_img;
    private ImageView[] section_imgs, style_imgs;
    private int style, section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_skin);

        back_img = findViewById(R.id.skin_back);
        skin_styles = findViewById(R.id.skin_styles);
        style_imgs = new ImageView[]{
                findViewById(R.id.skin_skin_img),
                findViewById(R.id.skin_block_img)
        };
        section_imgs = new ImageView[]{
                findViewById(R.id.skin_style_img),
                findViewById(R.id.skin_one_img),
                findViewById(R.id.skin_two_img)
        };
        style_lyts = new LinearLayout[]{
                findViewById(R.id.skin_faces),
                findViewById(R.id.skin_blocks),
                findViewById(R.id.skin_colors)
        };
        skin_img = findViewById(R.id.skin_skin_preview);
        block_img = findViewById(R.id.skin_block_preview);
        init();
    }

    private void init() {
        style = -1;
        section = -1;
        setStyle(0);
        setSection(0);
        clickListen();
        skin_styles.post(this::iniStyles);
    }

    private void iniStyles() {
        // vars
        final Resources res = getResources();
        final User user = User.get();
        int n = 12, count, index,
                lytPadding = skin_styles.getWidth() / (n * 2),
                size = (skin_styles.getWidth() - lytPadding * 2) / n,
                padding = size / 8;
        ViewGroup.LayoutParams paramsLyt = new ViewGroup.LayoutParams(size * n, size),
                paramsImg = new ViewGroup.LayoutParams(size, size);
        size -= padding * 2;
        final SharedPreferences prefs = getSharedPreferences(ALIAS, MODE_PRIVATE);
        // styles
        skin_styles.setPadding(lytPadding, lytPadding, lytPadding, lytPadding);
        for(int i = style_lyts.length - 1; i >= 0; i--) {
            count = index = 0;
            LinearLayout l = new LinearLayout(this);
            l.setLayoutParams(paramsLyt);

            while(count < styles[i].length) {
                ImageView img = new ImageView(this);
                img.setLayoutParams(paramsImg);
                img.setPadding(padding, padding, padding, padding);
                Drawable d = res.getDrawable(i == 1 ? R.drawable.block : R.drawable.circle);
                if(i == 2) {
                    d.setColorFilter(styles[2][count], MULTIPLY);
                    img.setImageBitmap(Droid.Img.drawToBmp(d, size, size));
                } else {
                    Drawable d2 = res.getDrawable(style(i, count));
                    d2.setColorFilter(0xFF000000, MULTIPLY);
                    img.setImageBitmap(Droid.Img.bmpMerge(
                            Droid.Img.drawToBmp(d, size, size),
                            Droid.Img.drawToBmp(d2, size, size)
                    ));
                }

                final int finalCount = count, finalI = i;
                img.setOnClickListener(v -> {
                    int st = style * 3 + section;
                    user.setStyle(st, finalCount);
                    prefs.edit().putInt(STYLE + st, finalCount).apply();
                    setPreviews();
                });
                l.addView(img);
                if(++index == n) {
                    style_lyts[i].addView(l);
                    l = new LinearLayout(this);
                    index = 0;
                }
                count++;
            }
            style_lyts[i].addView(l);
        }
        setPreviews();
    }

    private void setPreviews() {
        Resources res = getResources();
        User user = User.get();
        int prevSize = Droid.width(7);
        Drawable skin = res.getDrawable(R.drawable.circle),
                face = res.getDrawable(user.style(0));
        skin.setColorFilter(user.style(1), MULTIPLY);
        face.setColorFilter(user.style(2), MULTIPLY);
        skin_img.setImageBitmap(Droid.Img.bmpMerge(
                Droid.Img.drawToBmp(skin, prevSize, prevSize),
                Droid.Img.drawToBmp(face, prevSize, prevSize)
        ));
        Drawable block = res.getDrawable(R.drawable.block),
                blockFace = res.getDrawable(user.style(3));
        block.setColorFilter(user.style(4), MULTIPLY);
        blockFace.setColorFilter(user.style(5), MULTIPLY);

        block_img.setImageBitmap(Droid.Img.bmpMerge(
                Droid.Img.drawToBmp(block, prevSize, prevSize),
                Droid.Img.drawToBmp(blockFace, prevSize, prevSize)
        ));
    }

    private void setStyle(int style) {
        if(this.style == style) return;
        this.style = style;
        for(int i = style_imgs.length - 1; i >= 0; i--) {
            style_imgs[i].setBackgroundColor(i == style ? 0x44000000 : 0);
        }
        if(section == 0) showSection();
    }

    private void setSection(int section) {
        if(this.section ==  section) return;
        this.section = section;
        for(int i = section_imgs.length - 1; i >= 0; i--) {
            section_imgs[i].setBackgroundColor(i == section ? 0x44000000 : 0);
        }
        showSection();
    }

    private void showSection() {
        // 0 = face   1 = block   2 = color
        int section = this.section < 1 ? style : 2;
        for(int i = style_lyts.length - 1; i >= 0; i--) {
            style_lyts[i].setVisibility(i == section ? VISIBLE : GONE);
        }
    }

    private void clickListen() {
        View.OnClickListener clickListener = v -> {
            if (v == back_img) {
                finish();
                return;
            }
            for(int i = style_imgs.length - 1; i >= 0; i--) {
                if(v == style_imgs[i]) {
                    setStyle(i);
                    return;
                }
            }
            for(int i = section_imgs.length - 1; i >= 0; i--) {
                if(v == section_imgs[i]) {
                    setSection(i);
                    return;
                }
            }
        };
        back_img.setOnClickListener(clickListener);
        for(int i = style_imgs.length - 1; i >= 0; i--) {
            style_imgs[i].setOnClickListener(clickListener);
        }
        for(int i = section_imgs.length - 1; i >= 0; i--) {
            section_imgs[i].setOnClickListener(clickListener);
        }
    }

}
