package com.govy.base.service.impl;

import com.govy.base.controller.common.MinioImageLoadController;
import com.govy.base.service.MinioImageLoadService;
import com.govy.common.utils.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioImageLoadServiceImpl implements MinioImageLoadService {
    private final MinioUtil minioUtil;
    // 缩略图尺寸配置（可根据需求调整）
    private static final int THUMB_WIDTH = 2560;
    private static final int THUMB_HEIGHT = 2560;


    @SneakyThrows
    @Override
    public String upload(MultipartFile file) {
        String url = null;
        // 2. 生成并上传缩略图
        try {
            // 从MultipartFile获取输入流
            try (InputStream inputStream = file.getInputStream()) {
                // 创建缩略图字节流
                byte[] thumbBytes = createThumbnail(inputStream);

                // 构建缩略图文件名（添加 thumb_ 前缀）
                String originalFilename = file.getOriginalFilename();
                String thumbFilename = "thumb_" + (originalFilename != null ? originalFilename : "thumbnail");

                // 创建缩略图MultipartFile
                MultipartFile thumbFile = new MinioImageLoadController.InMemoryMultipartFile(
                        thumbFilename,
                        file.getContentType(),
                        thumbBytes
                );

                // 上传缩略图
                url = minioUtil.upload(thumbFile);
                log.info("缩略图上传成功: {}", url);
            }
        } catch (Exception e) {
            // 3.上传原图
            url = minioUtil.upload(file);
            log.error("缩略图生成失败，原图仍可用: {}", url, e);
        }
        return url;
    }


    /**
     * 创建缩略图（内存操作，不落盘）
     */
    private byte[] createThumbnail(InputStream imageStream) throws IOException {
        // 读取原始图片
        BufferedImage srcImage = ImageIO.read(imageStream);
        if (srcImage == null) {
            throw new IOException("无法读取图片数据");
        }

        // 计算等比例缩放尺寸
        int originalWidth = srcImage.getWidth();
        int originalHeight = srcImage.getHeight();
        double ratio = Math.min((double) THUMB_WIDTH / originalWidth, (double) THUMB_HEIGHT / originalHeight);
        int scaledWidth = (int) (originalWidth * ratio);
        int scaledHeight = (int) (originalHeight * ratio);

        // 创建缩放后的图像
        Image scaledImage = srcImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        BufferedImage thumbImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);

        // 绘制图像
        Graphics2D g = thumbImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();

        // 转换为字节数组
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(thumbImage, "jpg", baos);
            return baos.toByteArray();
        }
    }

    /**
     * 内存中的MultipartFile实现（用于缩略图上传）
     */
    private static class InMemoryMultipartFile implements MultipartFile {
        private final String name;
        private final String contentType;
        private final byte[] content;

        public InMemoryMultipartFile(String name, String contentType, byte[] content) {
            this.name = name;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) {
            throw new UnsupportedOperationException("内存文件不支持直接传输");
        }
    }
}
