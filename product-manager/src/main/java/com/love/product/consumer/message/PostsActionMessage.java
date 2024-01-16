package com.love.product.consumer.message;

import com.love.product.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsActionMessage implements Serializable {

    private Long postsId;

    private String title;

    private ActionType actionType;

    private LocalDateTime actionTime;
}
