package com.xilu.wybz.ui.http.utils;

import android.util.Log;

/**
 * Created by June on 16/04/28.
 */
public class L {
    private static boolean debug = false;

    public static void e(String msg) {
        if (debug) {
            Log.e("OkHttp", msg);
        }
    }

}

