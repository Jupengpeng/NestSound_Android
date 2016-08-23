package com.xilu.wybz.common.interfaces;

import android.content.Intent;

/**
 * 解决子Activity无法接收Activity回调的问题
 * @author June
 *
 */
public interface OnTabActivityResultListener {
    public void onTabActivityResult(int requestCode, int resultCode, Intent data);
}