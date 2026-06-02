package com.love.product.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.product.entity.req.CollectPageReq;
import com.love.product.entity.req.FollowPageReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 收藏 & 关注接口集成测试
 */
class CollectFollowApiTest extends BaseIntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    // ── 收藏 ──

    @Test
    @DisplayName("收藏新增 — 收藏帖子")
    void collectAdd_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/collect/add?postsIds=3&deleted=0");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  收藏结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("收藏分页 — 查询收藏列表")
    void collectGetPage_shouldReturnPage() throws Exception {
        CollectPageReq req = new CollectPageReq();

        ResponseEntity<String> resp = post("/collect/getPage", req);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  收藏分页: code=" + root.path("code").asInt());
    }

    @Test
    @DisplayName("收藏批量取消 — 取消多个收藏")
    void collectDeleteBatch_shouldReturnOk() throws Exception {
        List<Long> ids = List.of(1L, 2L);

        ResponseEntity<String> resp = post("/collect/deleteBatch", ids);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  取消收藏结果: " + root.path("msg").asText());
    }

    // ── 关注 ──

    @Test
    @DisplayName("关注新增 — 关注用户")
    void followAdd_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/follow/add?beFollowedUserId=2&deleted=0");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  关注结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("关注分页 — 查询关注列表")
    void followGetPage_shouldReturnPage() throws Exception {
        FollowPageReq req = new FollowPageReq();
        req.setFollowType(1);

        ResponseEntity<String> resp = post("/follow/getPage", req);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  关注分页: code=" + root.path("code").asInt());
    }
}
