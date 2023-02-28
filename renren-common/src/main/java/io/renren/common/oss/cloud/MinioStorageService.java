package io.renren.common.oss.cloud;

import io.minio.*;
import io.minio.errors.MinioException;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Minio存储
 *
 * @author Yangx 321740709@qq.com
 * @DATE 2022/11/18 13:06
 **/
public class MinioStorageService extends AbstractCloudStorageService {

    public MinioStorageService(CloudStorageConfig config) {
        this.config = config;
    }

    public MinioClient getMinioClient() {
        return  MinioClient.builder()
                .endpoint(config.getMinioEndpoint())
                .credentials(config.getMinioAccessKey(), config.getMinioSecretKey())
                .build();
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {

        try {
            boolean isExist = getMinioClient().bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(config.getMinioBucketName())
                            .build());
            if (!isExist) {
                getMinioClient().makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(config.getMinioBucketName())
                                .build());
            }
            getMinioClient().putObject(
                    PutObjectArgs.builder()
                            .bucket(config.getMinioBucketName())
                            .stream(inputStream,inputStream.available(), -1)
                            .object(path)
                            .contentType("image/jpeg")
                            .build()
            );
        } catch (Exception e) {
            throw new RenException(ErrorCode.OSS_UPLOAD_FILE_ERROR, e, "");
        }

        return config.getMinioEndpoint() + "/" + config.getMinioBucketName()  + "/" + path;
    }


    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getMinioPrefix(), suffix));
    }


    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getMinioPrefix(), suffix));
    }

    @Override
    public void removeObject(String objectName){
        System.out.println(objectName);
        System.out.println(config);
        //MinioClient client = MinioClient.builder()
        //        .endpoint(config.getMinioEndpoint())
        //        .credentials(config.getMinioAccessKey(), config.getMinioSecretKey())
        //        .build();
        try {
            getMinioClient().removeObject(RemoveObjectArgs.builder().bucket(config.getMinioBucketName()).object(objectName).build());

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RenException(ErrorCode.OSS_UPLOAD_FILE_ERROR, e, "");
        }
    }
}
