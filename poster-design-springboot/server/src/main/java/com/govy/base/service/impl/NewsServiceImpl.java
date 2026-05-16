package com.govy.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.govy.model.dto.NewsDTO;
import com.govy.model.dto.NewsQueryLikeDTO;
import com.govy.model.entity.Image;
import com.govy.model.entity.News;
import com.govy.base.service.NewsService;
import com.govy.base.mapper.NewsMapper;
import com.govy.model.vo.JudgmentId;
import com.govy.model.vo.NewsCountVO;
import com.govy.model.vo.NewsQueryLikeVO;
import com.govy.model.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Govy
 * @description 针对表【news】的数据库操作Service实现
 * @createDate 2025-07-04 10:05:11
 */
@Service
@RequiredArgsConstructor
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
        implements NewsService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final NewsMapper newsMapper;
    private final ImageMapper imageMapper;
    private final ImageService imageService;

    /**
     * 分页查询
     * @param queryDTO
     * @return
     */
    @Override
    public IPage<NewsQueryLikeVO> queryList(NewsQueryLikeDTO queryDTO) {
        // 分页查询
        Page<News> page = Page.of(queryDTO.getPageNo(), queryDTO.getPageSize());

        Page<News> newsPage = this.page(page, new LambdaQueryWrapper<News>()
                .select(News::getId, News::getTitle, News::getUploadTime)
                .like(queryDTO.getTitle() != null, News::getTitle, queryDTO.getTitle())
                .le(News::getUploadTime, new Date())
                .orderByDesc(News::getUploadTime)
        );

        // 如果没有数据，则返回一个空的分页结果
        if (newsPage.getRecords().isEmpty()) {
            return Page.of(queryDTO.getPageNo(), queryDTO.getPageSize());
        }

        // 转换为VO
        return newsPage.convert(news -> new NewsQueryLikeVO(news.getId(), news.getTitle(), news.getUploadTime()));
    }

    @Override
    public IPage<NewsQueryLikeVO> adminQueryList(NewsQueryLikeDTO queryDTO) {
        // 分页查询
        Page<News> page = Page.of(queryDTO.getPageNo(), queryDTO.getPageSize());

        Page<News> newsPage = this.page(page, new LambdaQueryWrapper<News>()
                .select(News::getId, News::getTitle, News::getUploadTime)
                .like(queryDTO.getTitle() != null, News::getTitle, queryDTO.getTitle())
                .orderByDesc(News::getUploadTime)
        );

        // 如果没有数据，则返回一个空的分页结果
        if (newsPage.getRecords().isEmpty()) {
            return Page.of(queryDTO.getPageNo(), queryDTO.getPageSize());
        }

        // 转换为VO
        return newsPage.convert(news -> new NewsQueryLikeVO(news.getId(), news.getTitle(), news.getUploadTime()));
    }

    /**
     * 统计新闻发布情况
     * @return
     */
    @Override
    public NewsCountVO newsCountAll() {
        // 1. 查询已发布新闻和未发布新闻的个数
        Long publishedCount = this.count(new LambdaQueryWrapper<News>()
                .le(News::getUploadTime, DateUtil.date())
        );
        Long unpublishedCount = this.count(new LambdaQueryWrapper<News>()
                .gt(News::getUploadTime, DateUtil.date())
        );
        return new NewsCountVO(publishedCount, unpublishedCount);
    }

    /**
     * 根据id获取新闻详情
     *
     * @param id
     * @return
     */
    @Override
    public NewsVO getByIdWithRedis(Integer id) {
        // 1.先从redis中查询是否有缓存
        String key = RedisConstant.NEWS_PREFIX + id;
        NewsVO newsVO = objectMapper.convertValue(redisTemplate.opsForValue().get(key), NewsVO.class);

        // 2.如果有缓存，则直接返回
        if (ObjectUtil.isNotNull(newsVO)) {
            return newsVO;
        }

        // 3.如果没有缓存，则从数据库中查询
        News news = this.getById(id);

        // 4.如果数据库中也没有这条数据
        if (ObjectUtil.isNull(news)) {
            throw new BaseException(ResultCodeEnum.NOT_FOUND);
        } else {
            newsVO = BeanUtil.copyProperties(news, NewsVO.class);
        }

        // 5.查询Images, 并创建VO对象
        List<Image> list = imageService.list(new LambdaQueryWrapper<Image>()
                .select(Image::getUrl)
                .eq(Image::getImageId, id)
                .eq(Image::getCategory, TableConstant.NEWS)
        );
        if (CollUtil.isEmpty(list)) {
            newsVO.setImages(new ArrayList<>());
        } else {
            newsVO.setImages(list.stream().map(Image::getUrl).collect(Collectors.toList()));
        }

        // 6.重建redis缓存
        redisTemplate.opsForValue().set(key, newsVO, RedisConstant.REDIS_TIME, TimeUnit.SECONDS);

        return newsVO;
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        // 1.先删除数据库
        this.removeById(id);

        // 2.删除该数据相关的图片
        imageMapper.removeByIdAndCategory(id, TableConstant.NEWS);

        // 3.再删除redis缓存
        redisTemplate.delete(RedisConstant.NEWS_PREFIX + id);
    }

    /**
     * 根据id批量删除
     * @param ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        // 1.先删除数据库
        this.removeByIds(ids);

        // 2.删除该数据相关的图片
        imageMapper.removeByIdsAndCategory(ids, TableConstant.NEWS);

        // 3.再删除redis缓存
        ids.forEach(id -> redisTemplate.delete(RedisConstant.NEWS_PREFIX + id));
    }

    /**
     * 根据id修改新闻数据
     * @param newsDTO
     */
    @Override
    public void updateByIdWithRedis(NewsDTO newsDTO) {
        // 1. 先更新数据库
        News news = BeanUtil.copyProperties(newsDTO, News.class);
        this.updateById(news);

        // 2. 删除该数据的图片
        imageMapper.removeByIdAndCategory(news.getId(), TableConstant.NEWS);

        // 3. 新增该数据图片
        List<String> images = newsDTO.getImages();
        if (CollUtil.isNotEmpty(images)) {
            List<Image> list = images.stream().map(url ->
                    new Image(url, TableConstant.NEWS, news.getId())
            ).toList();
            imageService.saveBatch(list);
        }

        // 2. 再删除redis缓存
        redisTemplate.delete(RedisConstant.NEWS_PREFIX + news.getId());
    }

    /**
     * 保存新闻
     * @param newsVO
     */
    @Override
    public void saveNews(NewsVO newsVO) {
        // 1. 保存新闻
        News news = BeanUtil.copyProperties(newsVO, News.class);
        news.setUploadTime(new Date());
        this.save(news);

        // 2. 插入该数据的图片
        List<String> images = newsVO.getImages();
        if (CollUtil.isNotEmpty(images)) {
            List<Image> list = images.stream().map(url ->
                    new Image(url, TableConstant.NEWS, news.getId())
            ).toList();
            imageService.saveBatch(list);
        }
    }

    /**
     * 根据id查询, 是否有前后id
     * @param id
     * @return
     */
    @Override
    public JudgmentId judgmentById(Integer id) {
        List<News> list = this.list(new LambdaQueryWrapper<News>()
                .select(News::getId)
                .orderByDesc(News::getUploadTime)
        );
        JudgmentId judgmentId = new JudgmentId(null, null);
        if (CollUtil.isEmpty(list)) {
            return judgmentId;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (id == list.get(i).getId()) {
                    if (i == 0 && i + 1 < list.size()) {
                        judgmentId.setBeforeId(null);
                        judgmentId.setAfterId(list.get(i + 1).getId());
                    } else if (i == list.size() - 1 && i - 1 >= 0) {
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
     * 获取指定数量的新闻
     * @param number
     * @return
     */
    @Override
    public List<NewsVO> getRandom(Integer number) {
        // 1. 获取所有数据的id
        List<Integer> list = newsMapper.getAllId();
        // 2. 获取指定数量的数据
        List<Integer> ids = RandomListUtil.getNumberList(list, number);
        // 3. 获取这些id对应的数据
        List<NewsVO> newsList = newsMapper.getAllByIds(ids);
        return newsList;
    }


    /**
     * 添加新闻
     * @param newsDTO
     */
    @Override
    public void addNews(NewsDTO newsDTO) {
        // 1.添加新闻基本信息
        News news = BeanUtil.copyProperties(newsDTO, News.class);
        this.save(news);

        // 2.添加新闻图片
        List<String> images = newsDTO.getImages();

        // 3.判断图片是否为空
        if (CollUtil.isEmpty(images)) {
            return ;
        }

        // 4.添加图片
        List<Image> list = images.stream().map(url -> new Image(url, TableConstant.NEWS, news.getId())).toList();
        imageService.saveBatch(list);
    }

    @Override
    public void deleteByIdWithRedis(Integer id) {
        // 1.获取key
        String key = RedisConstant.NEWS_PREFIX + id;
        // 2. 先删除数据库
        this.removeById(id);
        // 3. 再删缓存
        redisTemplate.delete(key);
    }

}




