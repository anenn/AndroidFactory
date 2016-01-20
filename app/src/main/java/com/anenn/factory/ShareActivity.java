package com.anenn.factory;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.anenn.share.SocialActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Anenn on 2015/11/8.
 */
public class ShareActivity extends SocialActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_share;
    }

    @Override
    protected void initViewById() {
    }

    @Override
    protected void initViewValue() {
        initSocial();
    }

    @Override
    protected void getPlatformInfo(String info) {
        if (!TextUtils.isEmpty(info))
            Log.i("ShareActivity", info);
    }

    @Override
    protected void deleteOAuth(int status) {
        if (status == 200) {
            Log.i("ShareActivity", "注销成功");
        } else {
            Log.i("ShareActivity", "注销失败");
        }
    }

    public void toQQOAuth(View view) {
        doOAuthInfo(SHARE_MEDIA.QQ);
    }

    public void toSinaOAuth(View view) {
        doOAuthInfo(SHARE_MEDIA.SINA);
    }

    public void toQQOAuthDel(View view) {
        doOAuthDelete(SHARE_MEDIA.QQ);
    }

    public void toSinaOAuthDel(View view) {
        doOAuthDelete(SHARE_MEDIA.SINA);
    }

    public void toShare(View view) {
        getShareManager().setContentToShare(this, "玩哈哈", "asdf", "http://img4.imgtn.bdimg.com/it/u=2672581030,62686104&fm=21&gp=0.jpg", "http://blog.anenn.cn/");
        getShareManager().openShare(this);
    }
}
