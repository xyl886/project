package com.love.product.mapper;

import com.love.product.entity.dto.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsMapper extends ElasticsearchRepository<PostEsDTO, Long> {

//    List<PostEsDTO> findByUserId(Long userId);
}
