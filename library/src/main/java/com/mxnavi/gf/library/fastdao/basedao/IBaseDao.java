package com.mxnavi.gf.library.fastdao.basedao;

import java.util.List;

/**
 * 描述 ：数据库接口 增 删 改 查
 * @author Mark
 * @date 2019.04.15
 */
public interface IBaseDao<T> {

    /**
     * 插入数据
     * @param entity
     */
    long insert(T entity);

    /**
     * 删除数据
     * @param entity
     */
    boolean delete(T entity);

    /**
     * 修改数据
     * @param entity
     */
    boolean update(T entity);

    /**
     * 查询数据
     */
    List<T> query();

}
