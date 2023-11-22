package com.love.product.job.once;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.gson.Gson;
import com.love.product.entity.Posts;
import com.love.product.entity.dto.PostEsDTO;
import com.love.product.mapper.EsMapper;
import com.love.product.service.PostsService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全量同步帖子到 es
 *
 * @author lacy
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private PostsService postService;

    @Resource
    private EsMapper esMapper;

    @Override
    public void run(String... args) {
        List<Posts> postList = postService.list();
        if (CollectionUtils.isEmpty(postList)) {
            return;
        }
        List<PostEsDTO> postEsDTOList = postList.stream()
                .map(PostEsDTO::objToDto)
                .collect(Collectors.toList());
        log.info(postEsDTOList.toString());
        final int pageSize = 500;
        int total = postEsDTOList.size();
        log.info("FullSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            esMapper.saveAll(postEsDTOList.subList(i, end));
        }
        log.info("FullSyncPostToEs end, total {}", total);
        log.info(new Gson().toJson(postEsDTOList.stream()
                .map(PostEsDTO::getTitle_suggest)
                .collect(Collectors.toList())));
    }
}
