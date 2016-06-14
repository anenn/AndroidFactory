# AliPayUtil 支付宝移动支付工具类
**使用支付宝进行移动支付，首先要在AndroidManifest.xml进行相关配置**

在 AndroidManifest.xml 添加相应的权限：

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

同时申明 **AliPay** 相关的 Activity： 

```
<!-- alipay sdk begin -->
<activity
    android:name="com.alipay.sdk.app.H5PayActivity"
    android:configChanges="orientation|keyboardHidden|navigation"
    android:exported="false"
    android:screenOrientation="behind"
    android:windowSoftInputMode="adjustResize|stateHidden" >
</activity>

<activity
    android:name="com.alipay.sdk.auth.AuthActivity"
    android:configChanges="orientation|keyboardHidden|navigation"
    android:exported="false"
    android:screenOrientation="behind" >
 </activity>
<!-- alipay sdk end -->
```

完成上述流程后，在需要支付的地方直接调用 AliPayUtil 的 pay() 方法就可以进行在线支付了。我们通过在构造器中绑定回调接口 IAliPayCallback，这样可以监听支付状态：

```
public interface IAliPayCallback {
    // 正在支付
    void aliPaying();

    // 支付成功
    void aliPaySuccess();

    // 支付失败
    void aliPayFailed();
}
```


针对 release 版本中涉及混淆问题，往 proguard-rules.pro 文件添加如下配置：

```
#---------------- 支付宝 设置 ----------------#
#-libraryjars libs/alipaySdk.jar 根据你具体 jar 包的位置放置情况而定，因为这里我是直接将 AliPay Module 编译成 aar 文件，所以就不需要了
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
#---------------- 支付宝 设置 ----------------#
```
