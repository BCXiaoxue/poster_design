package com.govy.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.model.entity.Image;
import com.govy.base.service.ImageService;
import com.govy.base.mapper.ImageMapper;
import org.springframework.stereotype.Service;

/**
* @author Govy
* @description 针对表【image】的数据库操作Service实现
* @createDate 2025-07-05 13:23:47
*/
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
    implements ImageService{

}




