package com.govy.base.controller.common;

import com.govy.base.service.impl.ImageDownloadService;
import com.govy.common.utils.ImageDownloadUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "下载图片", description = "通用接口")
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Slf4j
public class ImageDownloadController {

    @GetMapping(value = "/download-image", produces = "application/octet-stream")
    public ResponseEntity<byte[]> downloadImage(@RequestParam("url") String imageUrl) {
        try {
            // 下载图片并获取字节数组
            byte[] imageData = ImageDownloadUtil.downloadImageFromUrl(imageUrl);

            // 从URL提取文件名
            String fileName = ImageDownloadUtil.extractFileNameFromUrl(imageUrl);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 根据实际类型调整
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(imageData.length);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("下载失败: " + e.getMessage()).getBytes());
        }
    }
}
