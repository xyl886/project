package com.shiyi.service;

import com.shiyi.common.Result;
import com.shiyi.entity.PhotoAlbum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 相册 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
public interface PhotoAlbumService extends IService<PhotoAlbum> {

    Result listAlbum(String name);

    Result getAlbumById(Integer id);

    Result insertAlbum(PhotoAlbum photoAlbum);

    Result updateAlbum(PhotoAlbum photoAlbum);

    Result deleteAlbumById(Integer id);





    //web端方法开始
    Result webAlbumList();

    Result webListPhotos(Integer albumId);

}
