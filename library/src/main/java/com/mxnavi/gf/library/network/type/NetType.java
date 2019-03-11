package com.mxnavi.gf.library.network.type;

/**
 * 描述 ： 网络状态类型
 * @author Mark
 * @date 2019.03.11
 */
public enum NetType {

    //有网络 包括Wify Gprs
    AUTO,

    //Wify上网
    WIFI,

    //笔记本 Pad 利用Gprs上网
    CMNET,

    //手机上网
    CMWAP,

    //没有网络
    NONE
}
