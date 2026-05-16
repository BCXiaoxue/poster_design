package com.govy.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.model.entity.Score;
import com.govy.base.service.ScoreService;
import com.govy.base.mapper.ScoreMapper;
import org.springframework.stereotype.Service;

/**
* @author Govy
* @description 针对表【score】的数据库操作Service实现
* @createDate 2025-07-04 10:08:22
*/
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score>
    implements ScoreService{

}




