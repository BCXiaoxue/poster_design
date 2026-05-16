package com.govy.common.utils;

import io.minio.*;
import io.minio.errors.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class MinioUtil {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 创建minioClient实例
        MinioClient minioClient = MinioClient.builder()
                .endpoint(getEndpoint())
                .credentials(getAccessKey(), getSecretKey())
                .build();

        // 查询是否有这个桶
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(getBucketName())
                .build());
        if (!bucketExists) {
            // 没有这个桶，创建他
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(getBucketName())
                    .build());
            // 并配置权限
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(getBucketName())
                    .config(createBucketPolicyConfig(getBucketName()))
                    .build()
            );
        }
        // 拼接文件名
        String filename = new SimpleDateFormat("yyyyMMdd").format(new Date())
                + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        // 上传文件
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(getBucketName())  //指定桶
                .stream(file.getInputStream(), file.getSize(), -1)  // 上传文件流，文件大小，-1表示自动计算文件大小
                .object(filename)  //  指定文件名
                .contentType(file.getContentType()) // 指定文件类型
                .build());

        // 返回文件访问路径
        return String.join("/", getEndpoint(), getBucketName(), filename);
    }

    private String createBucketPolicyConfig(String bucketName) {

        return """
            {
              "Statement" : [ {
                "Action" : "s3:GetObject",
                "Effect" : "Allow",
                "Principal" : "*",
                "Resource" : "arn:aws:s3:::%s/*"
              } ],
              "Version" : "2012-10-17"
            }
            """.formatted(bucketName);
    }
}
