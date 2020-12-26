package com.blieve.dodgie.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.blieve.dodgie.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.createBitmap;
import static java.lang.Math.abs;

/**
 * @author estidlore
 */
@SuppressLint("Registered")
public class Droid {

    public static void vibrate(Vibrator v, int ms) {
        if(Build.VERSION.SDK_INT >= 26)  {
            v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else v.vibrate(ms);
    }

    public static class BaseActivity extends AppCompatActivity {

        @SuppressLint("InlinedApi")
        private static final int UI_OPTIONS = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            getWindow().getDecorView().getRootView().setPadding(0, 0,
                    UI.NAV_BAR_SIZE_RIGHT, UI.NAV_BAR_SIZE_BOTTOM);
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        hideSystemUI();
                    }
                }
            });
            hideSystemUI();
        }

        @Override
        protected void onPause() {
            super.onPause();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        private void hideSystemUI() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.hide();
            getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
        }

    }

    public static class Collision {

        public static boolean pointInCircle(float pX, float pY, float cX, float cY, float cR) {
            float dx = pX - cX, dy = pY - cY;
            return Math.sqrt(dx * dx + dy * dy) <= cR;
        }

        public static boolean pointInRect(float pX, float pY, float left, float top, float right, float bottom) {
            return pX >= left && pX <= right && pY >= top && pY >= bottom;
        }

        public static boolean rectWithCircle(float circleX, float circleY, float radius,
                float x, float y, float width, float height) {
            float dx = abs(circleX - x) - width / 2,
                    dy = abs(circleY - y) - height / 2;
            if (dx > radius || dy > radius) return false;
            if (dx <= 0 || dy <= 0) return true;
            return (dx * dx + dy * dy <= radius * radius);
        }

    }

    public static class Data {
        public static String inputStream(File dir, String filename) {
            File file = new File(dir, filename);
            byte[] bytes = new byte[(int) file.length()];
            try {
                if(!file.exists()) file.createNewFile();
                FileInputStream in = new FileInputStream(file);
                in.read(bytes);
                in.close();
            } catch (IOException e) { e.printStackTrace(); }
            return new String(bytes);
        }

        public static void outputStream(File dir, String filename, String data) {
            File file = new File(dir, filename);
            try {
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(data.getBytes());
                stream.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static class Lang {

        public static final String
                LANGUAGE = "lang",
                ENGLISH  = "en",
                SPANISH  = "es";

        // Ex. "en", "es"
        private static final ArrayList<String> langs = new ArrayList<>();
        private static int lang = 0;
        // Word, Words for each language
        private final Map<String, ArrayList<String>> texts = new HashMap<>();

        public static void addLang(String lang) {
            langs.add(lang);
        }

        public static int getLang() {
            return lang;
        }

        public static void setLang(int lang) {
            Lang.lang = lang;
        }

        public static String getLangName(int index) {
            return langs.get(index);
        }

        public static int indexOf(String name) {
            return langs.indexOf(name);
        }

        public void addText(String name, int langIndex,  String content) {
            if(!texts.containsKey(name)) {
                texts.put(name, new ArrayList<>());
            }
            texts.get(name).add(langIndex, content);
        }

        public String getText(String name) {
            try {
                return texts.get(name).get(lang);
            } catch (NullPointerException e) { return "-"; }
        }

    }

    public static class Img {
        public static Bitmap drawToBmp(Drawable drawable, int width, int height) {
            Bitmap bmp = createBitmap(width, height, ARGB_8888);
            Canvas cvs = new Canvas(bmp);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(cvs);
            return bmp;
        }

        public static Bitmap bmpMerge(Bitmap bmp1, Bitmap bmp2) {
            Bitmap result = createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(bmp1, 0, 0, null);
            canvas.drawBitmap(bmp2, 0, 0, null);
            return result;
        }
    }

    public static class Listen {
        private OnCallListen listen;

        public void setOnCallListener(OnCallListen listen) {
            this.listen = listen;
        }

        public Listen() { }

        public void call() {
            if(this.listen != null) listen.onCall();
        }

        public interface OnCallListen {
            void onCall();
        }
    }

    public static class Media {

        private static Media instance;

        public static Media get() {
            if(instance == null) instance = new Media();
            return instance;
        }

        public static final String
                SOUND = "sond",
                MUSIC = "musc",
                CLICK = "clck",
                CLOSE = "cls";

        private final HashMap<String, MediaPlayer> musics, sounds;
        private final int maxVolume;

        private int musicVolume, soundVolume;

        private Media() {
            this.musics = new HashMap<>();
            this.sounds = new HashMap<>();
            maxVolume = 100;
        }

        public void addMusic(Context ctx, String name, int resId) {
            musics.put(name, MediaPlayer.create(ctx, resId));
        }

        public void addSound(Context ctx, String name, int resId) {
            sounds.put(name, MediaPlayer.create(ctx, resId));
        }

        public void setMusicVolume(int volume) {
            musicVolume = volume;
            float realVolume = (float) volume / maxVolume;
            for (MediaPlayer i : musics.values()) {
                i.setVolume(realVolume, realVolume);
            }
        }

        public void setSoundVolume(int volume) {
            soundVolume = volume;
            float realVolume = (float) volume / maxVolume;
            Log.d("debug", "setSoundVolume(" + volume + ") : " + realVolume);
            for (MediaPlayer i : sounds.values()) {
                i.setVolume(realVolume, realVolume);
            }
        }

        public int getMusicVolume() {
            return musicVolume;
        }

        public int getSoundVolume() {
            return soundVolume;
        }

        public int getMaxVolume() {
            return maxVolume;
        }

        public void playMusic(String name) {
            if(musicVolume > 0) {
                MediaPlayer mp = musics.get(name);
                if(mp != null) mp.start();
            }
        }

        public void playSound(String name) {
            if(soundVolume > 0) {
                MediaPlayer mp = sounds.get(name);
                if(mp != null) mp.start();
            }
        }

    }

    public static class UI {

        public static int SCREEN_W, SCREEN_H, NAV_BAR_SIZE_RIGHT, NAV_BAR_SIZE_BOTTOM;
        private static float SCREEN_W1P, SCREEN_H1P;

        public static void init(Context ctx) {
            Point size = getScreenSize(ctx);
            SCREEN_W = size.x;
            SCREEN_H = size.y;
            SCREEN_W1P = SCREEN_W / 100f;
            SCREEN_H1P = SCREEN_H / 100f;

            size = getNavBarSize(ctx);
            NAV_BAR_SIZE_RIGHT = size.x;
            NAV_BAR_SIZE_BOTTOM = size.y;
        }

        public static void setLandscape(boolean b) {
            if(SCREEN_W == SCREEN_H) return;
            if(b != (SCREEN_W > SCREEN_H)) {
                // screen vars change
                SCREEN_W = SCREEN_H - SCREEN_W;
                SCREEN_H = SCREEN_H - SCREEN_W;
                SCREEN_W = SCREEN_H + SCREEN_W;

                SCREEN_W1P = SCREEN_W / 100f;
                SCREEN_H1P = SCREEN_H / 100f;

                // navBar vars change
                NAV_BAR_SIZE_RIGHT = NAV_BAR_SIZE_BOTTOM - NAV_BAR_SIZE_RIGHT;
                NAV_BAR_SIZE_BOTTOM = NAV_BAR_SIZE_BOTTOM - NAV_BAR_SIZE_RIGHT;
                NAV_BAR_SIZE_RIGHT = NAV_BAR_SIZE_BOTTOM + NAV_BAR_SIZE_RIGHT;
            }
        }

        public static Point getRealScreenSize(Context ctx) {
            Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            Point size = new Point();
            display.getRealSize(size);
            return size;
        }

        private static Point getScreenSize(Context ctx) {
            Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size;
        }

        private static Point getNavBarSize(Context ctx) {
            Point size = getRealScreenSize(ctx),
                    screen = getScreenSize(ctx);
            size.x -= screen.x;
            size.y -= screen.y;
            return size;
        }

        public static int width(float percent) {
            return (int) (SCREEN_W1P * percent);
        }

        public static int height(float percent) {
            return (int) (SCREEN_H1P * percent);
        }

        public static void setPadding(View v, int padding) {
            v.setPadding(padding, padding, padding, padding);
        }

    }

}
