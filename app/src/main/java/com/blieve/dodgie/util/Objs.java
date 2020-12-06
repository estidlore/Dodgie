package com.blieve.dodgie.util;

import org.jetbrains.annotations.NotNull;

public class Objs {

    @NotNull
    public static <T> T nonNull(T obj) {
        if(obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static int sum(int n) {
        return (n > 0 ? n + sum(n - 1) : 0);
    }

}
