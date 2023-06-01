package com.love.product.strategy;



import com.love.product.model.DTO.PostsSearchDTO;

import java.util.List;

public interface SearchStrategy {

    List<PostsSearchDTO> searchPosts(String keywords);

}
