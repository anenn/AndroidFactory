package com.anenn.wxpay;

import android.content.Context;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付工具类
 */
public class WXPayUtil {
    // 支付请求对象
    private IWXAPI mWXApi;

    public WXPayUtil(Context context) {
        // 向微信注册APP_ID
        mWXApi = WXAPIFactory.createWXAPI(context, WXPayConstants.getAppId());
        mWXApi.registerApp(WXPayConstants.getAppId());
    }

    /**
     * 发送支付请求
     *
     * @param wxPayBO 请求参数对象
     */
    public void sendReq(WXPayBO wxPayBO) {
        PayReq payReq = new PayReq();
        payReq.appId = wxPayBO.getAppId();
        payReq.partnerId = wxPayBO.getPartnerId();
        payReq.prepayId = wxPayBO.getPrepayId();
        payReq.nonceStr = wxPayBO.getNonceStr();
        payReq.timeStamp = wxPayBO.getTimeStamp();
        payReq.packageValue = wxPayBO.getPackageValue();
        payReq.sign = wxPayBO.getSign();
        if (wxPayBO.getExtData() != null) {
            payReq.extData = wxPayBO.getExtData();
        }
        mWXApi.sendReq(payReq);
    }
}