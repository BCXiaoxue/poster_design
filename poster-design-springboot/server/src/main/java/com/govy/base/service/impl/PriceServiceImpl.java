package com.govy.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.model.entity.Price;
import com.govy.base.service.PriceService;
import com.govy.base.mapper.PriceMapper;
import org.springframework.stereotype.Service;

/**
* @author Govy
* @description 针对表【price】的数据库操作Service实现
* @createDate 2025-07-04 10:12:55
*/
@Service
public class PriceServiceImpl extends ServiceImpl<PriceMapper, Price>
    implements PriceService{

}




