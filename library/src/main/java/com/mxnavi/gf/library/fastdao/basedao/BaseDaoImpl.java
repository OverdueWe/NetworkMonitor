package com.mxnavi.gf.library.fastdao.basedao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.mxnavi.gf.library.fastdao.annotation.FastFeild;
import com.mxnavi.gf.library.fastdao.annotation.FastTable;
import com.mxnavi.gf.library.fastdao.utils.Serializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    public final boolean init(Class<T> tClass, SQLiteDatabase sqLiteDatabase) {
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
    public final boolean insert(T entity) {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        return insertT(entity);
    }

    @Override
    public final boolean insert(List<T> entities) {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        for (T entity : entities) {
            insertT(entity);
        }
        return true;
    }

    @Override
    public final boolean delete(String whereClause, String[] whereArgs) {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        return deleteT(whereClause, whereArgs);
    }

    @Override
    public final boolean update(T entity, String whereClause, String[] whereArgs) {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        return updateT(entity, whereClause, whereArgs);
    }

    @Override
    public final List<T> query(String sql, String[] selectionArgs) {
        if (!sqLiteDatabase.isOpen()) {
            throw new IllegalStateException("SQLiteDatabase is closed");
        }
        return queryT(sql, selectionArgs);
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
                sqlBuilder.append(field.getAnnotation(FastFeild.class).value());
                if (clazz == String.class) {
                    //字符串类型
                    sqlBuilder.append(" text,");
                } else if (clazz == int.class) {
                    //Integer类型
                    sqlBuilder.append(" integer,");
                } else if (clazz == long.class) {
                    //Long类型
                    sqlBuilder.append(" long,");
                } else {
                    //byte[]类型 及 其他自定义Bean
                    sqlBuilder.append(" blob,");
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
    private <T> boolean insertT(T entity) {
        boolean success = false;
        ContentValues values = createValues(entity);
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.insert(tableName, null, values);
            sqLiteDatabase.setTransactionSuccessful();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "insertT exception : " + e.toString());
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return success;
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
                if (clazz == String.class) {
                    //字符串类型
                    values.put(entry.getKey(), (String) o);
                } else if (clazz == int.class) {
                    //Integer类型
                    values.put(entry.getKey(), (Integer) o);
                } else if (clazz == long.class) {
                    //Long类型
                    values.put(entry.getKey(), (Long) o);
                } else if (clazz == byte[].class) {
                    //byte[]类型
                    values.put(entry.getKey(), (byte[]) o);
                } else {
                    values.put(entry.getKey(), Serializer.toJson(o).getBytes());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createValues exception : " + e.toString());
        }
        return values;
    }

    /**
     * 查询数据
     *
     * @return
     */
    private List<T> queryT(String sql, String[] selectionArgs) {
        Cursor cursor = null;
        List<T> entities = null;
        sqLiteDatabase.beginTransaction();
        try {
            if (TextUtils.isEmpty(sql)) {
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + tableName, null);
            } else {
                cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
            }
            entities = createEntities(cursor);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "queryT Exception : " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.endTransaction();
        }
        return entities;
    }


    /**
     * 生成数据列表
     *
     * @param cursor
     * @return
     */
    public List<T> createEntities(Cursor cursor) throws Exception {
        int count = cursor.getCount();
        List<T> entities = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cursor.moveToNext();
            T entity = entityClazz.newInstance();
            String[] columns = cursor.getColumnNames();
            for (String column : columns) {
                Field field = memberMap.get(column);
                if (field != null) {
                    field.setAccessible(true);
                    Class<?> clazz = field.getType();
                    if (clazz == String.class) {
                        //字符串类型
                        field.set(entity, cursor.getString(cursor.getColumnIndex(column)));
                    } else if (clazz == int.class) {
                        //Integer类型
                        field.set(entity, cursor.getInt(cursor.getColumnIndex(column)));
                    } else if (clazz == long.class) {
                        //Long类型
                        field.set(entity, cursor.getLong(cursor.getColumnIndex(column)));
                    } else if (clazz == byte[].class) {
                        //byte[]类型
                        field.set(entity, cursor.getBlob(cursor.getColumnIndex(column)));
                    } else {
                        field.set(entity,Serializer.get(new String(cursor.getBlob(cursor.getColumnIndex(column))),clazz));
                    }
                }
            }
            entities.add(entity);
        }
        return entities;
    }

    /**
     * 删除数据
     */
    private boolean deleteT(String whereClause, String[] whereArgs) {
        boolean success = false;
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.delete(tableName, whereClause, whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "deleteT Exception : " + e.toString());
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return success;
    }

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    private boolean updateT(T entity, String whereClause, String[] whereArgs) {
        boolean success = false;
        ContentValues contentValues = createValues(entity);
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.update(tableName, contentValues, whereClause, whereArgs);
            sqLiteDatabase.setTransactionSuccessful();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "updateT exception : " + e.toString());
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return success;
    }

}
