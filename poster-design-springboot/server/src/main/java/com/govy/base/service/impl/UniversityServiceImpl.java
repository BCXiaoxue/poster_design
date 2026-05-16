package com.govy.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.base.mapper.UniversityMapper;
import com.govy.base.service.UniversityService;
import com.govy.model.entity.University;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl extends ServiceImpl<UniversityMapper, University>
        implements UniversityService {
}
