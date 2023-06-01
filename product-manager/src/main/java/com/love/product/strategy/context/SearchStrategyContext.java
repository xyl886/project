package com.love.product.strategy.context;


import com.love.product.model.DTO.PostsSearchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchStrategyContext {

    @Value("${search.mode}")
    private String searchMode;

//    @Resource
//    private Map<String, SearchStrategy> searchStrategyMap;

    public List<PostsSearchDTO> executeSearchStrategy(String keywords) {
//        return searchStrategyMap.get(getStrategy(searchMode)).searchPosts(keywords);
        return null;
    }

}
