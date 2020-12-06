package com.blieve.dodgie.util;

public class Var<T> {

    private T val;
    private OnVarChangeListener listener;

    public void setOnVarChangeListener(OnVarChangeListener listener) {
        this.listener = listener;
    }

    public Var(T val) {
        this.val = val;
    }

    public Var() {
    }

    public T get() {
        return val;
    }

    public void set(final T newVal) {
        val = newVal;
        if(this.listener != null) {
            listener.onVarChanged(newVal);
        }
    }

    public interface OnVarChangeListener<T> {
        void onVarChanged(T newVal);
    }

}
