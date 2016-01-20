1、在官网下载相应的 Android SDK : https://help.aliyun.com/document_detail/oss/sdk/android-sdk/preface.html?spm=5176.docoss/sdk/sdk-download/android.6.246.uPEJYM
    将下载下来的 SDK 中的 libs 文件夹下的3个 Jar 包依次添加到自己工程的libs文件夹下

2、在 AndroidManifest.xml 配置清单文件中添加相应权限
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

3、