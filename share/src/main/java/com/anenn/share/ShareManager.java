package com.anenn.share;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 社会化分享管理类
 * Created by Anenn on 2015/11/7.
 */
public class ShareManager {

    private static ShareManager shareManager;

    private UMSocialService mController;
    private static final String wxAppId = "wxc8def3046683f9ff";
    private static final String wxAppSecret = "d4624c36b6795d1d99dcf0547af5443d";
    private static final String qqAppId = "1104952698";
    private static final String qqAppSecret = "iKSE7lBqtgX6Fu46";

    private ShareManager() {
    }

    public static ShareManager getInstance() {
        if (shareManager == null) {
            synchronized (ShareManager.class) {
                if (shareManager == null) {
                    shareManager = new ShareManager();
                }
            }
        }
        return shareManager;
    }

    /**
     * 初始化配置，包括添加分享平台和SSO授权
     *
     * @param activity
     */
    public void initConfig(Activity activity) {
        // 首先在您的Activity中添加如下成员变量
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        // 设置分享平台的位置顺序
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA);

        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // 添加微信和微信朋友圈分享
        addWXPlatform(activity);
        // 添加QQ和QQZone分享
        addQQPlatform(activity);
    }

    /**
     * 微信和微信朋友分享
     * 微信和朋友圈支持纯文字，纯图片（点击查看大图），图文，音乐，视频分享。
     *
     * @param context 应用上下文
     */
    private final void addWXPlatform(Context context) {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, wxAppId, wxAppSecret);
        wxHandler.showCompressToast(false);
        wxHandler.addToSocialSDK();

        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, wxAppId, wxAppSecret);
        wxCircleHandler.showCompressToast(false);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * QQ好友分享
     * QQ分享内容为音乐，视频的时候，其形式必须为url;图片支持url和本地图片类型.
     * QQ SSO授权不会在onActivityResult方法内被调用
     * 未安装手机QQ客户端的情况下，QQ不支持纯图片分享.
     * 未安装手机QQ客户端的情况下，QQ取消授权不执行相关回调。
     * QQ和QQ空间授权，对于同一个应用同一个账号返回的uid，openid相同。
     * <p/>
     * QQZone空间分享
     * QZone不支持纯图片分享。
     * 无手机QQ客户端的情况下，QQ取消授权不执行相关回调。
     * QQ和QQ空间授权，对于同一个应用同一个账号返回的uid，openid相同。
     *
     * @param activity 当前的Activity
     */
    private final void addQQPlatform(Activity activity) {
        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, qqAppId, qqAppSecret);
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, qqAppId, qqAppSecret);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * 获取友盟分享控制器
     *
     * @return
     */
    public UMSocialService getController() {
        return mController;
    }

    /**
     * 打开友盟的分享面板
     *
     * @param activity
     */
    public void openShare(Activity activity) {
        // 是否只有已登录用户才能打开分享选择页
        mController.openShare(activity, false);
    }

    /**
     * 设置分享内容
     * 新浪微博、腾讯微博及豆瓣的跳转链接只能设置在分享文字之中，以http形式传递即可，人人网可以单独设置跳转链接
     * 如：mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
     *
     * @param context
     * @param content
     * @param imageUrl
     */
    public final void setContentToShare(Context context, String title, String content,
                                        String imageUrl, String targetUrl) {
        // 设置默认平台分享的内容
        mController.setShareContent(content);
        // 设置默认平台分享的图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(context, imageUrl));
        // 微信
        shareToWX(context, title, content, imageUrl, targetUrl);
        // 微信朋友圈
        shareToWXCircle(context, title, content, imageUrl, targetUrl);
        // QQ
        shareToQQ(context, title, content, imageUrl, targetUrl);
        // QQZone
        shareToQQZone(context, title, content, imageUrl, targetUrl);
    }


    /**
     * 分享内容到微信好友
     * 微信分享必须设置targetURL，需要为http链接格式
     * 微信朋友圈只能显示title，并且过长会被微信截取部分内容
     *
     * @param context
     * @param title
     * @param content
     * @param targetUrl
     * @param imageUrl
     */
    private final void shareToWX(Context context, String title, String content,
                                 String imageUrl, String targetUrl) {
        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        if (!TextUtils.isEmpty(content))
            weixinContent.setShareContent(content);
        //设置title
        weixinContent.setTitle(title);
        //设置分享内容跳转URL
        weixinContent.setTargetUrl(targetUrl);
        //设置分享图片
        if (!TextUtils.isEmpty(imageUrl)) {
            weixinContent.setShareImage(new UMImage(context, imageUrl));
        }

        mController.setShareMedia(weixinContent);
    }

    /**
     * 分享内容到微信朋友圈
     * 微信分享必须设置targetURL，需要为http链接格式
     * 微信朋友圈只能显示title，并且过长会被微信截取部分内容
     *
     * @param context
     * @param title
     * @param content
     * @param targetUrl
     * @param imageUrl
     */
    private final void shareToWXCircle(Context context, String title, String content,
                                       String imageUrl, String targetUrl) {
        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        //设置朋友圈title
        circleMedia.setTitle(title);
        //设置分享文字
        if (!TextUtils.isEmpty(content))
            circleMedia.setShareContent(content);
        //设置分享内容跳转URL
        circleMedia.setTargetUrl(targetUrl);
        //设置分享图片
        if (!TextUtils.isEmpty(imageUrl)) {
            circleMedia.setShareImage(new UMImage(context, imageUrl));
        }

        mController.setShareMedia(circleMedia);
    }

    /**
     * 分享内容到QQ
     * 使用纯图片分享必须使用本地图片，不可以使用URL网络图片
     *
     * @param context
     * @param title
     * @param content
     * @param imageUrl
     * @param targetUrl
     */
    private final void shareToQQ(Context context, String title, String content,
                                 String imageUrl, String targetUrl) {
        QQShareContent qqShareContent = new QQShareContent();
        //设置分享title
        qqShareContent.setTitle(title);
        //设置分享文字
        if (!TextUtils.isEmpty(content))
            qqShareContent.setShareContent(content);
        //设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl(targetUrl);
        //设置分享图片
        if (!TextUtils.isEmpty(imageUrl)) {
            qqShareContent.setShareImage(new UMImage(context, imageUrl));
        }

        mController.setShareMedia(qqShareContent);
    }

    /**
     * 分享内容到QQZone
     *
     * @param context
     * @param title
     * @param content
     * @param imageUrl
     * @param targetUrl
     */
    private final void shareToQQZone(Context context, String title, String content,
                                     String imageUrl, String targetUrl) {
        QZoneShareContent qzone = new QZoneShareContent();
        //设置分享内容的标题
        qzone.setTitle(title);
        //设置分享文字
        if (!TextUtils.isEmpty(content))
            qzone.setShareContent(content);
        //设置点击消息的跳转URL
        qzone.setTargetUrl(targetUrl);
        //设置分享图片
        if (!TextUtils.isEmpty(imageUrl)) {
            qzone.setShareImage(new UMImage(context, imageUrl));
        }

        mController.setShareMedia(qzone);
    }

    /**
     * 分享音频资源
     * 分享音乐只支持URL封装的音乐资源，不支持本地音乐
     *
     * @param title
     * @param content
     * @param author
     * @param thumbUrl
     * @param audioUrl
     */
    public final void shareAudio(String title, String content, String author, String thumbUrl, String audioUrl) {
        UMusic uMusic = new UMusic(audioUrl);
        uMusic.setTitle(title);
        uMusic.setAuthor(author);
        uMusic.setThumb(thumbUrl);

        WeiXinShareContent weiXinShareContent = new WeiXinShareContent(uMusic);
        weiXinShareContent.setShareContent(content);
        CircleShareContent circleShareContent = new CircleShareContent(uMusic);
        circleShareContent.setShareContent(content);
        QQShareContent qqShareContent = new QQShareContent(uMusic);
        qqShareContent.setShareContent(content);
        QZoneShareContent qZoneShareContent = new QZoneShareContent(uMusic);
        qqShareContent.setShareContent(content);
        SinaShareContent sinaShareContent = new SinaShareContent(uMusic);
        sinaShareContent.setShareContent(content);
        TencentWbShareContent tencentContent = new TencentWbShareContent(uMusic);
        tencentContent.setShareContent(content);

        mController.setShareMedia(weiXinShareContent);
        mController.setShareMedia(circleShareContent);
        mController.setShareMedia(qqShareContent);
        mController.setShareMedia(qZoneShareContent);
        mController.setShareMedia(sinaShareContent);
        mController.setShareMedia(tencentContent);
    }

    /**
     * 分享视频资源
     * 分享视频资源同样只支持URL资源
     *
     * @param title
     * @param content
     * @param thumbUrl
     * @param videoUrl
     */
    public final void shareVideo(String title, String content, String thumbUrl, String videoUrl) {
        UMVideo umVideo = new UMVideo(videoUrl);

        umVideo.setThumb(thumbUrl);

        WeiXinShareContent weiXinShareContent = new WeiXinShareContent(umVideo);
        weiXinShareContent.setShareContent(content);
        CircleShareContent circleShareContent = new CircleShareContent(umVideo);
        circleShareContent.setShareContent(content);
        QQShareContent qqShareContent = new QQShareContent(umVideo);
        qqShareContent.setShareContent(content);
        QZoneShareContent qZoneShareContent = new QZoneShareContent(umVideo);
        qqShareContent.setShareContent(content);
        SinaShareContent sinaShareContent = new SinaShareContent(umVideo);
        sinaShareContent.setShareContent(content);
        TencentWbShareContent tencentContent = new TencentWbShareContent(umVideo);
        tencentContent.setShareContent(content);

        mController.setShareMedia(weiXinShareContent);
        mController.setShareMedia(circleShareContent);
        mController.setShareMedia(qqShareContent);
        mController.setShareMedia(qZoneShareContent);
        mController.setShareMedia(sinaShareContent);
        mController.setShareMedia(tencentContent);
        mController.setShareMedia(umVideo);
    }

    /**
     * 移除监听器，释放资源
     */
    public final void removeListener() {
        mController.getConfig().cleanListeners();
    }
}
