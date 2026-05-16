package com.govy.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.govy.base.mapper.ImageMapper;
import com.govy.base.service.ImageService;
import com.govy.common.constant.RedisConstant;
import com.govy.common.constant.TableConstant;
import com.govy.common.handler.BaseException;
import com.govy.common.result.ResultCodeEnum;
import com.govy.common.utils.RandomListUtil;
import com.govy.model.dto.AnnunciationDTO;
import com.govy.model.dto.AnnunciationQueryLikeDTO;
import com.govy.model.entity.Annunciation;
import com.govy.base.service.AnnunciationService;
import com.govy.base.mapper.AnnunciationMapper;
import com.govy.model.entity.Image;
import com.govy.model.entity.News;
import com.govy.model.vo.AnnunciationCountVO;
import com.govy.model.vo.AnnunciationQueryLikeVO;
import com.govy.model.vo.AnnunciationVO;
import com.govy.model.vo.JudgmentId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Govy
 * @description 针对表【annunciation】的数据库操作Service实现
 * @createDate 2025-07-04 10:08:12
 */
@Service
@RequiredArgsConstructor
public class AnnunciationServiceImpl extends ServiceImpl<AnnunciationMapper, Annunciation>
        implements AnnunciationService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ImageService imageService;
    private final ImageMapper imageMapper;
    private final AnnunciationMapper annunciationMapper;

    /**
     * 获取通知列表 - 分页模糊查询
     *
     * @param queryLikeDTO
     * @return
     */
    @Override
    public IPage<AnnunciationQueryLikeVO> queryLike(AnnunciationQueryLikeDTO queryLikeDTO) {
        // 分页查询
        Page<Annunciation> page = Page.of(queryLikeDTO.getPageNo(), queryLikeDTO.getPageSize());

        Page<Annunciation> annunciationPage = this.page(page, new LambdaQueryWrapper<Annunciation>()
                .select(Annunciation::getId, Annunciation::getTitle, Annunciation::getUploadTime) // 指定查询字段
                .like(queryLikeDTO.getTitle() != null, Annunciation::getTitle, queryLikeDTO.getTitle())
                .le(Annunciation::getUploadTime, new Date())
                .orderByDesc(Annunciation::getUploadTime)
        );

        // 如果没有数据，则返回一个空的分页结果
        if (annunciationPage.getRecords().isEmpty()) {
            return Page.of(queryLikeDTO.getPageNo(), queryLikeDTO.getPageSize());
        }

        // 转换为VO
        return annunciationPage.convert(annunciation -> new AnnunciationQueryLikeVO(annunciation.getId(), annunciation.getTitle(), annunciation.getUploadTime()));
    }

    @Override
    public IPage<AnnunciationQueryLikeVO> adminQueryLike(AnnunciationQueryLikeDTO queryLikeDTO) {
        // 分页查询
        Page<Annunciation> page = Page.of(queryLikeDTO.getPageNo(), queryLikeDTO.getPageSize());

        Page<Annunciation> annunciationPage = this.page(page, new LambdaQueryWrapper<Annunciation>()
                .select(Annunciation::getId, Annunciation::getTitle, Annunciation::getUploadTime) // 指定查询字段
                .like(queryLikeDTO.getTitle() != null, Annunciation::getTitle, queryLikeDTO.getTitle())
                .orderByDesc(Annunciation::getUploadTime)
        );

        // 如果没有数据，则返回一个空的分页结果
        if (annunciationPage.getRecords().isEmpty()) {
            return Page.of(queryLikeDTO.getPageNo(), queryLikeDTO.getPageSize());
        }

        // 转换为VO
        return annunciationPage.convert(annunciation -> new AnnunciationQueryLikeVO(annunciation.getId(), annunciation.getTitle(), annunciation.getUploadTime()));
    }

    /**
     * 获取所有通知数量
     * @return
     */
    @Override
    public AnnunciationCountVO annunciationCountAll() {
        // 1. 统计已发布的大赛通知数量
        Long publishedCount = this.count(new LambdaQueryWrapper<Annunciation>()
                .le(Annunciation::getUploadTime, DateUtil.date())
        );

        // 2. 统计未发布大赛通知数量
        Long unpublishedCount = this.count(new LambdaQueryWrapper<Annunciation>()
                .gt(Annunciation::getUploadTime, DateUtil.date())
        );

        return new AnnunciationCountVO(publishedCount, unpublishedCount);
    }

    /**
     * 根据id获取通知详情
     *
     * @param id
     * @return
     */
    @Override
    public AnnunciationVO getByIdWithRedis(Integer id) {
        // 1.查看redis中是否存在缓存
        String key = RedisConstant.ANNUNCIATION_PREFIX + id;
        AnnunciationVO annunciationVO = objectMapper.convertValue(redisTemplate.opsForValue().get(key), AnnunciationVO.class);

        // 2. 如何数据不为空, 直接返回数据
        if (ObjectUtil.isNotNull(annunciationVO)) {
            return annunciationVO;
        }

        // 3.从数据库中查询数据
        Annunciation annunciation = this.getById(id);

        // 4.如何数据为空, 抛出异常
        if (ObjectUtil.isNull(annunciation)) {
            throw new BaseException(ResultCodeEnum.NOT_FOUND);
        } else {
            annunciationVO = BeanUtil.copyProperties(annunciation, AnnunciationVO.class);
        }

        // 5.查询图片，将数据转为VO
        List<Image> list = imageService.list(new LambdaQueryWrapper<Image>()
                .select(Image::getUrl)
                .eq(Image::getImageId, id)
                .eq(Image::getCategory, TableConstant.ANNUNCIATION)
        );
        if (CollUtil.isEmpty(list)) {
            annunciationVO.setImages(new ArrayList<>());
        } else {
            annunciationVO.setImages(list.stream().map(Image::getUrl).collect(Collectors.toList()));
        }

        // 6.重建redis缓存
        redisTemplate.opsForValue().set(key, annunciationVO, RedisConstant.REDIS_TIME, TimeUnit.SECONDS);

        return annunciationVO;
    }

    @Override
    public void deleteById(Integer id) {
        // 1.先删除数据库
        this.removeById(id);

        // 2.再删除该数据的相关图片
        imageMapper.removeByIdAndCategory(id, TableConstant.ANNUNCIATION);

        // 3.删除redis缓存
        redisTemplate.delete(RedisConstant.ANNUNCIATION_PREFIX + id);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        // 1.先删除数据库
        this.removeByIds(ids);

        // 2.再删除该数据相关的图片
        imageMapper.removeByIdsAndCategory(ids, TableConstant.ANNUNCIATION);

        // 3.删除redis缓存
        ids.forEach(id -> redisTemplate.delete(RedisConstant.ANNUNCIATION_PREFIX + id));
    }

    @Transactional
    @Override
    public void updateByIdWithRedis(AnnunciationDTO annunciationDTO) {
        // 1.先更新数据库
        Annunciation annunciation = BeanUtil.copyProperties(annunciationDTO, Annunciation.class);
        this.updateById(annunciation);

        // 2.再删除该数据相关的图片
        imageMapper.removeByIdAndCategory(annunciation.getId(), TableConstant.ANNUNCIATION);

        // 3.新建相关图片
        List<String> urls = annunciationDTO.getImages();
        // 3.1 如果不是空，插入图片
        if (CollUtil.isNotEmpty(urls)) {
            List<Image> list = urls.stream().map(url ->
                    new Image(url, TableConstant.ANNUNCIATION, annunciation.getId())
            ).toList();
            imageService.saveBatch(list);
        }

        // 4.再删除redis缓存
        redisTemplate.delete(RedisConstant.ANNUNCIATION_PREFIX + annunciation.getId());
    }

    /**
     * 新增通知
     * @param annunciationDTO
     */
    @Transactional
    @Override
    public void saveAnnunciation(AnnunciationDTO annunciationDTO) {
        // 1.添加通知到数据库
        Annunciation annunciation = BeanUtil.copyProperties(annunciationDTO, Annunciation.class);
        annunciation.setUploadTime(new Date());
        this.save(annunciation);

        // 2.添加图片到数据库
        List<String> urls = annunciationDTO.getImages();
        if (CollUtil.isNotEmpty(urls)) {
            List<Image> list = urls.stream().map(
                    url -> new Image(url, TableConstant.ANNUNCIATION, annunciation.getId())
            ).toList();
            imageService.saveBatch(list);
        }
    }

    @Override
    public JudgmentId judgmentById(Integer id) {
        List<Annunciation> list = this.list(new LambdaQueryWrapper<Annunciation>()
                .select(Annunciation::getId)
                .orderByDesc(Annunciation::getUploadTime)
        );
        JudgmentId judgmentId = new JudgmentId(null, null);
        if (CollUtil.isEmpty(list)) {
            return judgmentId;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (id == list.get(i).getId()) {
                    if (i == 0) {
                        judgmentId.setBeforeId(null);
                        judgmentId.setAfterId(list.get(i + 1).getId());
                    } else if (i == list.size() - 1) {
                        judgmentId.setBeforeId(list.get(i - 1).getId());
                        judgmentId.setAfterId(null);
                    } else {
                        judgmentId.setBeforeId(list.get(i - 1).getId());
                        judgmentId.setAfterId(list.get(i + 1).getId());
                    }
                }
            }
        }

        return judgmentId;
    }

    /**
     * 获取指定数量的数据
     * @param number
     * @return
     */
    @Override
    public List<AnnunciationVO> getRandom(Integer number) {
        // 1.获取所有数据的id
        List<Integer> list = annunciationMapper.getAllId();
        // 2.获取指定数量的数据
        List<Integer> ids = RandomListUtil.getNumberList(list, number);
        // 3.获取这些id对应的数据
        List<AnnunciationVO> annunciationVOList = annunciationMapper.getAllByIds(ids);
       return annunciationVOList;
    }

    /**
     * 添加通知
     * @param annunciationDTO
     */
    @Override
    public void addAnnunciation(AnnunciationDTO annunciationDTO) {
        // 1.添加基本信息通知
        Annunciation annunciation = BeanUtil.copyProperties(annunciationDTO, Annunciation.class);

        // 2.保存关联图片
        List<String> urls = annunciationDTO.getImages();
        if (CollUtil.isEmpty(urls)) {
            return ;
        }

        // 3.批量保存图片
        List<Image> list = urls.stream().map(
                url -> new Image(url, TableConstant.ANNUNCIATION, annunciation.getId())
        ).toList();

        // 4.批量保存图片
        imageService.saveBatch(list);
    }

    @Override
    public void deleteByIdWithRedis(Integer id) {
        // 1.获取key
        String key = RedisConstant.ANNUNCIATION_PREFIX + id;

        // 2.删除数据库
        this.removeById(id);

        // 3.删除redis缓存
        redisTemplate.delete(key);
    }


}




