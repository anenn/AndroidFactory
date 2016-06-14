package com.anenn.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 图片管理工具
 * Created by Anenn on 15-7-23
 */
public class ImageLoaderUtil {

    private static ImageLoader mImageLoader;

    private ImageLoaderUtil() {
    }

    public static void init(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .diskCacheFileCount(300)
                .imageDownloader(new MyImageDownloader(context))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .writeDebugLogs() // Remove for release app
                .diskCacheExtraOptions(0, 0, null);

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config.build());
    }

    public static final DisplayImageOptions AVATAR_OPTIONS = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(GlobalDisplayImage.getImageOnLoading())
            .showImageForEmptyUri(GlobalDisplayImage.getImageForEmptyUri())
            .showImageOnFail(GlobalDisplayImage.getImageOnFail())
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public static void displayImage(String url, ImageView imageView) {
        mImageLoader.displayImage(url, imageView, AVATAR_OPTIONS);
    }

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions imageOptions) {
        mImageLoader.displayImage(url, imageView, imageOptions);
    }

    public static void displayImage(String url, ImageView imageView, SimpleImageLoadingListener loadingListener) {
        mImageLoader.displayImage(url, imageView, AVATAR_OPTIONS, loadingListener);
    }

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions imageOptions,
                                    SimpleImageLoadingListener loadingListener) {
        mImageLoader.displayImage(url, imageView, imageOptions, loadingListener);
    }

    public static void displayImage(String url, ImageView imageView, ImageLoadingProgressListener progressListener) {
        mImageLoader.displayImage(url, imageView, AVATAR_OPTIONS, null, progressListener);
    }

    public static void displayImage(String url, ImageView imageView, SimpleImageLoadingListener loadingListener,
                                    ImageLoadingProgressListener progressListener) {
        mImageLoader.displayImage(url, imageView, AVATAR_OPTIONS, loadingListener, progressListener);
    }

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions imageOptions,
                                    ImageLoadingListener loadingListener, ImageLoadingProgressListener progressListener) {
        mImageLoader.displayImage(url, imageView, imageOptions, loadingListener, progressListener);
    }

    public static void resume() {
        mImageLoader.resume();
    }

    public static void pause() {
        mImageLoader.pause();
    }

    public static void stop() {
        mImageLoader.stop();
    }
}
