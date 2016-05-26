package com.xilu.wybz.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CommentBean;
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
        String comment ="回复我:" + commentBean.getComment();
        SpannableString spannableString = new SpannableString(comment);
        spannableString.setSpan(new ForegroundColorSpan(0xff539ac2),2,3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    public static SpannableString getWorkCommentStyleStr(CommentBean commentBean) {
        String nickName = commentBean.target_nickname;
        if (StringUtil.isBlank(nickName)){
            return new SpannableString(commentBean.getComment());
        }

        String comment ="回复" + nickName+"："+commentBean.getComment();
        SpannableString spannableString = new SpannableString(comment);

        spannableString.setSpan(new ForegroundColorSpan(0xff539ac2),2,2+nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString getParentCommentStyleStr(CommentBean commentBean) {
        String comment ="我:" + commentBean.getComment();
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