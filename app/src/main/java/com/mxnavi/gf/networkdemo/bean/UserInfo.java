package com.mxnavi.gf.networkdemo.bean;

import com.mxnavi.gf.library.fastdao.annotation.FastFeild;
import com.mxnavi.gf.library.fastdao.annotation.FastTable;

/**
 * 测试类
 */

@FastTable(value = "UserInfo")
public class UserInfo {

    @FastFeild(value = "name")
    private String name;

    @FastFeild(value = "password")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
