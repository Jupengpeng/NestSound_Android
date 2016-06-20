package com.xilu.wybz.view.toast;

import android.app.Activity;
import android.view.ViewGroup;
import com.xilu.wybz.R;
import static android.view.Gravity.BOTTOM;
import static com.xilu.wybz.view.toast.AppMsg.LENGTH_SHORT;

/**
 * Created by hujunwei on 16/6/20.
 */
public class ToastManager {
    /*
    *  location 0:up 1:bottom 2:指定位置
    *  viewGroup 指定位置
     */
    public static void toastMsg(Activity activity, String msg, int location, ViewGroup viewGroup) {
        final AppMsg.Style style = new AppMsg.Style(LENGTH_SHORT, R.color.confirm);
        ;
        AppMsg appMsg = AppMsg.makeText(activity, msg, style);
        if (viewGroup != null) {
            appMsg.setParent(viewGroup);
            appMsg.setAnimation(R.anim.up_in_anim, R.anim.up_out_anim);
        }else{
            if(location==1){
                appMsg.setLayoutGravity(BOTTOM);
            }
        }
        if(location==1) {
            appMsg.setAnimation(R.anim.bottom_in_anim, R.anim.bottom_out_anim);
        }else{
            appMsg.setAnimation(R.anim.up_in_anim, R.anim.up_out_anim);
        }
        appMsg.show();
    }

    public static void toastBottom(Activity activity, String msg) {
        toastMsg(activity, msg, 1, null);
    }

    public static void toastLocation(Activity activity, String msg, ViewGroup viewGroup) {
        toastMsg(activity, msg, 0, viewGroup);
    }
    public static void toastBottomLocation(Activity activity, String msg, ViewGroup viewGroup) {
        toastMsg(activity, msg, 1, viewGroup);
    }
    public static void toastTop(Activity activity, String msg) {
        toastMsg(activity, msg, 0, null);
    }
}
