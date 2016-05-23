package com.xilu.wybz.ui.utils;


public class FormatHelper {

    /**
     * return a format time of 00:00
     *
     * @return
     */
    public static String formatDuration(int seconds) {
        int secondPart = seconds % 60;
        int minutePart = seconds / 60;
        return (minutePart >= 10 ? minutePart : "0" + minutePart) + ":" + (secondPart >= 10 ? secondPart : "0" + secondPart);
    }

    public static int formatTime(String duration) {
        String[] str = duration.split(":");
        int time = Integer.valueOf(str[0]) * 60 + Integer.valueOf(str[1]);
        return time;
    }
}
