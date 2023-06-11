package com.love.product.strategy;



import com.love.product.entity.dto.PostsSearchDTO;

import java.util.List;

public interface SearchStrategy {

    List<PostsSearchDTO> searchPosts(String keywords);

}
