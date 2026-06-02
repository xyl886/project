package com.love.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.BizException;
import com.love.product.entity.Tags;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.TagPageReq;
import com.love.product.entity.vo.TagVO;
import com.love.product.mapper.TagsMapper;
import com.love.product.service.TagsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName: com.love.product.service
 * @Description: TagsServiceImpl
 * @Author: Administrator
 * @Date: 2023/5/23 19:23
 */
@Slf4j
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {
    @Resource
    private TagsMapper tagsMapper;

    @Override
    public String getTagNameById(Long tagId) {
        return tagsMapper.getTagNameById(tagId);
    }

    /**
     * 标签列表
     *
     * @param tagPageReq
     * @return
     */
    @Override
    public ResultPage<Tags> listTags(TagPageReq tagPageReq) {
        LambdaQueryWrapper<Tags> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(tagPageReq.tagName), Tags::getTagName, tagPageReq.tagName)
                .orderByDesc(Tags::getCreateTime);
        Page<Tags> page = page(tagPageReq.build(), lambdaQueryWrapper);
        List<Tags> list = new ArrayList<>();
        if (page.getTotal() > 0) {
            list = new ArrayList<>(page.getRecords());
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    /**
     * 标签详情
     *
     * @param id
     * @return
     */
    @Override
    public Tags getTagsById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 添加标签
     *
     * @param tags
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTag(Tags tags) {
        validateName(tags.tagName);
        baseMapper.insert(tags);
    }

    /**
     * 修改标签
     *
     * @param tags
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTag(Tags tags) {
        Tags entity = baseMapper.selectById(tags.getId());
        if (!entity.tagName.equals(tags.tagName)) validateName(tags.tagName);
        baseMapper.updateById(tags);
    }

    /**
     * 删除标签
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        int row = baseMapper.deleteById(id);
        if (row <= 0) {
            throw new BizException("删除标签失败！");
        }
    }

    /**
     * 批量删除标签
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }


    //    ----web端方法开始-----

    /**
     * 标签列表
     *
     * @return
     */
    @Override
    public List<TagVO> webList() {
        return baseMapper.selectAll();
    }

    //-----------自定义方法开始------------
    public void validateName(String name) {
        Tags entity = baseMapper.selectOne(new QueryWrapper<Tags>().eq("tag_name", name));
        Assert.isNull(entity, "标签名已存在!");
    }
}
