package com.blieve.dodgie.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.blieve.dodgie.R;
import com.blieve.dodgie.controller.ControlGame;
import com.blieve.dodgie.model.GameStats;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.util.Droid;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.String.valueOf;

public class A_Game extends Droid.BaseActivity {

    private ConstraintLayout layout, options, resume, settings;
    private ImageView pause_img, play_img, back_img;
    private TextView score_txt, level_txt, coins_txt, diamonds_txt;
    private SeekBar seek_sound, seek_music;
    private ImageView[] arrow_imgs;
    private ControlGame control;
    private SharedPreferences prefs;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_game);
        layout = findViewById(R.id.game);
        control = findViewById(R.id.game_canvas);
        arrow_imgs = new ImageView[]{
                findViewById(R.id.game_img_up),
                findViewById(R.id.game_img_left),
                findViewById(R.id.game_img_right)
        };
        pause_img = findViewById(R.id.game_img_pause);
        options = findViewById(R.id.game_options);
        settings = findViewById(R.id.game_settings);
        seek_sound = findViewById(R.id.game_seek_sound);
        seek_music = findViewById(R.id.game_seek_music);
        resume = findViewById(R.id.game_resume);
        score_txt = findViewById(R.id.game_txt_score);
        level_txt = findViewById(R.id.game_txt_lvl);
        coins_txt = findViewById(R.id.game_txt_coins);
        diamonds_txt = findViewById(R.id.game_txt_diamonds);
        play_img = findViewById(R.id.game_img_play);
        back_img = findViewById(R.id.game_img_back);
        init();
    }

    public void onPause() {
        super.onPause();
        pause();
    }

    private void pause() {
        if(control.play()) {
            control.stop();
            pause_img.setVisibility(GONE);
            resume.setVisibility(GONE);
            settings.setVisibility(VISIBLE);
            options.setVisibility(VISIBLE);
        }
    }

    public void init() {
        prefs = getSharedPreferences(A_Options.PREF_CONFIG, MODE_PRIVATE);
        seek_sound.setProgress(prefs.getInt(A_Options.SOUND, 100));
        seek_music.setProgress(prefs.getInt(A_Options.MUSIC, 100));
        boolean vibrate = prefs.getBoolean(A_Options.VIBRATION, true);
        if(vibrate) vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if(control.stats().mode() == 3) {
            arrow_imgs[0].setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.change, null));
        }
        touchListen();
        clickListen();
        seekListen();
        varListener();

        int initLvl = getIntent().getIntExtra(GameStats.INIT_LVL, 1),
                mode = getIntent().getIntExtra(GameStats.MODE, 0);
        control.init();
        GameStats stats = control.stats();
        stats.setInitLvl(initLvl);
        stats.setMode(mode);
        start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void touchListen() {
        for(int i = arrow_imgs.length - 1; i >= 0; i--) {
            final int fI = i;
            arrow_imgs[i].setOnTouchListener((v, ev) -> {
                if(control.play()) {
                    int action = ev.getActionMasked();
                    if(action == ACTION_UP) {
                        arrow_imgs[fI].setScaleX(1);
                        arrow_imgs[fI].setScaleY(1);
                        control.setKey(fI, false);
                    } else if(action == ACTION_DOWN) {
                        arrow_imgs[fI].setScaleX(0.9f);
                        arrow_imgs[fI].setScaleY(0.9f);
                        control.setKey(fI, true);
                    }
                    return true;
                }
                return false;
            });
        }
    }

    private void clickListen() {
        View.OnClickListener clickListener = v -> {
            if (v == pause_img) {
                pause();
            } else if (v == back_img) {
                control.setPlay(false);
                User.get().update(control.stats());
                finish();
            } else if (v == play_img) {
                start();
            }
        };
        pause_img.setOnClickListener(clickListener);
        play_img.setOnClickListener(clickListener);
        back_img.setOnClickListener(clickListener);
    }

    private void seekListen() {
        SeekBar.OnSeekBarChangeListener seek_listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == seek_sound) {
                    prefs.edit().putInt(A_Options.SOUND, progress).apply();
                } else if (seekBar == seek_music) {
                    prefs.edit().putInt(A_Options.MUSIC, progress).apply();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        };
        seek_sound.setOnSeekBarChangeListener(seek_listener);
        seek_music.setOnSeekBarChangeListener(seek_listener);
    }

    private void varListener() {
        control.gameOverListen().setOnCallListener(() -> {
            control.setPlay(false);
            final GameStats stats = control.stats();
            User.get().update(stats);
            final Runnable r = () -> {
                control.stop();
                pause_img.setVisibility(GONE);
                settings.setVisibility(GONE);
                resume.setVisibility(VISIBLE);
                options.setVisibility(VISIBLE);
                /* Resume */
                score_txt.setText(valueOf(stats.score()));
                level_txt.setText(valueOf(stats.lvl()));
                coins_txt.setText(valueOf(stats.coins()));
                diamonds_txt.setText(valueOf(stats.gems()));
                if(vibrator != null) {
                    Droid.vibrate(vibrator, 100);
                }
            };
            layout.post(r);
        });
    }

    public void start() {
        if(control.play()) control.resume();
        else control.start();
        options.setVisibility(GONE);
        pause_img.setVisibility(VISIBLE);
    }

}
