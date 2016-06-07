/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bither.util;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
public class NativeUtil {
	private static int DEFAULT_QUALITY = 95;
	private static CompressPicInterface mCompressPicInterface;
	public static void compressBitmap(Bitmap bit, String fileName,
			boolean optimize, int zoomSize) {
		compressBitmap(bit, DEFAULT_QUALITY, fileName, optimize, zoomSize);
	}
	public static void compressBitmap(Bitmap bit, int quality, String fileName,
			boolean optimize,float zoomSize) {
		Log.d("native", "compress of native");
		Bitmap result = Bitmap.createBitmap((int)(bit.getWidth() / zoomSize), (int)(bit.getHeight() / zoomSize),
				Config.ARGB_8888);  // 缩小3倍
		Canvas canvas = new Canvas(result);
		Rect rect = new Rect(0, 0, bit.getWidth(), bit.getHeight());// original
		rect = new Rect(0, 0, (int)(bit.getWidth() / zoomSize), (int)(bit.getHeight() / zoomSize));// 缩小3倍
		canvas.drawBitmap(bit, null, rect, null);
		saveBitmap(result, quality, fileName, optimize);
		result.recycle();
	}

	private static void saveBitmap(Bitmap bit, int quality, String fileName,
			boolean optimize) {
		compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality,
				fileName.getBytes(), optimize);

	}
	private static native String compressBitmap(Bitmap bit, int w, int h,
			int quality, byte[] fileNameBytes, boolean optimize);

	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("bitherjni");
	}
	// 高质量压缩图片
	//参数说明 quality 质量0-100 zoomSize 分辨率缩小比
	public static void CompressionPic( final String imagePath,
								   final String newImagePath,
								   final CompressPicInterface compressPicInterface) {
		mCompressPicInterface = compressPicInterface;
		System.gc();
		new Thread(new Runnable() {
			public void run() {
				try {
					int quality = 60;
					float zoomSize = 1;
					Bitmap bit = BitmapUtils.getSDCardImg(imagePath);
					if(bit==null){
						handler.sendEmptyMessage(0x100);
						return;
					}
					double fileSize = FileUtils.getFileOrFilesSize(imagePath, 3);
					if(fileSize>=7){
						quality = 50;
					}else if(fileSize>=5&&fileSize<7){
						quality = 55;
					}else if(fileSize>=4&&fileSize<5){
						quality = 60;
					}else if(fileSize>=3&&fileSize<4){
						quality = 65;
					}else if(fileSize>=2&&fileSize<3){
						quality = 70;
					}else if(fileSize>=1&&fileSize<2){
						quality = 75;
					}else if(fileSize>=0.5&&fileSize<1){
						quality = 80;
					}else if(fileSize>=0.2&&fileSize<0.5){
						quality = 90;
					}else {
						quality = 100;
					}
					int bitmapWidth = bit.getWidth();
					float baseWifth = 1080f;
					if(bitmapWidth<=baseWifth){
						zoomSize = 1;
					}else{
						zoomSize = bitmapWidth/baseWifth;
					}
					File originalFile = new File(newImagePath);
					File posterDir = new File(FileDir.inspirePicDir);
					if (!posterDir.exists()) {
						posterDir.mkdirs();
					}
					FileOutputStream fileOutputStream = new FileOutputStream(originalFile);
					bit.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
					NativeUtil.compressBitmap(bit, quality,
							originalFile.getAbsolutePath(), true, zoomSize);
					bit.recycle();
					handler.sendEmptyMessage(0x123);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					handler.sendEmptyMessage(0x100);
				}
			}
		}).start();
	}
	public static Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0x123){
				mCompressPicInterface.onCompressResult(0,"");
			}else{
				mCompressPicInterface.onCompressResult(-1,"");
			}
		}
	};
	public interface CompressPicInterface {
		void onCompressResult(int errorCode, String path);
	}
}
