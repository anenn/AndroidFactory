package com.anenn.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.OauthHelper;

import java.util.Map;
import java.util.Set;

/**
 * 社会化组件回调显示界面
 * Created by Anenn on 2015/11/12.
 */
public abstract class SocialActivity extends AppCompatActivity {

    private ShareManager shareManager;
    private UMSocialService mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        initViewById();
        initViewValue();
    }

    protected abstract int getContentView();

    protected abstract void initViewById();

    protected abstract void initViewValue();

    protected void initSocial() {
        shareManager = ShareManager.getInstance();
        shareManager.initConfig(this);
        mController = shareManager.getController();
    }

    public ShareManager getShareManager() {
        return shareManager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 执行平台授权并获取用户信息
     *
     * @param media 平台型号
     */
    protected void doOAuthInfo(final SHARE_MEDIA media) {
        // 判断是否已经授权
        if (OauthHelper.isAuthenticated(this, media)) {
            getPlatformInfo(media);
        } else {
            mController.doOauthVerify(this, media, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA platform) {
                }

                @Override
                public void onError(SocializeException e, SHARE_MEDIA platform) {
                }

                @Override
                public void onComplete(Bundle value, SHARE_MEDIA platform) {
                    getPlatformInfo(media);
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
                }
            });
        }
    }

    /**
     * 获取授权平台用户的信息
     *
     * @param media
     */
    private final void getPlatformInfo(SHARE_MEDIA media) {
        //获取相关授权信息
        mController.getPlatformInfo(SocialActivity.this, media, new SocializeListeners.UMDataListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                StringBuilder sb = new StringBuilder();
                if (status == 200 && info != null) {
                    Set<String> keys = info.keySet();
                    for (String key : keys) {
                        sb.append(key + "=" + info.get(key).toString() + "\r\n");
                    }
                }
                getPlatformInfo(sb.toString());
            }
        });
    }

    /**
     * 注销平台授权
     *
     * @param media 平台型号
     */
    protected final void doOAuthDelete(final SHARE_MEDIA media) {
        mController.deleteOauth(this, media,
                new SocializeClientListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(int status, SocializeEntity entity) {
                        // status == 200 表示注销授权成功
                        if (status == 200) {
                            if (media == SHARE_MEDIA.QQ) {
                                UMQQSsoHandler qqHandler = (UMQQSsoHandler) mController.getConfig()
                                        .getSsoHandler(HandlerRequestCode.QQ_REQUEST_CODE);
                                if (qqHandler != null) {
                                    qqHandler.cleanQQCache();
                                }
                            }
                        }
                        deleteOAuth(status);
                    }
                });
    }

    /**
     * 获取平台登录信息
     *
     * @param info 用户信息
     */
    protected abstract void getPlatformInfo(String info);

    /**
     * 注销平台授权
     *
     * @param status 状态码
     *               200 表示注销成功
     */
    protected abstract void deleteOAuth(int status);

    @Override
    protected void onDestroy() {
        shareManager.removeListener();
        super.onDestroy();
    }
}
