package com.blieve.dodgie.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.createBitmap;

/**
 * @author estidlore
 */
@SuppressLint("Registered")
public class Droid extends AppCompatActivity {

    public static int SCREEN_W, SCREEN_H;
    private static int uiOptions;
    private static boolean landscape;

    // get preferences
    // getSharedPreferences(name, MODE_PRIVATE);

    // get vibrator
    // (Vibrator) getSystemService(VIBRATOR_SERVICE);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUiVisibility();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        setUiVisibility();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    /*@Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }*/

    /**
     * STATIC
     * */

    @NotNull
    private static String fileInputStream(File path, String filename) {
        File file = new File(path, filename);
        byte[] bytes = new byte[(int) file.length()];
        try {
            if(!file.exists()) file.createNewFile();
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    private static void fileOutputStream(File path, String filename, @NotNull String data) {
        File file = new File(path, filename);
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data.getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getScreenSize(Activity a) {
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            a.getWindowManager().getDefaultDisplay().getRealSize(size);
        } else {
            a.getWindowManager().getDefaultDisplay().getSize(size);
        }
        if(size.x > size.y == landscape) {
            SCREEN_W = size.x;
            SCREEN_H = size.y;
        } else {
            SCREEN_W = size.y;
            SCREEN_H = size.x;
        }
    }

    @SuppressLint("InlinedApi")
    protected static int immersive() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    protected static void init(@NotNull Activity a, boolean landscape) {
        setUiOptions(immersive());
        Droid.landscape = landscape;
        getScreenSize(a);
    }

    public static void log(String msg) {
        Log.d("DroidLog", msg);
    }

    protected static void setUiOptions(int uiOptions) {
        Droid.uiOptions = uiOptions;
    }

    protected static void vibrate(Vibrator v, int ms) {
        if(Build.VERSION.SDK_INT >= 26) {
            v.vibrate(VibrationEffect.createOneShot(ms,
                    VibrationEffect.DEFAULT_AMPLITUDE));
        } else { v.vibrate(ms); }
    }

    public static Bitmap drawableToBitmap(@NotNull Drawable drawable, int width, int height) {
        Bitmap bmp = createBitmap(width, height, ARGB_8888);
        Canvas cvs = new Canvas(bmp);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(cvs);
        return bmp;
    }

    public static Bitmap bitmapMerge(@NotNull Bitmap bmp1, @NotNull Bitmap bmp2) {
        Bitmap result = createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return result;
    }

    /**
     * NON-STATIC
     * */

    protected void setUiVisibility() {
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    
    @NotNull
    protected String readFromDir(String filename) {
        return fileInputStream(getFilesDir(), filename);
    }

    protected void writeInDir(String filename, @NotNull String data) {
        fileOutputStream(getFilesDir(), filename, data);
    }

    /*
     * Needs to add to manifest.xml
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     * */
    @NotNull
    protected String readFromExt(String filename) {
        return fileInputStream(getExternalFilesDir(null), filename);
    }

    /*
     * Needs to add to manifest.xml either
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     * or <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     * */
    protected void writeInExt(String filename, @NotNull String data) {
        fileOutputStream(getExternalFilesDir(null), filename, data);
    }

}
