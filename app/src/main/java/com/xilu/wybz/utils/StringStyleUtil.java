package com.xilu.wybz.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.MsgCommentBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.ui.mine.UserInfoActivity;

import java.util.List;

public class StringStyleUtil {
    private StringStyleUtil() {

    }
    public static String removeSpecialCharacters(String junkData) {
        junkData = junkData.replaceAll("(\r\n|\n)", "");
        return junkData;
    }

    public static SpannableString getCommentStyleStr(MsgCommentBean commentBean) {
        String nickName = commentBean.targetname;
        if (StringUtils.isBlank(nickName)){
            return new SpannableString(commentBean.getComment());
        }
        String comment ="回复" + nickName+"："+commentBean.getComment();
        SpannableString spannableString = new SpannableString(comment);

        spannableString.setSpan(new ForegroundColorSpan(0xff539ac2),2,2+nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return spannableString;
    }


    public static SpannableString getWorkCommentStyleStr(Context context, CommentBean commentBean) {
        String nickName = commentBean.target_nickname;
        if (StringUtils.isBlank(nickName)){
            return new SpannableString(commentBean.getComment());
        }
        String comment ="回复" + nickName+"："+commentBean.getComment();
        SpannableString spannableString = new SpannableString(comment);

//        spannableString.setSpan(new ForegroundColorSpan(0xff539ac2),2,2+nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new UserNameClickableSpan(context,commentBean),2,2+nickName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString getUserLink(Context context, String userName, int uid) {
        SpannableString spannableString = new SpannableString(userName);
        spannableString.setSpan(new UserNameLinkClickableSpan(context,userName,uid),0,userName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
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
    public static class UserNameLinkClickableSpan extends ClickableSpan{
        public Context context;
        String name;
        int uid;
        public UserNameLinkClickableSpan(Context context,String name, int uid) {
            this.context = context;
            this.name = name;
            this.uid = uid;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#ff539ac2"));
            ds.setUnderlineText(false);
            ds.setAntiAlias(true);
        }

        @Override
        public void onClick(View widget) {
            if(PrefsUtil.getUserId(context)!=uid) {
                UserInfoActivity.ToNewUserInfoActivity(context, uid, name);
            }
        }

    }

    public static class UserNameClickableSpan extends ClickableSpan{
        public Context context;
        CommentBean comment;
        public UserNameClickableSpan(Context context,CommentBean comment) {
            this.context = context;
            this.comment = comment;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#ff539ac2"));
            ds.setUnderlineText(false);
            ds.setAntiAlias(true);
        }

        @Override
        public void onClick(View widget) {
            if(PrefsUtil.getUserId(context)!=comment.target_uid) {
                UserInfoActivity.ToNewUserInfoActivity(context, comment.target_uid, comment.target_nickname);
            }
        }

    }
}