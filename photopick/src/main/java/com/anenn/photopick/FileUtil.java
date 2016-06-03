package com.anenn.photopick;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 文件工具类
 * <p/>
 * Created by Anenn on 2015/8/21.
 */
public class FileUtil {
    public final static int FILE_DEL_ALL = 1;
    public final static int FILE_IGNORE_DIR = 2;

    private final static String DOWNLOAD_FOLDER = "Anenn";
    private final static String DOWNLOAD_PATH = "download_path";
    private final static String DOWNLOAD_SETTING = "download_setting";

    /**
     * 保存对象
     *
     * @param context  应用上下文
     * @param obj      保存的对象
     * @param fileName 文件名
     * @return 是否保存成功
     */
    public static boolean saveObject(@NonNull Context context, Object obj, String fileName) {
        if (obj == null || TextUtils.isEmpty(fileName)) {
            return false;
        }

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (oos != null)
                    oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file 文件名
     * @return 对象
     */
    public static Object readObject(Context context, String file) {
        if (!isExistDataCache(context, file))
            return null;

        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cacheFile 缓存路径
     * @return true 缓存对象存在, 反之亦然
     */
    private static boolean isExistDataCache(Context context, String cacheFile) {
        if (context == null || TextUtils.isEmpty(cacheFile)) {
            return false;
        }

        boolean exist = false;
        File data = context.getFileStreamPath(cacheFile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 删除指定路径下的所有文件
     *
     * @param dirPath 文件夹路径
     * @param options 文件夹的属性
     */
    public static void deleteDirAllFile(String dirPath, int options) {
        File file = new File(dirPath);
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                if (f.isDirectory() && (options != FILE_IGNORE_DIR)) {
                    deleteDirAllFile(f.getPath(), options);
                    f.delete();
                } else {
                    f.delete();
                }
            }
        }
    }

    /**
     * 判断SDCard是否可用
     *
     * @return true 表示SD卡可用，false 表示SD卡不可用
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 文件拷贝
     *
     * @param in   输入文件对象
     * @param path 文件路径
     * @param name 文件名
     * @return 拷贝后的文件对象
     */
    public static File copy(File in, String path, String name) {
        File newFile = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(in);
            newFile = writeFromInput(path, name, fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newFile;
    }

    /**
     * write data to sdcard from a input stream
     * 将输入流数据写进文件中
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @param is       输入流
     * @return 已保存的文件
     */
    public static File writeFromInput(String path, String fileName, InputStream is) {
        File file = createFile(path, fileName);

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read = 0;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * read data from the file
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @return 文件输出流
     */
    public static OutputStream readFromFile(String path, String fileName) {
        if (path.charAt(path.length() - 1) != '/') {
            path += '/';
        }

        File file = new File(path + fileName);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return os;
    }

    /**
     * 创建文件
     *
     * @param path     文件的路径
     * @param fileName 文件名
     * @return 文件
     */
    public static File createFile(String path, String fileName) {
        return createFileOrDir(path, fileName, false);
    }

    /**
     * create file or directory according to the isDir
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @param isDir    是否为文件夹
     * @return 文件
     */
    private static File createFileOrDir(String path, String fileName, boolean isDir) {
        if (path.charAt(path.length() - 1) != '/') {
            path += '/';
        }

        File file = new File(path + fileName);
        if (!isDir) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file.mkdir();
        }
        return file;
    }

    /**
     * 动态生成文件下载路径
     *
     * @param context 应用上下文
     * @return 文件路径
     */
    public static String getFileDownloadPath(Context context) {
        String path;
        String defaultPath = Environment.DIRECTORY_DOWNLOADS + File.separator + FileUtil.DOWNLOAD_FOLDER;
        SharedPreferences share = context.getSharedPreferences(FileUtil.DOWNLOAD_SETTING, Context.MODE_PRIVATE);
        if (share.contains(FileUtil.DOWNLOAD_PATH)) {
            path = share.getString(FileUtil.DOWNLOAD_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + FileUtil.DOWNLOAD_FOLDER);
        } else {
            path = defaultPath;
        }
        return path;
    }

    public static File getDestinationInExternalPublicDir(String dirType, String fileName) {
        File file = Environment.getExternalStoragePublicDirectory(dirType);
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IllegalStateException(file.getAbsolutePath() +
                        " already exists and is not a directory");
            }
        } else {
            if (!file.mkdirs()) {
                throw new IllegalStateException("Unable to create directory: " +
                        file.getAbsolutePath());
            }
        }

        //mDestinationUri = Uri.withAppendedPath(Uri.fromFile(file), subPath);
        /*File destFolder =  new File(file.getAbsolutePath() + File.separator + DOWNLOAD_FOLDER);
        if (destFolder.exists()) {
            if (!destFolder.isDirectory()) {
                throw new IllegalStateException(file.getAbsolutePath() +
                        " already exists and is not a directory");
            }
        } else {
            if (!destFolder.mkdirs()) {
                throw new IllegalStateException("Unable to create directory: "+
                        destFolder.getAbsolutePath());
            }
        }

        File destFile = new File(destFolder.getAbsolutePath() + File.separator + fileName);*/
        return new File(file.getAbsolutePath() + File.separator + fileName);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return "";
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
