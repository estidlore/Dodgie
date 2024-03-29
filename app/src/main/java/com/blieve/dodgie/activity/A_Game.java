package com.blieve.dodgie.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
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

public class A_Game extends BaseActivity {

    private ImageView[] arrow_imgs;
    private ConstraintLayout layout, options, resume, settings;
    private ImageView pause_img, play_img, back_img;
    private SeekBar seek_sound, seek_music;
    private TextView score_txt, level_txt, coins_txt, diamonds_txt;

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

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    private void pause() {
        if(control.play()) {
            control.stop();
            pause_img.setVisibility(View.GONE);
            resume.setVisibility(View.GONE);
            settings.setVisibility(View.VISIBLE);
            options.setVisibility(View.VISIBLE);
        }
    }

    public void init() {
        int initLvl = getIntent().getIntExtra(GameStats.INIT_LVL, 1),
            mode = getIntent().getIntExtra(GameStats.MODE, 0);
        control.init(initLvl, mode);

        prefs = getSharedPreferences(A_Options.PREF_CONFIG, MODE_PRIVATE);
        seek_music.setProgress(media.getMusicVolume());
        seek_sound.setProgress(media.getFxVolume());
        boolean vibrate = prefs.getBoolean(A_Options.VIBRATION, true);
        if(vibrate) vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if(control.stats().mode() == 3) {
            arrow_imgs[0].setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable._game_change, null));
        }
        touchListen();
        clickListen();
        seekListen();
        callListen();
        play();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void touchListen() {
        for(int i = arrow_imgs.length - 1; i >= 0; i--) {
            final int fI = i;
            arrow_imgs[i].setOnTouchListener((v, ev) -> {
                if(control.play()) {
                    int action = ev.getActionMasked();
                    if(action == MotionEvent.ACTION_UP) {
                        arrow_imgs[fI].setScaleX(1);
                        arrow_imgs[fI].setScaleY(1);
                        control.setKey(fI, false);
                    } else if(action == MotionEvent.ACTION_DOWN) {
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
                media.play(Droid.Media.CLICK);
                pause();
            } else if (v == back_img) {
                media.play(Droid.Media.CLOSE);
                if(control.play()) User.get().update(control.stats());
                media.setMusicPanning(0);
                finish();
            } else if (v == play_img) {
                if(!control.play()) {
                    GameStats stats = control.stats();
                    int initLvl = stats.initLvl(), costCoins, costDiamonds;
                    do {
                        initLvl--;
                        costCoins = initLvl * GameStats.PTS_PER_LVL;
                        costDiamonds = initLvl;
                    } while (!User.get().subtractCost(costCoins, costDiamonds));
                    stats.setInitLvl(initLvl + 1);
                }
                play();
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
                if (seekBar == seek_music) {
                    media.setMusicVolume(progress);
                    prefs.edit().putInt(Droid.Media.MUSIC, progress).apply();
                } else if (seekBar == seek_sound) {
                    media.setFxVolume(progress);
                    prefs.edit().putInt(Droid.Media.SOUND, progress).apply();
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

    private void callListen() {
        control.gameOverListen().setOnCallListener(() -> {
            if (vibrator != null) Droid.vibrate(vibrator, 100);
            runOnUiThread(() -> {
                Log.d("debug", "runOnUiThread() start");
                control.stop();
                pause_img.setVisibility(View.GONE);
                settings.setVisibility(View.GONE);
                resume.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
                /* Resume */
                GameStats stats = control.stats();
                score_txt.setText(String.valueOf(stats.score()));
                level_txt.setText(String.valueOf(stats.lvl()));
                coins_txt.setText(String.valueOf(stats.coins()));
                diamonds_txt.setText(String.valueOf(stats.gems()));
                Log.d("debug", "runOnUiThread() end");
            });
        });
    }

    public void play() {
        media.play(Droid.Media.FADE);
        if(control.play()) control.resume();
        else control.start();
        options.setVisibility(View.GONE);
        pause_img.setVisibility(View.VISIBLE);
    }

}
