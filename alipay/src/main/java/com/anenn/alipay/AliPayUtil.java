package com.anenn.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 支付宝移动支付工具类
 * Created by Anenn on 2015/11/4.
 */
public class AliPayUtil implements Callback {

    private static final int MSG_PAY = 10; // 支付处理标志位
    private static final int MSG_LOGIN = 11; // 检查本地是否有可用的支付宝账号

    // 当前环境
    private Activity mActivity;
    // 支付宝支付请求回调
    private IAliPayCallback mIAliPayCallback;
    // 消息处理器
    private WeakHandler mWeakHandler;

    /**
     * 初始化对象
     *
     * @param activity 应用上下文
     */
    public AliPayUtil(@NonNull Activity activity, IAliPayCallback iAliPayCallback) {
        mActivity = activity;
        mIAliPayCallback = iAliPayCallback;
        mWeakHandler = new WeakHandler(this);
    }

    /**
     * 消息处理
     *
     * @param msg 消息对象
     * @return 是否消费消息
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case AliPayUtil.MSG_PAY: {
                PayResult payResult = new PayResult((String) msg.obj);

                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                // TODO 签名验证
                String resultInfo = payResult.getResult();
                String resultStatus = payResult.getResultStatus();

                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    if (mIAliPayCallback != null) {
                        mIAliPayCallback.aliPaySuccess();
                    }
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        if (mIAliPayCallback != null) {
                            mIAliPayCallback.aliPaying();
                        }
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        if (mIAliPayCallback != null) {
                            mIAliPayCallback.aliPayFailed();
                        }
                    }
                }
                break;
            }
            case AliPayUtil.MSG_LOGIN: {
                Toast.makeText(mActivity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return true;
    }

    /**
     * 处理订单支付, 建议将签名加密放在服务端执行
     *
     * @param aliPayBO 支付信息
     * @param orderBO  订单信息
     * @deprecated Use {@link #pay(String)} instead
     */
    @Deprecated
    public void pay(AliPayBO aliPayBO, OrderBO orderBO) {
        // 创建订单信息
        String orderInfo = createOrderInfo(orderBO, aliPayBO);
        // 对订单信息进行RSA签名加密
        String sign = sign(orderInfo, aliPayBO.getPrivate_key());
        try {
            // 仅需对 sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        pay(payInfo);
    }

    /**
     * 启动支付
     *
     * @param payInfo 支付信息
     */
    public void pay(final String payInfo) {
        // 启动支付
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造 PayTask 对象
                PayTask aliPay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果, 必须异步调用
                String result = aliPay.pay(payInfo);

                if (mWeakHandler != null) {
                    Message msg = mWeakHandler.obtainMessage();
                    msg.what = MSG_PAY;
                    msg.obj = result;
                    mWeakHandler.sendMessage(msg);
                }
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 封装订单信息
     *
     * @param orderBO  订单信息
     * @param aliPayBO 支付信息
     * @return 支付参数
     */
    private String createOrderInfo(OrderBO orderBO, AliPayBO aliPayBO) {
        StringBuilder sb = new StringBuilder();
        // 签约合作者身份ID
        sb.append("partner=\"");
        sb.append(aliPayBO.getPid());
        // 签约卖家支付宝账号
        sb.append("\"&seller_id=\"");
        sb.append(aliPayBO.getAccount());
        // 商户网站唯一订单号
        sb.append("\"&out_trade_no=\"");
        sb.append(orderBO.getOut_trade_no());
        // 商品名称
        sb.append("\"&subject=\"");
        sb.append(orderBO.getSubject());
        // 商品详情
        sb.append("\"&body=\"");
        sb.append(orderBO.getBody());
        // 商品金额
        sb.append("\"&total_fee=\"");
        sb.append(orderBO.getPrice());
        // 服务器异步通知页面路径，网址需要做URL编码
        if (!TextUtils.isEmpty(aliPayBO.getNotify_url())) {
            sb.append("\"&notify_url=\"");
            sb.append(aliPayBO.getNotify_url());
        }
        // 服务器接口名称，固定值
        sb.append("\"&service=\"mobile.securitypay.pay");
        // 支付类型，固定值
        sb.append("\"&payment_type=\"1");
        // 参数编码，固定值
        sb.append("\"&_input_charset=\"UTF-8");
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        sb.append("\"&it_b_pay=\"");
        sb.append(aliPayBO.getOverTime());
        sb.append("m");
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        // sb.append("\"&return_url=\"");
        // sb.append(URLEncoder.encode("http://m.alipay.com", "utf-8"));

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        //如果show_url值为空，可不传
        //sb.append("\"&show_url=\"");

        sb.append("\"");

        return sb.toString();
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content, String privateKey) {
        return SignUtils.sign(content, privateKey);
    }

    /**
     * 获取sign_type参数信息，因为该参数不需要参加签名
     * 获取签名方式
     *
     * @return 签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void checkUser() {
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(mActivity);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                if (mWeakHandler != null) {
                    Message msg = mWeakHandler.obtainMessage();
                    msg.what = AliPayUtil.MSG_LOGIN;
                    msg.obj = isExist;
                    mWeakHandler.sendMessage(msg);
                }
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }

    /**
     * get the out_trade_no for an order.
     * 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOrderNumber() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * 释放资源
     */
    public void onDestroy() {
        if (mWeakHandler != null) {
            mWeakHandler.removeMessages(AliPayUtil.MSG_PAY);
            mWeakHandler.removeMessages(AliPayUtil.MSG_LOGIN);
            mWeakHandler = null;
        }
    }

    /**
     * 支付宝支付请求回调接口
     */
    public interface IAliPayCallback {
        // 正在支付
        void aliPaying();

        // 支付成功
        void aliPaySuccess();

        // 支付失败
        void aliPayFailed();
    }

    /**
     * 弱引用的消息处理器
     */
    private static class WeakHandler extends Handler {
        private WeakReference<Callback> reference = null;

        public WeakHandler(Callback callback) {
            reference = new WeakReference<>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Callback callback = reference.get();
            if (callback != null) {
                callback.handleMessage(msg);
            }
        }
    }
}
