package com.govy.base.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
public class ImageDownloadService {

    /**
     * 从URL下载图片并返回字节数组
     */
    public byte[] downloadImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // 配置连接
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000); // 5秒连接超时
        connection.setReadTimeout(10000);   // 10秒读取超时
        connection.setInstanceFollowRedirects(true); // 自动处理重定向
        
        // 检查响应状态
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP错误状态码: " + status);
        }
        
        // 读取图片数据
        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * 从URL中提取文件名
     */
    public String extractFileNameFromUrl(String imageUrl) {
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