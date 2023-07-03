package com.shiyi.service;

import com.shiyi.common.Result;
import org.springframework.web.multipart.MultipartFile;

public interface CloudOssService {
    /**
     * 上传
     * @param file 文件
     * @return
     */
    Result upload(MultipartFile file);

    /**
     * 批量删除文件
     * @param key 文件名
     * @return
     */
    Result delBatchFile(String ...key);
}
