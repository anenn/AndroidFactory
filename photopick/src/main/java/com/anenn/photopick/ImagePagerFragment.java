package com.anenn.photopick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anenn.core.common.Constants;
import com.anenn.core.utils.T;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

import javax.microedition.khronos.opengles.GL10;

import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片界面
 * Created by Anenn on 2015/7/30.
 */
public class ImagePagerFragment extends Fragment implements View.OnClickListener {

    private View rootView; // 缓存 View
    private ViewGroup rootLayout; // 图片视图容器
    private View imageLoadFail; // 图片加载失败默认显示控件
    private DonutProgress circleLoading; // 网络请求加载进度提示控件

    private View image;
    // 图片文件
    private File mPhotoFile;
    // 图片Uri
    private String mPhotoUri;
    // 异步请求对象
    private AsyncHttpClient mAsyncHttpClient;

    private static final DisplayImageOptions optionsImage = new DisplayImageOptions
            .Builder()
            .showImageForEmptyUri(Constants.DEFAULT_PHOTO)
            .showImageOnFail(Constants.DEFAULT_PHOTO)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true)
            .cacheInMemory(false)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.NONE)
            .build();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_image_pager_item, container, false);
        } else {
            ViewGroup parent = (ViewGroup) rootLayout.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewById(view);
        initValue();
    }

    private void initViewById(View view) {
        rootLayout = (ViewGroup) view.findViewById(R.id.rootLayout);
        imageLoadFail = view.findViewById(R.id.imageLoadFail);
        circleLoading = (DonutProgress) view.findViewById(R.id.circleLoading);
    }

    private void initValue() {
        mPhotoUri = getArguments().getString("uri");

        rootLayout.setOnClickListener(this);
        showPhoto();
    }

    @Override
    public void onClick(View v) {
        getActivity().onBackPressed();
    }

    private final View.OnClickListener onClickImageClose = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };
    private final PhotoViewAttacher.OnPhotoTapListener onPhotoTapClose = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float v, float v2) {
            getActivity().onBackPressed();
        }
    };

    private final PhotoViewAttacher.OnViewTapListener onViewTapListener = new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float v, float v1) {
            getActivity().onBackPressed();
        }
    };

    public void setData(String uriString) {
        mPhotoUri = uriString;
    }

    private void showPhoto() {
        if (!isAdded()) {
            return;
        }

        ImageSize size = new ImageSize(GL10.GL_MAX_TEXTURE_SIZE, GL10.GL_MAX_TEXTURE_SIZE);
        ImageLoader.getInstance().loadImage(mPhotoUri, size, optionsImage, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        circleLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        if (!isAdded()) {
                            return;
                        }

                        circleLoading.setVisibility(View.GONE);
                        imageLoadFail.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
                        if (!isAdded()) {
                            return;
                        }

                        circleLoading.setVisibility(View.GONE);
                        File file;
                        if (ImageInfo.isLocalFile(mPhotoUri)) {
                            file = new File(ImageInfo.getLocalFilePath(mPhotoUri));
                        } else {
                            file = ImageLoader.getInstance().getDiskCache().get(imageUri);
                        }

                        if (ImageInfo.isGifByFile(file)) {
                            image = getActivity().getLayoutInflater().inflate(R.layout.item_image_gif, null);
                            rootLayout.addView(image);
                            image.setOnClickListener(onClickImageClose);
                        } else {
                            PhotoView photoView = (PhotoView) getActivity().getLayoutInflater()
                                    .inflate(R.layout.item_image_touch, null);
                            image = photoView;
                            rootLayout.addView(image);
                            photoView.setOnPhotoTapListener(onPhotoTapClose);
                            photoView.setOnViewTapListener(onViewTapListener);
                        }

                        image.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                new AlertDialog.Builder(getActivity())
                                        .setItems(new String[]{"保存到手机"}, new OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (which == 0) {
                                                    if (mAsyncHttpClient == null) {
                                                        mAsyncHttpClient = new AsyncHttpClient();
                                                        PersistentCookieStore cookieStore = new PersistentCookieStore(getActivity());
                                                        mAsyncHttpClient.setCookieStore(cookieStore);
                                                        mAsyncHttpClient.addHeader("user-agent", "android");
                                                        mPhotoFile = FileUtil.getDestinationInExternalPublicDir(FileUtil.getFileDownloadPath(getActivity()), mPhotoUri.replaceAll(".*/(.*?)", "$1"));
                                                        mAsyncHttpClient.get(getActivity(), imageUri, new FileAsyncHttpResponseHandler(mPhotoFile) {
                                                            @Override
                                                            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                                                                if (!isResumed()) {
                                                                    return;
                                                                }
                                                                mAsyncHttpClient = null;
                                                                T.t("保存失败");
                                                            }

                                                            @Override
                                                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                                                                if (!isResumed()) {
                                                                    return;
                                                                }
                                                                mAsyncHttpClient = null;
                                                                T.t("图片已保存到:" + file.getPath());
                                                                getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));/**/
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        })
                                        .show();
                                return true;
                            }
                        });

                        if (image instanceof GifImageView) {
                            Uri uri = Uri.fromFile(file);
                            ((GifImageView) image).setImageURI(uri);
                        } else if (image instanceof PhotoView) {
                            ((PhotoView) image).setImageBitmap(loadedImage);
                        }
                    }
                },
                new ImageLoadingProgressListener() {

                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        if (!isAdded()) {
                            return;
                        }

                        int progress = current * 100 / total;
                        circleLoading.setProgress(progress);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (image != null) {
            if (image instanceof GifImageView) {
                ((GifImageView) image).setImageURI(null);
            } else if (image instanceof PhotoView) {
                Bitmap bitmap = ((BitmapDrawable) ((PhotoView) image).getDrawable()).getBitmap();
                if (bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (mAsyncHttpClient != null) {
            mAsyncHttpClient.cancelRequests(getActivity(), true);
            mAsyncHttpClient = null;
        }
        super.onDestroy();
    }
}
