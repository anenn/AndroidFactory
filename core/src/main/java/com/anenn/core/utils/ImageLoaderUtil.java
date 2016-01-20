package com.anenn.core.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.anenn.core.Global;
import com.anenn.core.MyImageDownloader;
import com.anenn.core.common.Constants;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 图片管理工具
 * Created by Anenn on 15-7-23
 */
public class ImageLoaderUtil {
    private static ImageLoader imageLoader;

    private ImageLoaderUtil() {
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .diskCacheFileCount(300)
                .imageDownloader(new MyImageDownloader(context))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.writeDebugLogs() // Remove for release app
                .diskCacheExtraOptions(0, 0, null)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    public static final DisplayImageOptions OPTIONS = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(Constants.DEFAULT_PHOTO)
            .showImageForEmptyUri(Constants.DEFAULT_PHOTO)
            .showImageOnFail(Constants.DEFAULT_PHOTO)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    public static final DisplayImageOptions AVATAR_OPTIONS = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(Constants.DEFAULT_PHOTO)
            .showImageForEmptyUri(Constants.DEFAULT_PHOTO)
            .showImageOnFail(Constants.DEFAULT_PHOTO)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    //两像素圆角
    public static final DisplayImageOptions optionsRounded = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(Constants.DEFAULT_PHOTO)
            .showImageForEmptyUri(Constants.DEFAULT_PHOTO)
            .showImageOnFail(Constants.DEFAULT_PHOTO)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(2))
            .build();

    //两dp圆角
    public static final DisplayImageOptions optionsRounded2 = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(Constants.DEFAULT_PHOTO)
            .showImageForEmptyUri(Constants.DEFAULT_PHOTO)
            .showImageOnFail(Constants.DEFAULT_PHOTO)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(Global.dp2px(2)))
            .build();

    public static void loadImage(String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url))
            imageLoader.displayImage(url, imageView, AVATAR_OPTIONS);
    }

    public static void loadImage(String url, ImageView imageView, DisplayImageOptions imageOptions) {
        if (!TextUtils.isEmpty(url))
            imageLoader.displayImage(url, imageView, imageOptions);
    }

    public static void loadImage(String url, ImageView imageView, ImageLoadingListener listener) {
        if (!TextUtils.isEmpty(url))
            imageLoader.displayImage(url, imageView, AVATAR_OPTIONS, listener);
    }

    public static void loadImage(String url, ImageView imageView, DisplayImageOptions imageOptions,
                                 SimpleImageLoadingListener listener) {
        if (!TextUtils.isEmpty(url))
            imageLoader.displayImage(url, imageView, imageOptions, listener);
    }

    private static class MyImageViewAware extends ImageViewAware {

        public MyImageViewAware(ImageView imageView) {
            super(imageView);
        }

        @Override
        public int getWidth() {
            super.getWidth();
            return 0;
        }

        @Override
        public int getHeight() {
            super.getHeight();
            return 0;
        }
    }
}
