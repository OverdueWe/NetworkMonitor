package com.mxnavi.gf.networkdemo.bean;

import com.mxnavi.gf.library.fastdao.annotation.FastFeild;
import com.mxnavi.gf.library.fastdao.annotation.FastTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试类
 */

@FastTable(value = "UserInfo")
public class UserInfo {

    @FastFeild("name")
    private String name;

    @FastFeild("password")
    private int password;

    @FastFeild("company")
    private Company company;

    @FastFeild("companyList")
    private List<Company> companyList;

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
