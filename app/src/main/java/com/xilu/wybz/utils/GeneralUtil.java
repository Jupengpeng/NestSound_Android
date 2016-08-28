package com.xilu.wybz.utils;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.util.regex.Pattern;

public class GeneralUtil {
    public GeneralUtil() {
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static void measureView(View view) {
        LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new LayoutParams(-1, -2);
        }

        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int tempHeight = p.height;
        int height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        view.measure(width, height);
    }


}