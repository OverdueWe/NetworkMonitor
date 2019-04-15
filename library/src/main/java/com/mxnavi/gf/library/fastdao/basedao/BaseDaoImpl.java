package com.mxnavi.gf.library.fastdao.basedao;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * 描述 ：数据库操作类
 *
 * @param <T>
 * @author Mark
 * @date 2019.04.15
 */
public class BaseDaoImpl<T> implements IBaseDao<T> {

    private Class<T> clazz;

    private SQLiteDatabase sqLiteDatabase;

    /**
     * 初始化
     *
     * @param tClass
     * @param sqLiteDatabase
     */
    public void init(Class<T> tClass, SQLiteDatabase sqLiteDatabase) {
        clazz = tClass;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public long insert(T entity) {
        return 1;
    }

    @Override
    public boolean delete(T entity) {
        return false;
    }

    @Override
    public boolean update(T entity) {
        return false;
    }

    @Override
    public List<T> query() {
        return null;
    }

    /**
     * 创建表
     *
     * @return
     */
    private boolean createTable() {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }

        return false;
    }
}
