package com.mxnavi.gf.networkdemo;

import com.mxnavi.gf.library.fastdao.basedao.BaseDaoImpl;
import com.mxnavi.gf.networkdemo.bean.UserInfo;

import java.util.List;

public class UserInfoDao extends BaseDaoImpl {

    public List<UserInfo> queryUsers() {
        return super.query(null, null);
    }

    public boolean deleteUsers() {
        return super.delete(null, null);
    }
}
