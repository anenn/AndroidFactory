package com.anenn.photopick;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.anenn.core.utils.DialogUtil;
import com.anenn.core.utils.T;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.anenn.photopick.ImagePagerActivity.DEL_URIS;
import static com.anenn.photopick.ImagePagerActivity.EDITABLE;
import static com.anenn.photopick.ImagePagerActivity.PAGER_POSITION;
import static com.anenn.photopick.ImagePagerActivity.SHOW_URIS;

/**
 * 图片管理器
 * Created by Anenn on 2015/7/29
 */
public abstract class PhotoManager {
    public int MAX_LIMIT = 6; // 最多允许上传图片数量
    private final int REQUEST_MULTI_PHOTO = 1000; // 多张图片选择回调标志位
    private final int REQUEST_SINGLE_PHOTO = 1001; // 单张图片选择回调标志位
    private final int REQUEST_PHOTO_CROP = 1002; // 图片裁剪回调标志位
    private final int REQUEST_VIEW_IMAGE = 1003; // 查看图片原图回调标志位

    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;

    // 原始图片的Uri
    private Uri fileUri;
    // 裁剪后的图片的Uri
    private Uri fileCropUri;
    // 输出图片的宽
    private int outputX = 640;
    // 输出图片的高
    private int outputY = 640;
    // 图片的缩放比例
    private int aspectX = 1;
    private int aspectY = 1;
    // 图片缩放使能位
    private boolean isAspect;
    // 图片进行裁剪使能位
    private boolean isNeedCrop;
    // 图片裁剪限制条件
    private boolean isCropLimit;

    public PhotoManager(Activity activity) {
        this.mContext = activity;
        this.mActivity = activity;
    }

    public PhotoManager(Fragment fragment) {
        this.mContext = fragment.getActivity();
        this.mFragment = fragment;
    }

//    /**
//     * 设置图片最多选择的数目
//     *
//     * @param limit 图片的最多数目
//     */
//    private void setMaxLimit(int limit) {
//        MAX_LIMIT = limit;
//    }
//
//    /**
//     * 设置图片裁剪的宽、高
//     *
//     * @param x 宽
//     * @param y 高
//     */
//    private void setCropXY(int x, int y) {
//        isNeedCrop = true;
//        isCropLimit = true;
//        isAspect = false;
//        outputX = x;
//        outputY = y;
//    }
//
//    /**
//     * 设置图片的宽、高缩放比例
//     *
//     * @param x 轴
//     * @param y 轴
//     */
//    private void setAspectXY(int x, int y) {
//        isNeedCrop = true;
//        isCropLimit = true;
//        isAspect = true;
//        aspectX = x;
//        aspectY = y;
//    }
//
//    /**
//     * 设置是否可对图片进行裁剪
//     *
//     * @param isNeedCrop true 表示对图片进行裁剪，反之亦然
//     */
//    private void setNeedCrop(boolean isNeedCrop) {
//        this.isNeedCrop = isNeedCrop;
//    }
//
//    /**
//     * 是否限制图片的裁剪功能
//     *
//     * @param cropLimit true 表示根据缩放比例和宽高对图片进行裁剪，false 表示不做任何限制
//     */
//    private void setCropLimit(boolean cropLimit) {
//        isCropLimit = cropLimit;
//        if (cropLimit)
//            isNeedCrop = true;
//    }
//
//    /**
//     * 弹出图片选择窗口，针对多张图片选择
//     *
//     * @param imageInfoList 已选择的图片数据集
//     */
//    private final void showMultiDialog(ArrayList<ImageInfo> imageInfoList) {
//        if (imageInfoList == null) {
//            imageInfoList = new ArrayList<>();
//        }
//
//        int other = MAX_LIMIT - imageInfoList.size();
//        if (other <= 0) {
//            T.t(String.format("最多只能添加%d张图片", MAX_LIMIT));
//            return;
//        }
//
//        Intent intent = new Intent(mContext, PhotoPickActivity.class);
//        intent.putExtra(PhotoPickActivity.EXTRA_MAX, MAX_LIMIT);
//        intent.putParcelableArrayListExtra(PhotoPickActivity.EXTRA_PICKED, imageInfoList);
//        if (mFragment != null) {
//            mFragment.startActivityForResult(intent, REQUEST_MULTI_PHOTO);
//        } else if (mActivity != null) {
//            mActivity.startActivityForResult(intent, REQUEST_MULTI_PHOTO);
//        }
//    }

