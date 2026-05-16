package com.govy.common.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Data
@Slf4j
public class ImageDownloadUtil {

    /**
     * 从URL下载图片并返回字节数组
     */
    public static byte[] downloadImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 配置连接
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Connection", "close"); // 防止重定向问题

        // 检查响应状态
        int status = connection.getResponseCode();
        if (status < 200 || status >= 300) {
            connection.disconnect();
            throw new IOException("HTTP错误状态码: " + status);
        }

        // 验证内容类型
        String contentType = connection.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            connection.disconnect();
            throw new IOException("无效的内容类型: " + contentType);
        }

        // 带大小限制的流读取
        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            int maxSize = 10 * 1024 * 1024; // 10MB限制
            byte[] buffer = new byte[4096];
            int bytesRead;
            int totalBytes = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
                if (totalBytes > maxSize) {
                    throw new IOException("图片大小超过10MB限制");
                }
            }
            return outputStream.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    /**
     * 从URL中提取文件名
     */
    public static String extractFileNameFromUrl(String imageUrl) {
        // 尝试从URL路径获取文件名
        String fileName = FilenameUtils.getName(imageUrl);

        // 移除查询参数
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }

        // 如果没有有效文件名或扩展名，生成随机文件名
        if (!StringUtils.hasText(fileName) || !fileName.contains(".")) {
            return "image_" + UUID.randomUUID() + ".jpg";
        }

        return fileName;
    }
}