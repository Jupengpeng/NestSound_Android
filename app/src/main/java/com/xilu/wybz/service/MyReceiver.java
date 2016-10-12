package com.xilu.wybz.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.xilu.wybz.R;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.mine.FollowAndFansActivity;
import com.xilu.wybz.ui.msg.MsgCommentActivity;
import com.xilu.wybz.ui.msg.MsgFavActivity;
import com.xilu.wybz.ui.msg.MsgPreserveActivity;
import com.xilu.wybz.ui.msg.MsgSystemActivity;
import com.xilu.wybz.ui.msg.MsgZambiaActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private int notifyId = 0;
    public NotificationManager mNotificationManager;
    public static List<Integer> ids;
    public static Map<String, List<Integer>> idTypes;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (idTypes == null) {
            idTypes = new HashMap<>();
        }
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户点击打开了通知");
            //打开自定义的Activity
//        	Intent i = new Intent(content, TestActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	content.startActivity(i);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else if ("com.xilu.wybz.intent.CLEARNOTICE".equals(intent.getAction())) {
            String type = bundle.getString("type");
            cancleNoticeByType(type);
        } else {
            Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        if (StringUtils.isNotBlank(message)) {
            notify(context, message);
        }
    }

    private void notify(Context context, String msg) {
        notifyId = (int) (System.currentTimeMillis() / 1000);
        String content = "";
        String type = "";
        try {
            JSONObject msgJson = new JSONObject(msg);
            content = msgJson.getString("content");
            type = msgJson.getString("type");
        } catch (JSONException e) {

        }
        if (StringUtils.isBlank(content) || StringUtils.isBlank(type)) return;
        if (PrefsUtil.getUserId(context) == 0) {//未登录
            if (type.equals(MyCommon.PUSH_TYPE_COMMENT) || type.equals(MyCommon.PUSH_TYPE_ZAN) ||
                    type.equals(MyCommon.PUSH_TYPE_FOV) || type.equals(MyCommon.PUSH_TYPE_RECOMENDTOINDEX) ||
                    type.equals(MyCommon.PUSH_TYPE_FOCUS) || type.equals(MyCommon.PUSH_TYPE_ADDTOSONGLIST)
                    ) {
                return;
            }
        }
        Intent openintent = null;
        //分组存储ID 用于清除消息
        if (type.equals(MyCommon.PUSH_TYPE_ACTIVITYFINISH) || type.equals(MyCommon.PUSH_TYPE_NEWACTIVITY) ||
                type.equals(MyCommon.PUSH_TYPE_RECOMENDTOINDEX) || type.equals(MyCommon.PUSH_TYPE_ADDTOSONGLIST)
                ) {
            type = MyCommon.PUSH_TYPE_SYSTEMMSG;
        } else if (type.contains(MyCommon.PUSH_TYPE_COPYRIGH)) {
            type = MyCommon.PUSH_TYPE_COPYRIGH;
        }
//        if (AppInfoUtil.isRunningForeground(content)) {//app在前台
//           } else {//app在后台 需要打开MainTabActivity 再进行跳转
//            openintent = new Intent(content, MainTabActivity.class);
//            openintent.putExtra("type", type);
//        }
        if (type.equals(MyCommon.PUSH_TYPE_COMMENT)) {
            openintent = new Intent(context, MsgCommentActivity.class);
        } else if (type.equals(MyCommon.PUSH_TYPE_ZAN)) {
            openintent = new Intent(context, MsgZambiaActivity.class);
        } else if (type.equals(MyCommon.PUSH_TYPE_FOV)) {
            openintent = new Intent(context, MsgFavActivity.class);
        } else if (type.equals(MyCommon.PUSH_TYPE_COPYRIGHSUCCESS)
                ||type.equals(MyCommon.PUSH_TYPE_COPYRIGHFAIL)) {
            openintent = new Intent(context, MsgPreserveActivity.class);
        } else if (type.equals(MyCommon.PUSH_TYPE_SYSTEMMSG)
                ||type.equals(MyCommon.PUSH_TYPE_NEWACTIVITY)
                ||type.equals(MyCommon.PUSH_TYPE_ACTIVITYFINISH)
                ||type.equals(MyCommon.PUSH_TYPE_RECOMENDTOINDEX)
                ||type.equals(MyCommon.PUSH_TYPE_ADDTOSONGLIST)
                ||type.equals(MyCommon.PUSH_TYPE_SYSTEMMSG)
                ) {
            openintent = new Intent(context, MsgSystemActivity.class);
        } else if (type.equals(MyCommon.PUSH_TYPE_FOCUS)) {
            openintent = new Intent(context, FollowAndFansActivity.class);
            openintent.putExtra(KeySet.KEY_TYPE, KeySet.TYPE_FANS_ACT);
            openintent.putExtra(KeySet.KEY_UID, PrefsUtil.getUserId(context));
        }

        if (idTypes.get(type) == null) {
            ids = new ArrayList<>();
        } else {
            ids = idTypes.get(type);
        }
        ids.add(notifyId);
        idTypes.put(type, ids);
        //发送消息通知
        EventBus.getDefault().post(new Event.NoticeMsgEvent(type));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, openintent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setLargeIcon(BitmapUtils.ReadBitmapById(context, R.drawable.ic_launcher));
        mBuilder.setTicker(content);
        mBuilder.setContentTitle(context.getResources().getString(R.string.app_name));
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setContentText(content);
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    public static boolean getHasUnReadMsg(String type) {
        if (idTypes == null) {
            return false;
        } else {
            List<Integer> ids = idTypes.get(type);
            if (ids != null && ids.size() > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void cancleNoticeByType(String type) {
        if (idTypes == null || mNotificationManager == null) return;
        List<Integer> ids = idTypes.get(type);
        if (ids != null && ids.size() > 0) {
            for (Integer id : ids) {
                mNotificationManager.cancel(id);
            }
            ids.clear();
            idTypes.put(type, ids);
        }
    }
}
