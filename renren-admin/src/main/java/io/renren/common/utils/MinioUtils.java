package io.renren.common.utils;

import io.minio.Result;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     *
     * @param bucketName 存储bucket
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 文件上传
     *
     * @param file       文件
     * @param bucketName 存储bucket
     * @return Boolean
     */
    public Boolean upload(MultipartFile file, String fileName, String bucketName) {
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
            log.info("文件上传成功{}", fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 文件下载
     *
     * @param bucketName 存储bucket名称
     * @param fileName   文件名称
     * @param res        response
     * @return Boolean
     */
    public void download(String bucketName, String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName)
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                //设置强制下载不打开
                res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    ///**
    // * 查看文件对象
    // *
    // * @param bucketName 存储bucket名称
    // * @return 存储bucket内文件对象信息
    // */
    //public List<ObjectItem> listObjects(String bucketName) {
    //    Iterable<Result<Item>> results = minioClient.listObjects(
    //            ListObjectsArgs.builder().bucket(bucketName).build());
    //    List<ObjectItem> objectItems = new ArrayList<>();
    //    try {
    //        for (Result<Item> result : results) {
    //            Item item = result.get();
    //            ObjectItem objectItem = new ObjectItem();
    //            objectItem.setObjectName(item.objectName());
    //            objectItem.setSize(item.size());
    //            objectItems.add(objectItem);
    //        }
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        return null;
    //    }
    //    return objectItems;
    //}

    /**
     * 批量删除文件对象
     *
     * @param bucketName 存储bucket名称
     * @param objects    对象名称集合
     */
    public Iterable<io.minio.Result<DeleteError>> removeObjects(String bucketName, List<String> objects) {
        List<DeleteObject> dos = objects.stream().map(e -> new DeleteObject(e)).collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos).build());
        return results;
    }

    public void removeObject(String bucketName, String objectName) {
        try {
            // 从mybucket中删除myobject。
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
        }
    }
}
