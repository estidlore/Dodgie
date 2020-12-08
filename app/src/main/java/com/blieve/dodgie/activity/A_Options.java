package com.blieve.dodgie.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.blieve.dodgie.R;
import com.blieve.dodgie.fragment.F_SignIn;
import com.blieve.dodgie.fragment.F_SignUp;
import com.blieve.dodgie.util.Droid;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class A_Options extends Droid.BaseActivity {

    public final static String
            PREF_CONFIG = "cnfg",
            SOUND = "sond",
            MUSIC = "musc",
            VIBRATION = "vibr",
            LANGUAGE = "lang",
            ENGLISH  = "en",
            SPANISH  = "es";
    private final String
            SIGN_IN = "signIn",
            SIGN_UP = "signUp";

    private Button btn_signIn, btn_signUp;
    private CheckBox check_vibration;
    private ConstraintLayout pop, settings, sign;
    private ImageView img_back, imgClose;
    private SeekBar seek_music, seek_sound;
    private Spinner spin_lang;

    private SharedPreferences prefs;
    private Droid.Lang lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_options);

        btn_signIn = findViewById(R.id.opts_signIn);
        btn_signUp = findViewById(R.id.opts_signUp);
        check_vibration = findViewById(R.id.opts_check_vibration);
        pop = findViewById(R.id.opts_pop);
        settings = findViewById(R.id.settings);
        sign = findViewById(R.id.opts_sign);
        img_back = findViewById(R.id.opts_back);
        imgClose = findViewById(R.id.opts_close);
        seek_sound = findViewById(R.id.opts_seek_sound);
        seek_music = findViewById(R.id.opts_seek_music);
        spin_lang = findViewById(R.id.opts_spin_lang);

        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        showPop(false);
    }

    private  void init() {
        prefs = getSharedPreferences(PREF_CONFIG, MODE_PRIVATE);
        seek_sound.setProgress(prefs.getInt(SOUND, 100));
        seek_music.setProgress(prefs.getInt(MUSIC, 100));
        spin_lang.setSelection(Droid.Lang.getLang());
        check_vibration.setChecked(prefs.getBoolean(VIBRATION, true));
        spinListen();
        seekListen();
        checkListen();
        clickListen();
        initLangs();
        setTextsLang();
    }

    private void spinListen() {
        spin_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Droid.Lang.setLang(position);
                prefs.edit().putInt(LANGUAGE, position).apply();
                setTextsLang();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void seekListen() {
        SeekBar.OnSeekBarChangeListener seek_listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == seek_sound) {
                    prefs.edit().putInt(SOUND, progress).apply();
                } else if (seekBar == seek_music) {
                    prefs.edit().putInt(MUSIC, progress).apply();
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

    private void checkListen() {
        check_vibration.setOnCheckedChangeListener((v, isChecked) -> {
            if (v == check_vibration) {
                prefs.edit().putBoolean(VIBRATION, isChecked).apply();
            }
        });
    }

    private void clickListen(){
        View.OnClickListener clickListen = v -> {
            if (v == img_back) {
                finish();
            } else if(v == imgClose) {
                showPop(false);
            } else if (v == btn_signIn) {
                setFragment(new F_SignIn());
            } else if(v == btn_signUp) {
                setFragment(new F_SignUp());
            }
        };
        img_back.setOnClickListener(clickListen);
        imgClose.setOnClickListener(clickListen);
        btn_signIn.setOnClickListener(clickListen);
        btn_signUp.setOnClickListener(clickListen);
    }

    private void setFragment(Fragment frg) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.opts_pop_frg, frg).commit();
        showPop(true);
    }

    private void showPop(boolean b) {
        if(b) {
            sign.setVisibility(GONE);
            settings.setVisibility(GONE);
            pop.setVisibility(VISIBLE);
            imgClose.setVisibility(VISIBLE);
        } else {
            imgClose.setVisibility(GONE);
            pop.setVisibility(GONE);
            settings.setVisibility(VISIBLE);
            sign.setVisibility(VISIBLE);
        }
    }

    private void initLangs() {
        int enIndex = Droid.Lang.indexOf(ENGLISH),
                esIndex = Droid.Lang.indexOf(SPANISH);
        lang = new Droid.Lang();
        lang.addText(SIGN_IN, enIndex, "Sign in");
        lang.addText(SIGN_IN, esIndex, "Ingresar");

        lang.addText(SIGN_UP, enIndex, "Sign up");
        lang.addText(SIGN_UP, esIndex, "Registrarse");
    }

    private void setTextsLang() {
        btn_signIn.setText(lang.getText(SIGN_IN));
        btn_signUp.setText(lang.getText(SIGN_UP));
    }

}
