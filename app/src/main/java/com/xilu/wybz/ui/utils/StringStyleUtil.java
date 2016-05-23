package com.xilu.wybz.ui.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.InforCommentBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

public class StringStyleUtil {
    private StringStyleUtil() {

    }
    public static String removeSpecialCharacters(String junkData) {
        junkData = junkData.replaceAll("(\r\n|\n)", "");
        return junkData;
    }
    public static SpannableString getCommentStyleStr(CommentBean commentBean) {
        String comment = commentBean.getName() + ":" + commentBean.getComment();
        SpannableString spannableString = new SpannableString(comment);
//        spannableString.setSpan(new RelativeSizeSpan(0.8f),gank.desc.length()+1,gankStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY),gank.desc.length()+1,gankStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public static SpannableString getCommentStyleStr(InforCommentBean commentBean) {
        String comment ="回复我:" + commentBean.getComment();
        SpannableString spannableString = new SpannableString(comment);
        spannableString.setSpan(new ForegroundColorSpan(0xff539ac2),2,3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public static SpannableString getParentCommentStyleStr(InforCommentBean commentBean) {
        String comment ="我:" + commentBean.getParentcomment();
        SpannableString spannableString = new SpannableString(comment);
        spannableString.setSpan(new ForegroundColorSpan(0xff539ac2),0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    //获取歌词内容
    public static String getLyrics(WorksData worksData){
        String lyrics = worksData.getLyrics();
        String content = "";
        if (worksData.getSampleid() == 1) {
            List<String> mList = new Gson().fromJson(lyrics, new TypeToken<List<String>>() {}.getType());
            for (String string : mList) {
                content += string + "\n";
            }
            return content;
        }
        return lyrics;
    }
}