package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.Result;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.Photo;
import com.shiyi.mapper.PhotoMapper;
import com.shiyi.service.PhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.util.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 照片 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    /**
     * 照片列表
     * @param albumId
     * @return
     */
    @Override
    public Result listPhoto(Integer albumId) {
        Page<Photo> photoPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), new QueryWrapper<Photo>().eq(SqlConf.ALBUM_ID, albumId));
        return Result.success(photoPage);
    }

    /**
     * 照片详情
     * @param id
     * @return
     */
    @Override
    public Result getPhotoById(Integer id) {
        Photo photo = baseMapper.selectById(id);
        return Result.success(photo);
    }

    /**
     * 添加照片
     * @param photo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertAlbum(Photo photo) {
        int rows = baseMapper.insert(photo);
        return rows > 0 ? Result.success(): Result.error("添加照片失败");
    }

    /**
     * 修改照片
     * @param photo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatePhoto(Photo photo) {
        int rows = baseMapper.updateById(photo);
        return rows > 0 ? Result.success(): Result.error("修改照片失败");
    }

    /**
     * 移动照片
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result movePhoto(Map<String,Object> map) {
        Integer albumId = (Integer) map.get("albumId");
        List<Integer> ids = (List<Integer>) map.get("ids");
        baseMapper.movePhoto(ids,albumId);
        return Result.success();
    }

    /**
     * 批量删除照片
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? Result.success(): Result.error("删除照片失败");
    }
}
