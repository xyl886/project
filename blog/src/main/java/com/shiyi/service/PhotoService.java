package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.Photo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 照片 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
public interface PhotoService extends IService<Photo> {

    Result listPhoto(Integer albumId);

    Result getPhotoById(Integer id);

    Result insertAlbum(Photo photo);

    Result updatePhoto(Photo photo);

    Result movePhoto(Map<String,Object> map);

    Result deleteBatch(List<Integer> ids);

}
