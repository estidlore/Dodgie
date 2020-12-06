package com.blieve.dodgie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.blieve.dodgie.R;
import com.blieve.dodgie.fragment.F_SignIn;
import com.blieve.dodgie.fragment.F_SignUp;
import com.blieve.dodgie.util.Droid;
import com.blieve.dodgie.util.Listen;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class A_Options extends Droid {

    public final static String
            PREF_CONFIG = "cnfg",
            SOUND = "sond",
            MUSIC = "musc",
            VIBRATION = "vibr",
            LANGUAGE = "lang";
    public static int langIndex;

    private Button btn_signIn, btn_signUp;
    private CheckBox check_vibration;
    private ConstraintLayout pop, settings, sign;
    private ImageView img_back, imgClose;
    private SeekBar seek_music, seek_sound;
    private Spinner spin_lang;

    private SharedPreferences prefs;
    private Intent _home;

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

    private  void init() {
        prefs = getSharedPreferences(PREF_CONFIG, MODE_PRIVATE);
        seek_sound.setProgress(prefs.getInt(SOUND, 100));
        seek_music.setProgress(prefs.getInt(MUSIC, 100));
        langIndex = prefs.getInt(LANGUAGE, 0);
        spin_lang.setSelection(langIndex);
        check_vibration.setChecked(prefs.getBoolean(VIBRATION, true));
        _home = new Intent(A_Options.this, A_Home.class);
        spinListen();
        seekListen();
        checkListen();
        clickListen();
        setTextsLang();
    }

    private void spinListen() {
        spin_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                langIndex = position;
                prefs.edit().putInt(LANGUAGE, langIndex).apply();
                setTextsLang();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void seekListen() {
        SeekBar.OnSeekBarChangeListener seek_listener = new Listen.OnProgressChangeListener() {
            @Override
            public void onProgressChange(SeekBar seekBar, int progress) {
                if (seekBar == seek_sound) {
                    prefs.edit().putInt(SOUND, progress).apply();
                } else if (seekBar == seek_music) {
                    prefs.edit().putInt(MUSIC, progress).apply();
                }
            }
        };
        seek_sound.setOnSeekBarChangeListener(seek_listener);
        seek_music.setOnSeekBarChangeListener(seek_listener);
    }

    private void checkListen() {
        check_vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                if (v == check_vibration) {
                    prefs.edit().putBoolean(VIBRATION, isChecked).apply();
                }
            }
        });
    }

    private void clickListen(){
        View.OnClickListener clickListen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == img_back) {
                    startActivity(_home);
                } else if(v == imgClose) {
                    setEnabled(true);
                } else if (v == btn_signIn) {
                    setFragment(new F_SignIn());
                } else if(v == btn_signUp) {
                    setFragment(new F_SignUp());
                }
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
        setEnabled(false);
    }

    private void setEnabled(boolean b) {
        if(b) {
            imgClose.setVisibility(GONE);
            pop.setVisibility(GONE);
            settings.setVisibility(VISIBLE);
            sign.setVisibility(VISIBLE);
        } else {
            sign.setVisibility(GONE);
            settings.setVisibility(GONE);
            pop.setVisibility(VISIBLE);
            imgClose.setVisibility(VISIBLE);
        }
    }

    private void setTextsLang() {

    }

}
