package com.blieve.dodgie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.blieve.dodgie.controller.ControlGame;
import com.blieve.dodgie.model.Block;
import com.blieve.dodgie.model.Obstacle;
import com.blieve.dodgie.model.User;
import com.blieve.dodgie.R;
import com.blieve.dodgie.util.Droid;

public class A_Load extends Droid.BaseActivity {

    private A_Load aLoad;
    private ProgressBar loadBar;
    private SharedPreferences prefConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_load);

        aLoad = this;
        loadBar = findViewById(R.id.load_bar);

        AsyncLoad asyncLoad = new AsyncLoad();
        asyncLoad.execute(10);
    }

    private void initVars() {
        prefConfig = getSharedPreferences(A_Options.PREF_CONFIG, MODE_PRIVATE);

        User.get().init(getSharedPreferences(User.ALIAS, MODE_PRIVATE));
        Droid.UI.init(this);
        Droid.UI.setLandscape(true);
        ControlGame.CELLS_Y = 9;
        ControlGame.CELL_W = Droid.UI.SCREEN_H / ControlGame.CELLS_Y;
        ControlGame.CELLS_X = Droid.UI.SCREEN_W / ControlGame.CELL_W;
        Obstacle.setBounds(
                Droid.UI.SCREEN_W / 2f + ControlGame.CELL_W * 2,
                Droid.UI.SCREEN_H / 2f + ControlGame.CELL_W * 2
        );
        Block.setWidth(ControlGame.CELL_W);
    }

    private void initLangs() {
        Droid.Lang.addLang(Droid.Lang.ENGLISH);
        Droid.Lang.addLang(Droid.Lang.SPANISH);
        Droid.Lang.setLang(prefConfig.getInt(Droid.Lang.LANGUAGE, 0));
    }

    private void initSounds() {
        Droid.Media media = Droid.Media.get();
        media.setMusicVolume(prefConfig.getInt(Droid.Media.MUSIC, media.getMaxVolume()));
        media.setSoundVolume(prefConfig.getInt(Droid.Media.SOUND, media.getMaxVolume()));

        media.addSound(getApplicationContext(), Droid.Media.CLICK, R.raw.click);
    }

    private class AsyncLoad extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadBar.setProgress(0);
        }

        @Override
        protected String doInBackground(Integer... params) {
            // Load
            int i = 1;
            initVars();
            publishProgress(i++);
            initLangs();
            publishProgress(i++);
            initSounds();
            publishProgress(i++);
            // Show more "progress"
            while (i <= params[0]) {
                try {
                    Thread.sleep(100);
                    publishProgress(i++);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "success";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            loadBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            startActivity(new Intent(aLoad, A_Home.class));
        }
    }
}
