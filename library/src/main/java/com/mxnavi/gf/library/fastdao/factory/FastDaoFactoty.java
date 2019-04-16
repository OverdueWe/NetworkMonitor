package com.mxnavi.gf.library.fastdao.factory;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.mxnavi.gf.library.fastdao.basedao.BaseDaoImpl;
import com.mxnavi.gf.library.fastdao.basedao.IBaseDao;
import com.mxnavi.gf.library.fastdao.utils.FastDaoConstant;

import java.io.File;

/**
 * 描述 ：数据库工厂类
 *
 * @author Mark
 * @date 2019.04.15
 */
public class FastDaoFactoty {

    private SQLiteDatabase sqLiteDatabase;

    public FastDaoFactoty() {
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + FastDaoConstant.FASTDAO_PATH, null);
    }

    /**
     * 根据数据类型创建对应的数据库
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    public <T> IBaseDao createDao(Class<T> entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        if (sqLiteDatabase == null) {
            throw new IllegalStateException("SQLiteDatabase is init faild");
        }

        try {
            BaseDaoImpl baseDao = BaseDaoImpl.class.newInstance();
            baseDao.init(entityClass, sqLiteDatabase);
            return baseDao;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }
}
