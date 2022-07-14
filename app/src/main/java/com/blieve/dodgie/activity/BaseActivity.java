package com.blieve.dodgie.activity;

import com.blieve.dodgie.util.Droid;

public class BaseActivity extends Droid.FullScreenActivity {

    protected final Droid.Media media = Droid.Media.get();

    @Override
    protected void onResume() {
        super.onResume();
        media.play(Droid.Media.BASE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        media.pause(Droid.Media.BASE);
    }

}
