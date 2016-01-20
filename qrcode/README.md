1、使用二维码/条码 Jar 包记得在 AndroidManifest.xml 添加相应权限
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />

    <application
        ...
        >
        <activity android:name="com.anenn.qrcode.MipcaActivityCapture" />
    </application>

2、启动二维码扫描
    Intent intent = new Intent();
    intent.setClass(this, MipcaActivityCapture.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

3、重写 onActivityResult() 方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    mTextView.setText(bundle.getString("result"));
                    //显示
                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }
