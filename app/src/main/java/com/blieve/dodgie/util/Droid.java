package com.blieve.dodgie.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.blieve.dodgie.R;

import org.jetbrains.annotations.NotNull;

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

    public static int
            SCREEN_W,
            SCREEN_H;
    private static float
            SCREEN_W1P,
            SCREEN_H1P;

    public static void initApp(Context ctx) {
        getScreenSize(ctx);
    }

    public static void initApp(Context ctx, boolean landscape) {
        initApp(ctx);
        setLandscape(landscape);
    }

    public static void setLandscape(boolean b) {
        if(SCREEN_W == SCREEN_H) return;
        if(b != (SCREEN_W > SCREEN_H)) {
            SCREEN_W = SCREEN_H - SCREEN_W; // -1 = 3 - 4
            SCREEN_H = SCREEN_H - SCREEN_W; // 4 = 3 + 1
            SCREEN_W = SCREEN_H + SCREEN_W; // 3 = 4 - 1
        }
    }

    private static void getScreenSize(@NotNull Context ctx) {
        Point size = new Point();
        final Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 19) {
            display.getRealSize(size);
        } else {
            display.getSize(size);
        }
        SCREEN_W = size.x;
        SCREEN_H = size.y;
        SCREEN_W1P = SCREEN_W / 100f;
        SCREEN_H1P = SCREEN_H / 100f;
    }

    public static int width(float percent) {
        return (int) (SCREEN_W1P * percent);
    }

    public static int height(float percent) {
        return (int) (SCREEN_H1P * percent);
    }

    public static void vibrate(Vibrator v, int ms) {
        if(Build.VERSION.SDK_INT >= 26)  {
            v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else v.vibrate(ms);
    }

    public static class BaseActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        @Override
        protected void onResume() {
            super.onResume();
            hideSystemUI();
        }

        @Override
        protected void onPause() {
            super.onPause();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        private void hideSystemUI() {
            Window window = getWindow();
            View decorView = window.getDecorView();

            WindowCompat.setDecorFitsSystemWindows(window, false);
            /*ViewCompat.setOnApplyWindowInsetsListener(decorView, (v, insets) -> {
                Insets i = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                decorView.setPadding(i.left, i.top, i.right, i.bottom);
                return insets;
            });*/
            WindowInsetsControllerCompat insetsControllerCompat = new WindowInsetsControllerCompat(window, decorView);
            insetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            insetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());
        }

        /*private void hideUI() {
            if(Build.VERSION.SDK_INT >= 30) {
                getWindow().setDecorFitsSystemWindows(false);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                Objects.requireNonNull(getSupportActionBar()).hide();
                // getWindow().getDecorView().setSystemUiVisibility();
            }
        }*/

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

    public static class Lang {
        // Ex. "EN", "ES"
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

    public static class Data {
        @NotNull
        public static String inputStream(File path, String filename) {
            File file = new File(path, filename);
            byte[] bytes = new byte[(int) file.length()];
            try {
                if(!file.exists()) file.createNewFile();
                FileInputStream in = new FileInputStream(file);
                in.read(bytes);
                in.close();
            } catch (IOException e) { e.printStackTrace(); }
            return new String(bytes);
        }

        public static void outputStream(File path, String filename, @NotNull String data) {
            File file = new File(path, filename);
            try {
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(data.getBytes());
                stream.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static class Img {
        public static Bitmap drawToBmp(@NotNull Drawable drawable, int width, int height) {
            Bitmap bmp = createBitmap(width, height, ARGB_8888);
            Canvas cvs = new Canvas(bmp);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(cvs);
            return bmp;
        }

        public static Bitmap bmpMerge(@NotNull Bitmap bmp1, @NotNull Bitmap bmp2) {
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

    public static class UI {
        public static void setPadding(@NotNull View v, int padding) {
            v.setPadding(padding, padding, padding, padding);
        }
    }

}
