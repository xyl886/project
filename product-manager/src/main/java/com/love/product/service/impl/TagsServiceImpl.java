package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Category;
import com.love.product.entity.Tags;
import com.love.product.entity.base.PageQuery;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.TagPageReq;
import com.love.product.entity.vo.CategoryVO;
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
import java.util.stream.Collectors;

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
    public Result getTagsById(Long id) {
        Tags tags = baseMapper.selectById(id);
        return Result.OK(tags);
    }

    /**
     * 添加标签
     *
     * @param tags
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertTag(Tags tags) {
        validateName(tags.tagName);
        baseMapper.insert(tags);
        return Result.OK();
    }

    /**
     * 修改标签
     *
     * @param tags
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateTag(Tags tags) {
        Tags entity = baseMapper.selectById(tags.getId());
        if (!entity.tagName.equals(tags.tagName)) validateName(tags.tagName);
        baseMapper.updateById(tags);
        return Result.OK();
    }

    /**
     * 删除标签
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteById(Long id) {
        int row = baseMapper.deleteById(id);
        return row > 0 ? Result.OK() : Result.failMsg("删除标签失败！");
    }

    /**
     * 批量删除标签
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        return Result.OK();
    }


    //    ----web端方法开始-----

    /**
     * 标签列表
     *
     * @return
     */
    @Override
    public Result webList() {
        List<TagVO> list = baseMapper.selectAll();
        return Result.OK(list);
    }

    //-----------自定义方法开始------------
    public void validateName(String name) {
        Tags entity = baseMapper.selectOne(new QueryWrapper<Tags>().eq("tag_name", name));
        Assert.isNull(entity, "标签名已存在!");
    }
}
