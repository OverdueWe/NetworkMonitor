package com.mxnavi.gf.library.fastdao.basedao;

import java.util.List;

/**
 * 描述 ：数据库接口 增 删 改 查
 *
 * @author Mark
 * @date 2019.04.15
 */
public interface IBaseDao<T> {

    /**
     * 插入数据
     *
     * @param entity
     */
    boolean insert(T entity);

    /**
     * 插入数据表
     * @param entities
     * @return
     */
    boolean insert(List<T> entities);

    /**
     * 删除数据
     *
     * @return
     */
    boolean delete(String whereClause, String[] whereArgs);

    /**
     * 修改数据
     *
     * @return
     */
    boolean update(T entity, String whereClause, String[] whereArgs);

    /**
     * 查询数据
     *
     * @return
     */
    List<T> query(String sql, String[] selectionArgs);

}
