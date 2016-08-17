package com.xilu.wybz.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.xilu.wybz.R;
import java.util.LinkedList;
/**
 * 固定行数展开收缩控件
 * Created by evan on 2016/3/3.
 */
public class FolderTextView extends TextView {
    private static final String ELLIPSIS="...  ";
    private static final String FOLD_TEXT = "  收起";
    private static final String UNFOLD_TEXT = "  更多";
    private boolean isFold = false;
    private boolean isDrawed = false;
    private boolean isInner = false;
    private int foldLine;
    private String fullText;
    private float mSpacingMult = 1.0f;
    private float mSpacingAdd = 0.0f;


    public FolderTextView(Context context) {
        this(context, null);
    }

    public FolderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FolderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(R.styleable.FolderTextView);
        foldLine = a.getInt(R.styleable.FolderTextView_foldline, 2);

        a.recycle();
    }

    /**
     * 不更新全文本下，进行展开和收缩操作
     * @param text
     */
    private void setUpdateText(CharSequence text){
        isInner = true;
        setText(text);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(TextUtils.isEmpty(fullText) || !isInner){
            isDrawed = false;
            fullText = String.valueOf(text);
        }
        super.setText(text, type);
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        mSpacingAdd = add;
        mSpacingMult = mult;
        super.setLineSpacing(add, mult);
    }

    public int getFoldLine() {
        return foldLine;
    }

    public void setFoldLine(int foldLine) {
        this.foldLine = foldLine;
    }

    private Layout makeTextLayout(String text) {
        return new StaticLayout(text, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
                Layout.Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isDrawed){
            resetText();
        }
        super.onDraw(canvas);
        isDrawed = true;
        isInner = false;
    }

    private void resetText() {
        String spanText = fullText;

        SpannableString spanStr;

        //收缩状态
        if(isFold){
            spanStr = createUnFoldSpan(spanText);
        }else{ //展开状态
            spanStr = createFoldSpan(spanText);
        }

        setUpdateText(spanStr);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 创建展开状态下的Span
     * @param text 源文本
     * @return
     */
    private SpannableString createUnFoldSpan(String text) {
        Layout layout = makeTextLayout(text);
        //如果行数大于固定行数
        if(layout.getLineCount() > getFoldLine()) {
            String destStr = text + FOLD_TEXT;
            int start = destStr.length() - FOLD_TEXT.length();
            int end = destStr.length();

            SpannableString spanStr = new SpannableString(destStr);
            spanStr.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spanStr;
        }else{
            return new SpannableString(text);
        }
    }

    /**
     * 创建收缩状态下的Span
     * @param text
     * @return
     */
    private SpannableString createFoldSpan(String text) {
        Layout layout = makeTextLayout(text);
        //如果行数大于固定行数
        if(layout.getLineCount() > getFoldLine()){
            String destStr = tailorText(text);
            int start = destStr.length() - UNFOLD_TEXT.length();
            int end = destStr.length();

            SpannableString spanStr = new SpannableString(destStr);
            spanStr.setSpan(clickSpan,start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spanStr;
        }else{
            return new SpannableString(text);
        }
    }

    /**
     * 裁剪文本至固定行数
     * @param text 源文本
     * @return
     */
    private String tailorText(String text){
        String destStr = text + ELLIPSIS + UNFOLD_TEXT;
        Layout layout = makeTextLayout(destStr);
        //如果行数大于固定行数
        if(layout.getLineCount() > getFoldLine()){
            int index = layout.getLineEnd(getFoldLine());
            if(text.length() < index){
                index = text.length();
            }
            String subText = text.substring(0, index-10); //从最后一位逐渐试错至固定行数
            return tailorText(subText);
        }else{
            if(layout.getLineCount()==2) {
                return destStr;
            }{
                return text;
            }
        }
    }

    ClickableSpan clickSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            isFold = !isFold;
            isDrawed = false;
            invalidate();
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
        }
    };

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
//            BrowserActivity.toBrowserActivity(mContext,info);
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