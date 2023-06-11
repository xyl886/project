package com.love.product.mapper;


import com.love.product.entity.dto.PostsSearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * @author 花未眠
 * elasticsearch
 */
@Repository
public interface ElasticsearchMapper extends ElasticsearchRepository<PostsSearchDTO,Integer> {

}
