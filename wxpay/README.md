** 使用微信进行移动支付，要在AndroidManifest.xml进行相关配置**
在 AndroidManifest.xml 添加相应的权限
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

<!-- wxpay sdk begin -->
        <activity
            android:name=".PayActivity"
            android:exported="false"
            android:launchMode="singleTop">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

            <intent-filter>
                <action android:name="android.intent.action.VIEW"/>
                <category android:name="android.intent.category.DEFAULT"/>
                <data android:scheme="wxf2f565574a968187"/>
            </intent-filter>
        </activity>

        <activity
            android:name="com.weixin.paydemo.wxapi.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop"/>

        <receiver
            android:name="com.anenn.wxpay.AppRegister">
            <intent-filter>
                <action android:name="com.tencent.mm.plugin.openapi.Intent.ACTION_REFRESH_WXAPP" />
            </intent-filter>
        </receiver>
<!-- wxpay sdk end -->