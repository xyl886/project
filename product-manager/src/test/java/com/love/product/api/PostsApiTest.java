package com.love.product.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.product.entity.Posts;
import com.love.product.entity.req.PostsPageReq;
import com.love.product.entity.vo.PostsVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 帖子接口集成测试
 */
class PostsApiTest extends BaseIntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("热门帖子列表 — 无需认证")
    void listHot_shouldReturnPosts() throws Exception {
        ResponseEntity<String> resp = get("/posts/listHot");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        assertThat(root.path("code").asInt()).isEqualTo(200);
        assertThat(root.path("data").isArray()).isTrue();
        System.out.println("  热门帖子数: " + root.path("data").size());
    }

    @Test
    @DisplayName("帖子详情 — 根据ID查询")
    void getDetail_shouldReturnPost() throws Exception {
        ResponseEntity<String> resp = get("/posts/getDetail?id=3");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        assertThat(root.path("code").asInt()).isEqualTo(200);
        JsonNode data = root.path("data");
        assertThat(data.isObject()).isTrue();
        assertThat(data.path("id").asText()).isNotBlank();
        System.out.println("  帖子标题: " + data.path("title").asText());
    }

    @Test
    @DisplayName("帖子详情 — 缺少参数应返回400")
    void getDetail_whenMissingId_shouldReturn400() throws Exception {
        ResponseEntity<String> resp = get("/posts/getDetail");

        assertThat(resp.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    @DisplayName("帖子分页查询 — 按类型筛选")
    void getPage_shouldReturnPage() throws Exception {
        PostsPageReq req = new PostsPageReq();
        req.setPostsType(2); // 校园帖

        ResponseEntity<String> resp = post("/posts/getPage", req);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        assertThat(root.path("code").asInt()).isEqualTo(200);
        System.out.println("  分页结果数: " + root.path("data").size());
    }

    @Test
    @DisplayName("帖子搜索 — 关键词搜索")
    void search_shouldReturnResults() throws Exception {
        ResponseEntity<String> resp = get("/posts/articles/search?keywords=论文");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        assertThat(root.path("code").asInt()).isEqualTo(200);
        System.out.println("  搜索结果数: " + root.path("data").size());
    }

    @Test
    @DisplayName("帖子浏览 — 增加浏览量")
    void browse_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/posts/browse?userId=1&id=3");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        // 可能因 Redis 不可用导致业务错误，但 HTTP 状态应正常
        System.out.println("  浏览结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("帖子新增 — 添加新帖子")
    void add_shouldReturnOk() throws Exception {
        PostsVO postsVO = new PostsVO();
        postsVO.title = "测试帖子 - 请忽略";
        postsVO.content = "这是集成测试自动创建的帖子";
        postsVO.categoryId = 1;
        postsVO.postsType = 2;
        postsVO.setPrice(new BigDecimal("0.00"));

        ResponseEntity<String> resp = post("/posts/add", postsVO);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  新增结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("帖子删除（逻辑删除）— 需要认证")
    void del_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = delete("/posts/del?userId=1&id=1");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  删除结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("帖子还原 — 需要认证")
    void restore_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = post("/posts/restore?userId=1&id=1", null);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  还原结果: " + root.path("msg").asText());
    }
}
