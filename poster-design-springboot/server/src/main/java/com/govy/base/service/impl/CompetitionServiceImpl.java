package com.govy.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.govy.base.mapper.CompetitionCategoryMapper;
import com.govy.base.mapper.ImageMapper;
import com.govy.base.mapper.WorkMapper;
import com.govy.base.service.CompetitionCategoryService;
import com.govy.base.service.ImageService;
import com.govy.common.constant.RedisConstant;
import com.govy.common.constant.TableConstant;
import com.govy.common.handler.BaseException;
import com.govy.common.login.LoginUserHolder;
import com.govy.common.result.ResultCodeEnum;
import com.govy.model.dto.*;
import com.govy.model.entity.Competition;
import com.govy.base.service.CompetitionService;
import com.govy.base.mapper.CompetitionMapper;
import com.govy.model.entity.CompetitionCategory;
import com.govy.model.entity.Image;
import com.govy.model.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author Govy
* @description 针对表【competition】的数据库操作Service实现
* @createDate 2025-07-04 09:47:17
*/
@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl extends ServiceImpl<CompetitionMapper, Competition>
    implements CompetitionService{

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final ImageMapper imageMapper;
    private final ImageService imageService;
    private final CompetitionCategoryMapper competitionCategoryMapper;
    private final CompetitionCategoryService competitionCategoryService;
    private final CompetitionMapper competitionMapper;
    private final WorkMapper workMapper;

    /**
     * 根据id删除竞赛信息
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        // 1.先删除数据库
        this.removeById(id);

        // 2.删除该数据相关的图片
        imageMapper.removeByIdAndCategory(id, TableConstant.COMPETITION);

        // 3.再删除redis缓存
        redisTemplate.delete(RedisConstant.COMPETITION_PREFIX + id);
    }

    /**
     * 根据id列表批量删除竞赛信息
     * @param ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        // 1.先删除数据库
        this.removeByIds(ids);

        // 2.删除该数据相关的图片
        imageMapper.removeByIdsAndCategory(ids, TableConstant.COMPETITION);

        // 3.再删除redis缓存
        ids.forEach(id -> redisTemplate.delete(RedisConstant.COMPETITION_PREFIX + id));
    }

    /**
     * 根据id修改数据
     * @param competitionDTO
     */
    @Override
    @Transactional
    public void updateByIdWithRedis(CompetitionDTO competitionDTO) {
        // 1.先更新数据库基本信息
        Competition competition = BeanUtil.copyProperties(competitionDTO, Competition.class);
        this.updateById(competition);

        // 2.再删除该数据的相关图片信息
        imageMapper.removeByIdAndCategory(competition.getId(), TableConstant.COMPETITION);

        // 3.新增相关图片信息
        List<String> urls = competitionDTO.getImages();
        if (CollUtil.isNotEmpty(urls)) {
            List<Image> list = urls.stream().map(url ->
                    new Image(url, TableConstant.COMPETITION, competition.getId())
            ).toList();
            imageService.saveBatch(list);
        }

        // 4.再删除redis缓存
        redisTemplate.delete(RedisConstant.COMPETITION_PREFIX + competition.getId());
    }

    /**
     * 根据id获取竞赛信息（手动序列化）
     * @param id
     * @return
     */
    @Override
    @SneakyThrows
    public CompetitionVO getByIdWithRedis(Integer id) {
        // 1.先从redis中查询是否有缓存
        String key = RedisConstant.COMPETITION_PREFIX + id;
        // 2.如果有缓存，则直接返回
        String json = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasText(json)) {
            return objectMapper.readValue(json, CompetitionVO.class); // 读取JSON字符串
        }

        // 3.如果没有缓存，则从数据库中查询
        Competition competition = this.getById(id);

        CompetitionVO competitionVO = new CompetitionVO();
        // 4.如果数据库中也没有这条数据
        if (ObjectUtil.isNull(competition)) {
            throw new BaseException(ResultCodeEnum.NOT_FOUND);
        } else {
            competitionVO = objectMapper.convertValue(competition, CompetitionVO.class);
            // 4.1获取categoryId对应的categoryName
            CompetitionCategory competitionCategory = competitionCategoryService.getById(competition.getCategoryId());
            competitionVO.setCategoryName(competitionCategory.getName());
            competitionVO.setCategoryId(competitionCategory.getId());
        }

        // 5.获取该数据的图片信息
        List<Image> list = imageService.list(
                new LambdaQueryWrapper<Image>()
                        .select(Image::getUrl)
                        .eq(Image::getImageId, id)
                        .eq(Image::getCategory, TableConstant.COMPETITION)
                        .orderByAsc(Image::getId)
        );

        // 5.1 如果没有图片信息，则返回空列表
        if (CollUtil.isEmpty(list)) {
            competitionVO.setImages(new ArrayList<>());
        } else {
            competitionVO.setImages(list.stream().map(Image::getUrl).collect(Collectors.toList()));
        }

        // 6.获取该竞赛的作品图片信息
        List<WorkUrlVO> workList = workMapper.selectWorkImagesByCompetitionId(id);
        // 6.1 如果没有作品图片信息，则返回空列表
        if (CollUtil.isEmpty(workList)) {
            competitionVO.setWorkImages(new ArrayList<>());
        } else {
            competitionVO.setWorkImages(workList);
        }

        // 7.重建redis缓存
        String strJson = objectMapper.writeValueAsString(competitionVO);
        redisTemplate.opsForValue().set(key, strJson, RedisConstant.REDIS_TIME, TimeUnit.SECONDS);

        return competitionVO;
    }

    /**
     * 保存竞赛信息
     * @param competitionDTO
     */
    @Override
    public void saveCompetition(CompetitionDTO competitionDTO) {
        // 1. 保存竞赛基本信息
        Competition competition = BeanUtil.copyProperties(competitionDTO, Competition.class);
        this.save(competition);

        // 2. 保存竞赛图片信息
        List<String> urls = competitionDTO.getImages();
        if (CollUtil.isNotEmpty(urls)) {
            List<Image> list = urls.stream().map(
                    url -> new Image(url, TableConstant.COMPETITION, competition.getId())
            ).toList();
            imageService.saveBatch(list);
        }
    }

    /**
     * 根据名称和状态模糊查询
     *
     * @param competitionMyQueryLikeDTO
     * @return
     */
    @Override
    public Page<CompetitionMyVO> getByIdWithLike(CompetitionMyQueryLikeDTO competitionMyQueryLikeDTO) {
        // 0.获取分页参数
        Page<Competition> page = Page.of(competitionMyQueryLikeDTO.getPageNo(), competitionMyQueryLikeDTO.getPageSize());

        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        // 2.获取竞赛状态
        Integer competitionStatus = competitionMyQueryLikeDTO.getCompetitionStatus();

        Page<CompetitionMyVO> pageList = new Page<>();
        // 3.如果status为空，则查询所有
        if (ObjectUtil.isNull(competitionStatus)) {
            throw new BaseException(ResultCodeEnum.PARAM_ERROR);
        }
        // 4.未开始-0 招募中-1，进行中-2，已结束-3
        if (competitionStatus == 0) {
            pageList = competitionMapper.selectPageB(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
        } else if (competitionStatus == 1) {
            pageList = competitionMapper.selectPageC(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
        } else if (competitionStatus == 2) {
            pageList = competitionMapper.selectPageD(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
        } else if (competitionStatus == 3) {
            pageList = competitionMapper.selectPageE(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
        } else if (competitionStatus == 4) {
            // 查询所有比赛（已在数据库层面按状态排序）
            pageList = competitionMapper.selectPageA(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
            // 无需在Java代码中再次排序，数据库已经按照状态排序
            return pageList;
        }
        // 5.封装competition_status
        for (CompetitionMyVO competitionMyVO : pageList.getRecords()) {
            competitionMyVO.setCompetitionStatus(competitionStatus);
        }

        return pageList;
    }

    @Override
    public List<CompetitionAdminLikeVO> getWithLike(CompetitionAdminLike competitionAdminLike) {
        // 2.获取竞赛状态
        Integer competitionStatus = competitionAdminLike.getCompetitionStatus();

        List<CompetitionAdminLikeVO> list = new ArrayList<>();
        // 3.如果status为空，则查询所有
        if (ObjectUtil.isNull(competitionStatus)) {
            throw new BaseException(ResultCodeEnum.PARAM_ERROR);
        }

        // 4.未开始-0 招募中-1，进行中-2，已结束-3
        if (competitionStatus == 0) {
            list = competitionMapper.selectPageAdminB(competitionAdminLike.getCompetitionName(), competitionAdminLike.getCategoryId());
        } else if (competitionStatus == 1) {
            list = competitionMapper.selectPageAdminC(competitionAdminLike.getCompetitionName(), competitionAdminLike.getCategoryId());
        } else if (competitionStatus == 2) {
            list = competitionMapper.selectPageAdminD(competitionAdminLike.getCompetitionName(), competitionAdminLike.getCategoryId());
        } else if (competitionStatus == 3) {
            list = competitionMapper.selectPageAdminE(competitionAdminLike.getCompetitionName(), competitionAdminLike.getCategoryId());
        }

        // 5.封装competitionStatus
        for (CompetitionAdminLikeVO competitionAdminLikeVO : list) {
            competitionAdminLikeVO.setCompetitionStatus(competitionStatus);
            competitionAdminLikeVO.getUserAvatars().add("http://122.51.51.36:9000/poster-design/20250704/0f23a57b-5777-4021-ab38-2f8bcf779219-thumb_默认头像.png");
            competitionAdminLikeVO.getUserAvatars().add("http://122.51.51.36:9000/poster-design/20250704/0f23a57b-5777-4021-ab38-2f8bcf779219-thumb_默认头像.png");
        }

        return list;

    }

    /**
     * 获取竞赛所有详情
     * @param id
     * @return
     */
    @Override
    public CompetitionAdminByIdVO getByIdWithAll(Integer id) {
        return competitionMapper.getByIdWithAll(id);
    }

    @Override
    public Page<CompetitionMyVO> getByNameWithStatus(CompetitionMyQueryLikeDTO competitionMyQueryLikeDTO) {
        // 0.获取分页参数
        Page<Competition> page = Page.of(competitionMyQueryLikeDTO.getPageNo(), competitionMyQueryLikeDTO.getPageSize());

        // 1.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        // 2.获取竞赛状态
        Integer competitionStatus = competitionMyQueryLikeDTO.getCompetitionStatus();

        Page<CompetitionMyVO> pageList = new Page<>();
        // 3.如果status为空，则查询所有
        if (ObjectUtil.isNull(competitionStatus)) {
            throw new BaseException(ResultCodeEnum.PARAM_ERROR);
        }
        // 4.未开始-0 招募中-1，进行中-2，已结束-3
        if (competitionStatus == 0) {
            pageList = competitionMapper.selectPageBB(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
        } else if (competitionStatus == 1) {
            pageList = competitionMapper.selectPageCC(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
        } else if (competitionStatus == 2) {
            pageList = competitionMapper.selectPageDD(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
        } else if (competitionStatus == 3) {
            pageList = competitionMapper.selectPageEE(page, competitionMyQueryLikeDTO.getCompetitionName(), userId);
        }

        // 5.封装competition_status
        for (CompetitionMyVO competitionMyVO : pageList.getRecords()) {
            competitionMyVO.setCompetitionStatus(competitionStatus);
        }

        return pageList;
    }

    /**
     * 获取竞赛状态数量
     * @return
     */
    @Override
    public List<CompetitionDataVO> getCompetitionStatusCount() {
        // 1.获取未开始的数量
        Long countA = competitionMapper.selectCount(new LambdaQueryWrapper<Competition>()
                .gt(Competition::getRegistrationBegin, DateUtil.date())
        );

        // 2.获取招募中的数量
        Long countB = competitionMapper.selectCount(new LambdaQueryWrapper<Competition>()
                .le(Competition::getRegistrationBegin, DateUtil.date())
                .ge(Competition::getRegistrationEnd, DateUtil.date())
        );

        // 3.获取进行中的数量
        Long countC = competitionMapper.selectCount(new LambdaQueryWrapper<Competition>()
                .lt(Competition::getRegistrationEnd, DateUtil.date())
                .ge(Competition::getActivityEnd, DateUtil.date())
        );

        // 4.获取已结束的数量
        Long countD = competitionMapper.selectCount(new LambdaQueryWrapper<Competition>()
                .lt(Competition::getActivityEnd, DateUtil.date())
        );

        return List.of(
                new CompetitionDataVO("未开始", countA, "#909399"),
                new CompetitionDataVO("招募中", countB, "#409eff"),
                new CompetitionDataVO("评审中", countC, "#e6a23c"),
                new CompetitionDataVO("已结束", countD, "#67c23a")
        );
    }

    /**
     * 获取竞赛分类数量
     * @return
     */
    @Override
    public List<CompetitionCategoryDataVO> getCompetitionCategoryCount() {
        List<CompetitionCategoryDataVO> list = competitionCategoryMapper.getCompetitionCategoryCount();

        list.get(0).setColor("#67c23a");
        list.get(1).setColor("#409eff");
        list.get(2).setColor("#e6a23c");
        list.get(3).setColor("#f56c6c");
        list.get(4).setColor("#909399");
        return list;
    }

    // 工具方法：根据时间计算比赛状态
    private int calcCompetitionStatus(java.util.Date registrationBegin, java.util.Date registrationEnd, java.util.Date activityEnd) {
        java.util.Date now = DateUtil.date();
        if (now.before(registrationBegin)) {
            return 0; // 未开始
        } else if (!now.before(registrationBegin) && !now.after(registrationEnd)) {
            return 1; // 招募中
        } else if (now.after(registrationEnd) && !now.after(activityEnd)) {
            return 2; // 进行中
        } else {
            return 3; // 已结束
        }
    }
}




