package com.anenn.alipay;

/**
 * 支付宝移动支付实体类
 * Created by Anenn on 2015/11/4.
 */
public class AliPayBO {
    private String pid; // 签约合作者身份ID
    private String account; // 签约卖家支付宝账号
    private String notify_url; // 服务器异步通知页面路径，网址需要做URL编码
    private String private_key; // 商户私钥，Java语言则采用PKCS8格式
    private String public_key; // 支付宝公钥
    private int overTime = 30; // 支付超时时间,默认为30m

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public int getOverTime() {
        return overTime;
    }

    public void setOverTime(int overTime) {
        this.overTime = overTime;
    }
}
