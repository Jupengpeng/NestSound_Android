package com.xilu.wybz.dao;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.FileDir;

import java.util.List;
/**
 * Created by hujunwei on 16/5/19.
 */
public class DBManager {
    public static LiteOrm liteOrm;
    public static void createDb(Context context,Object userId){
        if(liteOrm==null){
            String DB_NAME = FileDir.rootDir + "/data/works_"+userId.toString()+".db";
            DataBaseConfig config = new DataBaseConfig(context, DB_NAME);
            config.debugged = true; // open the log
            config.dbVersion = 1; // set database version
            config.onUpdateListener = null; // set database update listener
            liteOrm = LiteOrm.newCascadeInstance(config);// cascade
        }
    }
    /**
     * 插入所有记录
     * @param list
     */
    public static <T> long insertAll(List<T> list){
        return liteOrm.save(list);
    }
    /**
     * 插入一条记录
     * @param t
     */
    public static <T> long insert(T t){
        return liteOrm.save(t);
    }
    /**
     * 分页查询
     * @param cla
     * @param page
     * @return
     */
    public static <T> List<T> getQueryByPage(Class<T> cla, int page){
        return liteOrm.<T>query(new QueryBuilder(cla).limit((page-1)*20, 20));
    }
    /**
     * 查询所有
     * @param cla
     * @return
     */
    public static <T> List<T> getQueryAll(Class<T> cla){
        return liteOrm.query(cla);
    }

    /**
     * 查询  某字段 等于 Value的值
     * @param cla
     * @param field
     * @param value
     * @return
     */
    public static <T> List<T> getQueryByWhere(Class<T> cla,String field,String [] value){
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value));
    }

    /**
     * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页
     * @param cla
     * @param field
     * @param value
     * @param start
     * @param length
     * @return
     */
    public static <T> List<T> getQueryByWhereLength(Class<T> cla,String field,String [] value,int start,int length){
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value).limit(start, length));
    }

    /**
     * 删除所有 某字段等于 Vlaue的值
     * @param cla
     * @param field
     * @param value
     */
    public static <T> void deleteWhere(Class<T> cla,String field,String [] value){
        liteOrm.delete(cla, WhereBuilder.create(cla).where(field + "=?", value));
    }

    /**
     * 删除所有
     * @param cla
     */
    public static <T> void deleteAll(Class<T> cla){
        liteOrm.deleteAll(cla);
    }

    /**
     * 仅在以存在时更新
     * @param t
     */
    public static <T> long update(T t){
       return liteOrm.update(t, ConflictAlgorithm.Replace);
    }


    public static <T> void updateALL(List<T> list){
        liteOrm.update(list);
    }
    public static long delectWorkData(WorksData worksData){
        return liteOrm.delete(worksData);
    }
    public static long updateWorkData(WorksData worksData){
        return liteOrm.update(worksData);
    }
//    public static List<WorksData> selectWorkDatas(int page,int pageNo){
//        List<WorksData> worksDatas = liteOrm.query(new QueryBuilder<WorksData>(WorksData.class)
//                .distinct(true)
//                .limit((page-1)*pageNo, pageNo)
//                .appendOrderAscBy(WorkDatas.ID));
//        OrmLog.i("WorksData", worksDatas);
//        return worksDatas;
//    }
}
