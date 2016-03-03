package com.anenn.alipay;

/**
 * 订单实体类
 * Created by Anenn on 2015/11/4.
 */
public class OrderBO {
    private String out_trade_no; // 商户网站唯一订单号
    private String subject; // 商品名称
    private String body; // 商品详情
    private double price; // 商品金额

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
