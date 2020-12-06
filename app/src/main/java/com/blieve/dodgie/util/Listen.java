package com.blieve.dodgie.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SeekBar;

public class Listen {

    private OnCallListen listen;

    public void setOnCallListener(OnCallListen listen) {
        this.listen = listen;
    }

    public Listen() {
    }

    public void call() {
        if(this.listen != null) {
            listen.onCall();
        }
    }

    public interface OnCallListen {
        void onCall();
    }

    public abstract static class OnProgressChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) onProgressChange(seekBar, progress);
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }

        public abstract void onProgressChange(SeekBar seekBar, int progress);
    }

    public abstract class OnTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) { this.onTextChanged(s); }

        abstract void onTextChanged(Editable s);

    }

}
