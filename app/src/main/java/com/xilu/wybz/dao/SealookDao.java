package com.xilu.wybz.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xilu.wybz.bean.CzMusicBean;

import java.util.ArrayList;

public class SealookDao {

    SealookDBHelper dbHelper;

    public SealookDao(Context context) {
        dbHelper = new SealookDBHelper(context);
    }

    public void insertCZMUSICBean(CzMusicBean cb) throws Exception {
        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
        try {
            String sql = "insert into music_cz (id, title, lyrics, createtype, hotid, diyids, speed, useheadset, playurl, " +
                    "times, json, recordsize, createtime) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            writeDb.execSQL(sql, new String[]{cb.getId(), cb.getTitle(), cb.getLyrics(), cb.getCreatetype(), cb.getHotid(), cb.getDiyids(),
                    cb.getSpeed(), cb.getUseheadset(), cb.getPlayUrl(), cb.getTimes(), cb.getJson(), cb.getRecordSize(), cb.getCreatetime()});
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("insert data error");
        } finally {
            if (writeDb != null)
                writeDb.close();
        }
    }


    public void delCzMusicBean(String id) throws Exception {
        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
        try {
            String delSql = "delete from music_cz where id = ?;";
            writeDb.execSQL(delSql, new String[]{id});
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("insert data error");
        } finally {
            if (writeDb != null)
                writeDb.close();
        }
    }

    public void insertOrDelCzMusicBean(CzMusicBean cb) throws Exception {
        SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select * from music_cz where id = ?;";
            String insertSql = "insert into music_cz (id, title, lyrics, createtype, hotid, diyids, speed, useheadset, playurl, times, json, " +
                    "recordsize, createtime) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            String delSql = "delete from music_cz where id = ?;";
            cursor = writeDb.rawQuery(sql, new String[]{cb.getId()});
            if (cursor.moveToFirst()) {
                writeDb.execSQL(delSql, new String[]{cb.getId()});
            } else {
                writeDb.execSQL(insertSql, new String[]{cb.getId(), cb.getTitle(), cb.getLyrics(), cb.getCreatetype(), cb.getHotid(), cb.getDiyids(),
                        cb.getSpeed(), cb.getUseheadset(), cb.getPlayUrl(), cb.getTimes(), cb.getJson(), cb.getRecordSize(), cb.getCreatetime()});
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("insert data error");
        } finally {
            if (writeDb != null)
                writeDb.close();
        }
    }

    //查询所有ID的内容
    public ArrayList<CzMusicBean> selectCzMusicBean() throws Exception {
        ArrayList<CzMusicBean> cbList = new ArrayList<CzMusicBean>();
        CzMusicBean cb;
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select id, title, lyrics, createtype, hotid, diyids, speed, useheadset, playurl, times, json, recordsize, createtime from music_cz";
            cursor = readDb.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                cb = new CzMusicBean();
                cb.setId(cursor.getString(0));
                cb.setTitle(cursor.getString(1));
                cb.setLyrics(cursor.getString(2));
                cb.setCreatetype(cursor.getString(3));
                cb.setHotid(cursor.getString(4));
                cb.setDiyids(cursor.getString(5));
                cb.setSpeed(cursor.getString(6));
                cb.setUseheadset(cursor.getString(7));
                cb.setPlayUrl(cursor.getString(8));
                cb.setTimes(cursor.getString(9));
                cb.setJson(cursor.getString(10));
                cb.setRecordSize(cursor.getString(11));
                cb.setCreatetime(cursor.getString(12));
                cbList.add(cb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("select data error");
        } finally {
            if (cursor != null)
                cursor.close();
            if (readDb != null)
                readDb.close();

        }
        return cbList;
    }

    //查询指定ID的内容
    public CzMusicBean selectCzMusicBeanById(String id) throws Exception {
        CzMusicBean cb = null;
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String sql = "select id, title, lyrics, createtype, hotid, diyids, speed, useheadset, playurl, times, json, recordsize, createtime from music_cz " +
                    "where id = ?";
            cursor = readDb.rawQuery(sql, new String[]{id});
            if (cursor.moveToFirst()) {
                cb = new CzMusicBean();
                cb.setId(cursor.getString(0));
                cb.setTitle(cursor.getString(1));
                cb.setLyrics(cursor.getString(2));
                cb.setCreatetype(cursor.getString(3));
                cb.setHotid(cursor.getString(4));
                cb.setDiyids(cursor.getString(5));
                cb.setSpeed(cursor.getString(6));
                cb.setUseheadset(cursor.getString(7));
                cb.setPlayUrl(cursor.getString(8));
                cb.setTimes(cursor.getString(9));
                cb.setJson(cursor.getString(10));
                cb.setRecordSize(cursor.getString(11));
                cb.setCreatetime(cursor.getString(12));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("select data error");
        } finally {
            if (cursor != null)
                cursor.close();
            if (readDb != null)
                readDb.close();

        }
        return cb;
    }
}
