package com.anenn.photopick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片缩放工具
 * Created by Anenn on 2015/8/21.
 */
public class PhotoScaleUtil {

    /**
     * 图片比例缩放
     *
     * @param context 应用上下文
     * @param path    图片路径
     * @return 缩放后的图片
     * @throws Exception
     */
    public static File scale(@NonNull Context context, @NonNull String path) throws Exception {
        path = ImageInfo.getLocalFilePath(path);
        File outputFile = new File(path);
        if (isGifFile(path)) {
            return outputFile;
        }

        long fileSize = outputFile.length();
        final long fileMaxSize = 1024 * 1024; // 单位: byte
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = getTempFile(context);
            FileOutputStream fos = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } else {
            File tempFile = outputFile;
            outputFile = getTempFile(context);
            copyFileUsingFileChannels(tempFile, outputFile);
        }
        return outputFile;
    }

    /**
     * 判断是否为Gif文件
     *
     * @param uri 图片Uri
     * @return true 表示为gif文件，反之亦然
     */
    private static boolean isGifFile(String uri) {
        return uri.toLowerCase().endsWith(".gif");
    }

    /**
     * 创建临时文件
     *
     * @return 临时文件对象
     */
    private static File getTempFile(Context context) {
        File file = null;
        try {
            String fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            file = File.createTempFile(fileName, ".jpg", context.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 文件通道转换
     *
     * @param source 原文件
     * @param dest   目标文件
     * @throws IOException 异常
     */
    private static void copyFileUsingFileChannels(File source, File dest)
            throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            if (inputChannel != null)
                inputChannel.close();
            if (outputChannel != null)
                outputChannel.close();
        }
    }
}
