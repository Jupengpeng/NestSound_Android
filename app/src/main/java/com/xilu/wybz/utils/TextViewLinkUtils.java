package com.xilu.wybz.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.xilu.wybz.ui.BrowserActivity;

import java.util.LinkedList;
/**
 * TextView 超链接处理类
 * Created by June on 2016/8/12.
 */
public class TextViewLinkUtils {
	public Context mContext;
	// 直接拷贝这些代码到你希望的位置，然后在TextView设置了文本之后调用就ok了
	public void SetLinkClickIntercept(TextView tv,Context context) {
		mContext = context;
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		CharSequence text = tv.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) tv.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			if (urls.length == 0) {
				return;
			}
			SpannableStringBuilder spannable = new SpannableStringBuilder(text);
			// 只拦截 http:// URI
			LinkedList<String> myurls = new LinkedList<String>();
			for (URLSpan uri : urls) {
				String uriString = uri.getURL();
				if (uriString.indexOf("http://") == 0) {
					myurls.add(uriString);
				}
			}
			// 循环把链接发过去
			for (URLSpan uri : urls) {
				String uriString = uri.getURL();
				if (uriString.indexOf("http://") == 0) {
					MyURLSpan myURLSpan = new MyURLSpan(uriString, myurls);
					spannable.setSpan(myURLSpan, sp.getSpanStart(uri),
							sp.getSpanEnd(uri),
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				}
			}
			tv.setText(spannable);
		}
	}
	/**
	 * 处理TextView中的链接点击事件 链接的类型包括：url，号码，email，地图 这里只拦截url，即 http:// 开头的URI
	 */
	public class MyURLSpan extends ClickableSpan {
		private String mUrl; // 当前点击的实际链接
		private LinkedList<String> mUrls; 
		// 根据需求，一个TextView中存在多个link的话，这个和我求有关，可已删除掉
		// 无论点击哪个都必须知道该TextView中的所有link，因此添加改变量
		public MyURLSpan(String url, LinkedList<String> urls) {
			mUrl = url;
			mUrls = urls;
		}
		@Override
		public void onClick(View widget) {
			// 这里你可以做任何你想要的处理
			// 比如在你自己的应用中用webview打开，而不是打开系统的浏览器
			String info = new String();
//			if (mUrls.size() == 1) {
				// 只有一个url，根据策略弹出提示对话框
				info = mUrls.get(0);
				BrowserActivity.toBrowserActivity(mContext,info);
//			} else {
//				// 多个url，弹出选择对话框，意思一下
//				info = mUrls.get(0);
//				BrowserActivity.toBrowserActivity(mContext,info);
//			}
		}
		@Override
	    public void updateDrawState(TextPaint ds) {
	        super.updateDrawState(ds);
	        // 去掉超链接的下划线
	        ds.setUnderlineText(false);
	    }
	}
}
