package com.xilu.wybz.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xilu.wybz.bean.VO.BaseVO;
import com.xilu.wybz.common.KeySet;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ActivityUtils {




    public static void start(Context context, BaseVO baseVO, Class cls){

        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(KeySet.ACTIVITY_VO,baseVO);

        context.startActivity(intent);
    }

    public static void startForResult(Activity context, BaseVO baseVO, Class cls){

        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(KeySet.ACTIVITY_VO,baseVO);

        context.startActivityForResult(intent,baseVO.requestCode);
    }

    public static void finishByResult(Activity context, BaseVO baseVO){

        Intent intent = new Intent();
        intent.putExtra(KeySet.ACTIVITY_VO,baseVO);

        context.setResult(Activity.RESULT_OK,intent);
        context.finish();
    }




    public static <T extends BaseVO> T getIntentData(Intent intent){
        if (intent == null){
            return null;
        }
       return (T)intent.getSerializableExtra(KeySet.ACTIVITY_VO);
    }

}
