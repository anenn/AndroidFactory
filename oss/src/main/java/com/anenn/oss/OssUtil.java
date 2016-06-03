package com.anenn.oss;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传、下载操作工具类
 * Created by Anenn on 2016/1/3.
 */
public class OssUtil {

    // 阿里云存储服务对象
    private OSS oss;
    // 文件上传回调接口对象
    private IFileUploadCallback iFileUploadCallback;
    // 文件上传任务对象
    private OSSAsyncTask ossAsyncTask;
    // 消息处理对象
    private Handler handler;
    // 应用上下文
    private Context mContext;
    // 是否持续更新上传状态
    private boolean isShowUploadState;

    public void setShowUploadState(boolean showUploadState) {
        isShowUploadState = showUploadState;
    }

    public OssUtil(Activity activity, Handler handler) {
        iFileUploadCallback = (IFileUploadCallback) activity;
        this.handler = handler;
        mContext = activity;
    }

    public OssUtil(Fragment fragment, Handler handler) {
        iFileUploadCallback = (IFileUploadCallback) fragment;
        this.handler = handler;
        mContext = fragment.getActivity();
    }

    /**
     * 初始化云存储服务
     */
    public void initOSS() {
        // 网络参数配置
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket 超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        // 凭证
        OSSCredentialProvider ossCredentialProvider =
                new OSSPlainTextAKSKCredentialProvider(OssKV.accessKeyId, OssKV.accessKeySecret);

        oss = new OSSClient(mContext, OssKV.endPoint, ossCredentialProvider, conf);
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     */
    public void uploadFile(@NonNull final String filePath) {
        if (oss == null) {
            Toast.makeText(mContext, "文件上传服务出现异常, 请稍后重试", Toast.LENGTH_LONG).show();
            return;
        }
        // MD5校验设置
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        try {
            // 设置Md5以便校验
            metadata.setContentMD5(BinaryUtil.calculateBase64Md5(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        UUID uuid = UUID.randomUUID();
        String[] pathSegment = filePath.split("/");
        final String objectKey = uuid.toString().replaceAll("-", "") + File.separator + pathSegment[pathSegment.length - 1];

        PutObjectRequest putObjectRequest = new PutObjectRequest(OssKV.bucket, objectKey, filePath);
        putObjectRequest.setMetadata(metadata);
        putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, final long currentSize, final long totalSize) {
                if (iFileUploadCallback != null && isShowUploadState) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            iFileUploadCallback.fileUploadProgress(currentSize, totalSize);
                        }
                    });
                }
            }
        });

        ossAsyncTask = oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(final PutObjectRequest putObjectRequest, final PutObjectResult putObjectResult) {
                if (putObjectResult != null) {
                    if (iFileUploadCallback != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iFileUploadCallback.fileUploadSuccess(putObjectResult.getETag(), putObjectRequest.getObjectKey());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(final PutObjectRequest request, ClientException clientException, final ServiceException serviceException) {
                // 本地异常如网络异常
                if (clientException != null) {
                    clientException.printStackTrace();
                }
                // 服务异常
                if (serviceException != null) {
                    if (iFileUploadCallback != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iFileUploadCallback.fileUploadFailure(serviceException.getErrorCode(),
                                        request.getObjectKey(),
                                        serviceException.getHostId(),
                                        serviceException.getRawMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取对象存储实体
     *
     * @return OSS
     */
    public OSS getOss() {
        return oss;
    }

    /**
     * 结束任务
     */
    public void cancelTask() {
        if (ossAsyncTask != null) {
            ossAsyncTask.cancel();
        }
    }

    /**
     * 文件上传回调接口
     */
    public interface IFileUploadCallback {
        void fileUploadProgress(long currentSize, long totalSize);

        void fileUploadSuccess(String eTag, String objectKey);

        void fileUploadFailure(String errorCode, String objectKey, String hostId, String rawMessage);
    }
}
