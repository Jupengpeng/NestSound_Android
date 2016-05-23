package com.xilu.wybz.ui.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;

public class SealookDBHelper extends SQLiteOpenHelper {
    private final static int DATABASE_VERSION = 1;
    public static final String dbPath = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "yinchao/DbCourse";
    public static final String DB_NAME = dbPath + "/" + "works.db";
    private File dbf = null;
    public static final String DROP_TABLE_GT = "DROP TABLE IF EXISTS music_cz";
    public static final String CREATE_TABLE_GT = "CREATE TABLE music_cz (id TEXT PRIMARY KEY, title TEXT, lyrics TEXT, createtype TEXT, hotid TEXT, " +
            "diyids TEXT, speed TEXT, useheadset TEXT, playurl TEXT, times TEXT, json TEXT," +
            " recordsize TEXT,createtime TEXT);";
    /**
     * @param context 上下文对象
     */
    public SealookDBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    /**
     * 数据库第一次被创建时调用该方法，这里面主要进行对数据库的初始化操作
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GT);
    }

    /**
     * 数据库更新的时候调用该方法
     *
     * @param db         当前操作的数据库对象
     * @param oldVersion 老版本号
     * @param newVersion 新版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_GT);
        onCreate(db);
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        File dbp = new File(dbPath);
        dbf = new File(DB_NAME);
        if (!dbp.exists()) {
            dbp.mkdir();
        }
        // 数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        if (!dbf.exists()) {
            try {
                isFileCreateSuccess = dbf.createNewFile();
                if (isFileCreateSuccess) {
                    SQLiteDatabase.openOrCreateDatabase(dbf, null).execSQL(CREATE_TABLE_GT);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            isFileCreateSuccess = true;
        }
        if (isFileCreateSuccess) {
            return SQLiteDatabase.openOrCreateDatabase(dbf, null);
        } else {
            return null;
        }
    }

    public void close() {
        if (dbf.exists()) {
            SQLiteDatabase.openOrCreateDatabase(dbf, null).close();

        }
    }
}