    /**
     * 弹出选择图片窗口，针对单张图片选择
     */
    public final void showSingleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("选择图片")
                .setItems(R.array.camera_gallery,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    camera();
                                } else {
                                    gallery();
                                }
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
        DialogUtil.dialogTitleLineColor(mContext, dialog);
    }

    /**
     * 调用系统相册
     */
    private final void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, REQUEST_SINGLE_PHOTO);
        } else if (mActivity != null) {
            mActivity.startActivityForResult(intent, REQUEST_SINGLE_PHOTO);
        }
    }

    /**
     * 调用照相机
     */
    public final void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = CameraPhotoUtil.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, REQUEST_SINGLE_PHOTO);
        } else if (mActivity != null) {
            mActivity.startActivityForResult(intent, REQUEST_SINGLE_PHOTO);
        }
    }

    /**
     * 查看图片
     *
     * @param photoList 图片数据集
     * @param position  选中图片的索引值
     * @param needEdit  是否需要编辑
     */
    public final void reviewImages(List<String> photoList, int position, boolean needEdit) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(SHOW_URIS, (ArrayList<String>) photoList);
        intent.putExtra(PAGER_POSITION, position);
        intent.putExtra(EDITABLE, needEdit);
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, REQUEST_VIEW_IMAGE);
        } else if (mActivity != null) {
            mActivity.startActivityForResult(intent, REQUEST_VIEW_IMAGE);
        }
    }

    public final void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_MULTI_PHOTO) {
                if (data != null) {
                    ArrayList<String> imageInfoList = data.getStringArrayListExtra("data");
                    obtainMultiPhoto(imageInfoList);
                }
            } else if (requestCode == REQUEST_SINGLE_PHOTO) {
                if (data != null) {
                    Uri temp = data.getData();
                    if (temp != null)
                        fileUri = temp;
                }
                if (!isNeedCrop) {
                    try {
                        String filePath = CameraPhotoUtil.getPath(mContext, fileUri);
                        if (!TextUtils.isEmpty(filePath)) {
                            File file = PhotoScaleUtil.scale(mContext, filePath);
                            obtainSinglePhoto(file.getAbsolutePath());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    fileCropUri = CameraPhotoUtil.getOutputMediaFileUri();
                    cropImageUri(fileUri, fileCropUri, REQUEST_PHOTO_CROP);
                }
            } else if (requestCode == REQUEST_PHOTO_CROP) {
                String filePath = CameraPhotoUtil.getPath(mContext, fileCropUri);
                obtainSinglePhoto(filePath);
            } else if (requestCode == REQUEST_VIEW_IMAGE) {
                if (data != null) {
                    ArrayList<String> delUris = data.getStringArrayListExtra(DEL_URIS);
                    viewImagesCallback(delUris);
                }
            }
        }
    }

    public void obtainMultiPhoto(List<String> imageInfoList) {
    }

    public abstract void obtainSinglePhoto(String path);

    public abstract void viewImagesCallback(List<String> uriList);

    /**
     * 自定义裁剪后图片的大小
     *
     * @param srcUri      源图片Uri
     * @param outputUri   输出图片Uri
     * @param requestCode 请求标志位
     */
    private void cropImageUri(Uri srcUri, Uri outputUri, int requestCode) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(srcUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            intent.putExtra("crop", "true");
            if (isCropLimit) {
                if (isAspect) {
                    intent.putExtra("aspectX", aspectX);
                    intent.putExtra("aspectY", aspectY);
                }
                intent.putExtra("outputX", outputX);
                intent.putExtra("outputY", outputY);
            }
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", true);
            if (mFragment != null) {
                mFragment.startActivityForResult(intent, requestCode);
            } else if (mActivity != null) {
                mActivity.startActivityForResult(intent, requestCode);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            T.t("当前手机不支持图片裁剪功能");
        }
    }

    /**
     * 释放资源
     */
    public final void onDestroy() {
        File file = new File(CameraPhotoUtil.getCachePath());
        FileUtil.deleteDirAllFile(file.getAbsolutePath(),
                FileUtil.FILE_DEL_ALL);
    }
}
