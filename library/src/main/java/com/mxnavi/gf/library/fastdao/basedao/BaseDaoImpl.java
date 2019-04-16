package com.mxnavi.gf.library.fastdao.basedao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.mxnavi.gf.library.fastdao.annotation.FastFeild;
import com.mxnavi.gf.library.fastdao.annotation.FastTable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 ：数据库操作类
 *
 * @param <T>
 * @author Mark
 * @date 2019.04.15
 */
public class BaseDaoImpl<T> implements IBaseDao<T> {

    private static final String TAG = "BaseDaoImpl";

    private Class<T> entityClazz;

    /**
     * 数据库句柄
     */
    private SQLiteDatabase sqLiteDatabase;

    /**
     * 是否初始化
     */
    private boolean isInit;

    /**
     * 表明
     */
    private String tableName;

    /**
     * 将列名与bean成员变量 进行一一映射 提高效率
     */
    private HashMap<String, Field> memberMap;

    /**
     * 初始化
     *
     * @param tClass
     * @param sqLiteDatabase
     */
    public boolean init(Class<T> tClass, SQLiteDatabase sqLiteDatabase) {
        entityClazz = tClass;
        this.sqLiteDatabase = sqLiteDatabase;
        if (!isInit) {
            if (!sqLiteDatabase.isOpen()) {
                throw new IllegalStateException("SQLiteDatabase is closed");
            }

            if (!autoCreateTable()) {
                isInit = true;
            }

            createMemberMap();
        }
        return isInit;
    }

    @Override
    public Long insert(T entity) {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        return insertT(entity);
    }

    @Override
    public boolean delete(T entity) {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        return deleteT(entity);
    }

    @Override
    public boolean update(T entity) {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        //TODO 待实现
        return false;
    }

    @Override
    public List<T> query() {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        //TODO 待实现
        return null;
    }

    /**
     * 创建表
     *
     * @return
     */
    private boolean autoCreateTable() {
        tableName = entityClazz.getAnnotation(FastTable.class).value();
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("DB table must not be empty");
        }
        try {
            StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            sqlBuilder.append(tableName);
            sqlBuilder.append("(");
            sqlBuilder.append("id integer primary key autoincrement ,");

            Field[] fields = entityClazz.getDeclaredFields();
            for (Field field : fields) {
                Class<?> clazz = field.getType();
                //字符串类型
                if (clazz == String.class) {
                    sqlBuilder.append(field.getAnnotation(FastFeild.class).value());
                    sqlBuilder.append(" text,");
                }
                //Integer类型
                if (clazz == int.class) {
                    sqlBuilder.append(field.getAnnotation(FastFeild.class).value());
                    sqlBuilder.append(" integer,");
                }
                //Long类型
                if (clazz == long.class) {
                    sqlBuilder.append(field.getAnnotation(FastFeild.class).value());
                    sqlBuilder.append(" long,");
                }
                //byte[]类型
                if (clazz == byte[].class) {
                    sqlBuilder.append(field.getAnnotation(FastFeild.class).value());
                    sqlBuilder.append(" text,");
                }
            }
            sqlBuilder.append(")");
            String sql = sqlBuilder.toString().replace(",)", ")");
            Log.d(TAG, "SQL " + sql);
            sqLiteDatabase.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "autoCreateTable Exception " + e.toString());
            return false;
        }
        return true;
    }

    /**
     * 将列名与bean成员变量 进行一一映射 提高效率
     */
    private void createMemberMap() {
        Cursor cursor = null;
        memberMap = new HashMap<>();
        /**
         * 经典之处 查询空表 建立映射关系 避免数据库升级失败 人为改表造成的Crash
         * 一些主流的开源数据库都是这样处理的 GreenDao
         */
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);
        if (cursor == null) {
            throw new IllegalStateException("createMemberMap faild");
        }
        try {
            String[] columns = cursor.getColumnNames();
            for (String column : columns) {
                Field[] fields = entityClazz.getDeclaredFields();
                for (Field field : fields) {
                    if (column.equals(field.getAnnotation(FastFeild.class).value())) {
                        memberMap.put(column, field);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createMemberMap Exception " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 插入数据
     *
     * @param entity
     * @param <T>
     * @return
     */
    private <T> long insertT(T entity) {
        long result = 0L;
        ContentValues values = createValues(entity);
        sqLiteDatabase.beginTransaction();
        try {
            result = sqLiteDatabase.insert(tableName, null, values);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "insertT exception : " + e.toString());
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return result;
    }

    /**
     * 创建插入数据表
     *
     * @param entity
     * @return
     */
    private <T> ContentValues createValues(T entity) {
        ContentValues values = new ContentValues();
        try {
            for (Map.Entry<String, Field> entry : memberMap.entrySet()) {
                Field field = entry.getValue();
                field.setAccessible(true);
                Object o = field.get(entity);
                Class<?> clazz = field.getType();
                //字符串类型
                if (clazz == String.class) {
                    values.put(entry.getKey(), (String) o);
                }
                //Integer类型
                if (clazz == int.class) {
                    values.put(entry.getKey(), (Integer) o);
                }
                //Long类型
                if (clazz == long.class) {
                    values.put(entry.getKey(), (Long) o);
                }
                //byte[]类型
                if (clazz == byte[].class) {
                    values.put(entry.getKey(), (byte[]) o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createValues exception : " + e.toString());
        }
        return values;
    }

    /**
     * 删除数据
     * @param entity
     * @param <T>
     * @return
     */
    private <T> boolean deleteT(T entity) {
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ");
        sqlBuilder.append(tableName);
        sqLiteDatabase.execSQL(sqlBuilder.toString());
        return false;
    }


}
