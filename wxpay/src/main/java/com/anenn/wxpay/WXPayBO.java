package com.anenn.wxpay;

import org.json.JSONObject;

/**
 * 微信支付参数实体类
 * Created by Anenn on 2015/11/20.
 */
public class WXPayBO {
    private String appId; // 微信分配的公众账号ID
    private String partnerId; // 微信支付分配的商户号
    private String prepayId; // 微信返回的支付交易会话ID
    private String nonceStr; // 随机字符串，不长于32位
    private String timeStamp; // 时间戳
    private String packageValue = "Sign=WXPay"; // 暂填写固定值Sign=WXPay
    private String sign; // 签名
    private String extData; // 额外信息

    public WXPayBO() {
    }

    public WXPayBO(JSONObject json) {
        if (null != json && !json.has("retcode")) {
            appId = json.optString("appid");
            partnerId = json.optString("partnerid");
            prepayId = json.optString("prepayid");
            nonceStr = json.optString("noncestr");
            timeStamp = json.optString("timestamp");
            sign = json.optString("sign");
        }
    }

    public String getAppId() {
        return appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public String getSign() {
        return sign;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }
}
