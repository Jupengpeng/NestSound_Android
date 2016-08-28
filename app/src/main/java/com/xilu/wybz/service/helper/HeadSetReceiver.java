package com.xilu.wybz.service.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

public class HeadSetReceiver extends BroadcastReceiver {

	Timer timer = null;
	HeadSetHelper.OnHeadSetListener headSetListener = null;
	private static boolean isTimerStart = false;
	private static MyTimer myTimer = null;
	public HeadSetReceiver(){
		timer = new Timer(true);
		this.headSetListener = HeadSetHelper.getInstance().getOnHeadSetListener();
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 String intentAction = intent.getAction() ;
	        if(Intent.ACTION_MEDIA_BUTTON.equals(intentAction)){
	        	KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
	        	if(headSetListener != null){
	        		try {
	        			if(keyEvent.getAction() == KeyEvent.ACTION_UP){
	        				if(isTimerStart){
	        					myTimer.cancel();
	        					isTimerStart = false;
	        					headSetListener.onDoubleClick();
	        				}else{
	        					myTimer = new MyTimer();
	        					timer.schedule(myTimer,1000);
	        					isTimerStart = true;
	        				}
	        			}
					} catch (Exception e) {
						// TODO: handle exception
					}
	        	}
	        }
//	        abortBroadcast();
	}
	class MyTimer extends TimerTask {
			@Override
			public void run() {
				try {
					myHandle.sendEmptyMessage(0);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
	};
	Handler myHandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			headSetListener.onClick();
			isTimerStart = false;
		}
		
	};
		
}
