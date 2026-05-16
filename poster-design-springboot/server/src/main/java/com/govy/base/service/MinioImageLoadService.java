package com.govy.base.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface MinioImageLoadService {
    String upload(MultipartFile file);
}
