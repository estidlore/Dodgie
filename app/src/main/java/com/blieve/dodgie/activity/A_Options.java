package com.blieve.dodgie.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class A_Options extends BaseActivity {

    public final static String
            PREF_CONFIG = "cnfg",
            VIBRATION = "vibr";
    private final String
            SIGN_IN = "signIn",
            SIGN_UP = "signUp";

    // private Button btnSignIn, btnSignUp;
    private CheckBox checkVibration;
    private ConstraintLayout pop, settings/*, sign*/;
    private ImageView img_back, imgClose;
    private SeekBar seekMusic, seekSound;
    private Spinner spinLang;

    private SharedPreferences prefs;
    private Droid.Lang lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_options);

        // btnSignIn = findViewById(R.id.opts_signIn);
        // btnSignUp = findViewById(R.id.opts_signUp);
        checkVibration = findViewById(R.id.opts_check_vibration);
        pop = findViewById(R.id.opts_pop);
        settings = findViewById(R.id.settings);
        // sign = findViewById(R.id.opts_sign);
        img_back = findViewById(R.id.opts_back);
        imgClose = findViewById(R.id.opts_close);
        seekSound = findViewById(R.id.opts_seek_sound);
        seekMusic = findViewById(R.id.opts_seek_music);
        spinLang = findViewById(R.id.opts_spin_lang);

        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hidePop();
    }

    private  void init() {
        prefs = getSharedPreferences(PREF_CONFIG, MODE_PRIVATE);
        seekMusic.setProgress(media.getMusicVolume());
        seekSound.setProgress(media.getFxVolume());
        spinLang.setSelection(Droid.Lang.getLang());
        checkVibration.setChecked(prefs.getBoolean(VIBRATION, true));
        spinListen();
        seekListen();
        checkListen();
        clickListen();
        initLangs();
        setTextsLang();
    }

    private void spinListen() {
        spinLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != Droid.Lang.getLang()) {
                    media.play(Droid.Media.CLICK);
                    Droid.Lang.setLang(position);
                    prefs.edit().putInt(Droid.Lang.LANGUAGE, position).apply();
                    setTextsLang();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void seekListen() {
        SeekBar.OnSeekBarChangeListener seek_listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == seekMusic) {
                    media.setMusicVolume(progress);
                    prefs.edit().putInt(Droid.Media.MUSIC, progress).apply();
                } else if (seekBar == seekSound) {
                    media.setFxVolume(progress);
                    prefs.edit().putInt(Droid.Media.SOUND, progress).apply();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        };
        seekMusic.setOnSeekBarChangeListener(seek_listener);
        seekSound.setOnSeekBarChangeListener(seek_listener);
    }

    private void checkListen() {
        checkVibration.setOnCheckedChangeListener((v, isChecked) -> {
            media.play(Droid.Media.CLICK);
            if (v == checkVibration) {
                prefs.edit().putBoolean(VIBRATION, isChecked).apply();
            }
        });
    }

    private void clickListen(){
        View.OnClickListener clickListen = v -> {
            if (v == img_back) {
                media.play(Droid.Media.CLOSE);
                finish();
            } else if(v == imgClose) {
                media.play(Droid.Media.CLOSE);
                hidePop();
            } else {
                media.play(Droid.Media.CLICK);
                /*if (v == btnSignIn) {
                    setFragment(new F_SignIn());
                } else if(v == btnSignUp) {
                    setFragment(new F_SignUp());
                }*/
            }
        };
        img_back.setOnClickListener(clickListen);
        imgClose.setOnClickListener(clickListen);
        /*btnSignIn.setOnClickListener(clickListen);
        btnSignUp.setOnClickListener(clickListen);*/
    }

    private void setFragment(Fragment frg) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.opts_pop_frg, frg).commit();
        // sign.setVisibility(GONE);
        settings.setVisibility(GONE);
        pop.setVisibility(VISIBLE);
        imgClose.setVisibility(VISIBLE);
    }

    private void hidePop() {
        imgClose.setVisibility(GONE);
        pop.setVisibility(GONE);
        settings.setVisibility(VISIBLE);
        // sign.setVisibility(VISIBLE);
    }

    private void initLangs() {
        int enIndex = Droid.Lang.indexOf(Droid.Lang.ENGLISH),
                esIndex = Droid.Lang.indexOf(Droid.Lang.SPANISH);
        lang = new Droid.Lang();
        lang.addText(SIGN_IN, enIndex, "Sign in");
        lang.addText(SIGN_IN, esIndex, "Ingresar");

        lang.addText(SIGN_UP, enIndex, "Sign up");
        lang.addText(SIGN_UP, esIndex, "Registrarse");
    }

    private void setTextsLang() {
        /*btnSignIn.setText(lang.getText(SIGN_IN));
        btnSignUp.setText(lang.getText(SIGN_UP));*/
    }

}
