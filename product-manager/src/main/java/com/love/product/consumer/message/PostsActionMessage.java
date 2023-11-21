package com.love.product.consumer.message;

import com.love.product.enumerate.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsActionMessage {

    private Long postsId;

    private String title;

    private ActionType actionType;

    private LocalDateTime actionTime;

    // 构造函数、getter和setter方法省略
}
