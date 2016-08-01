package com.xilu.wybz.service.helper;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;


public class HeadSetHelper {

	private static HeadSetHelper headSetHelper;
	
	private OnHeadSetListener headSetListener = null;
	
	public static HeadSetHelper getInstance(){
		if(headSetHelper == null){
			headSetHelper = new HeadSetHelper();
		}
		return headSetHelper;
	}
	
	public void setOnHeadSetListener(OnHeadSetListener headSetListener){
		this.headSetListener = headSetListener;
	}
	

	public void open(Context context){
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		ComponentName name = new ComponentName(context.getPackageName(),
				HeadSetReceiver.class.getName());
		audioManager.registerMediaButtonEventReceiver(name);
	}

	public void close(Context context){
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		ComponentName name = new ComponentName(context.getPackageName(),
				HeadSetReceiver.class.getName());
		audioManager.unregisterMediaButtonEventReceiver(name);
	}

	public void delHeadSetListener()
	{
		this.headSetListener=null;
	}
	

	protected OnHeadSetListener getOnHeadSetListener() {
		return headSetListener;
	}
	

	public interface OnHeadSetListener{

		public void onClick();
		public void onDoubleClick();
	}
}
