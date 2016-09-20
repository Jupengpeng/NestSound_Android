package com.xilu.wybz.http.utils;

/**
 * Created by June on 16/04/28.
 */
public class Exceptions {
    public static void illegalArgument(String msg, Object... params) {
        throw new IllegalArgumentException(String.format(msg, params));
    }


}
