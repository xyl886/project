package com.love.product.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.product.entity.dto.MessageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 留言 & 评论 & 点赞 接口集成测试
 */
class SocialApiTest extends BaseIntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    // ── 留言板 ──

    @Test
    @DisplayName("留言列表 — 查询所有留言")
    void messageList_shouldReturnMessages() throws Exception {
        ResponseEntity<String> resp = get("/message/");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  留言列表: code=" + root.path("code").asInt()
                + ", 数据=" + root.path("data").toString());
    }

    @Test
    @DisplayName("留言新增 — 添加留言")
    void messageInsert_shouldReturnOk() throws Exception {
        MessageDTO dto = MessageDTO.builder()
                .content("集成测试留言-" + System.currentTimeMillis())
                .nickname("test")
                .build();

        ResponseEntity<String> resp = post("/message/", dto);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  新增留言: " + root.path("msg").asText());
    }

    // ── 评论 ──

    @Test
    @DisplayName("评论列表 — 根据帖子ID查询评论")
    void commentList_shouldReturnComments() throws Exception {
        ResponseEntity<String> resp = get("/postsComment/listByPostsId?postsId=3");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  评论列表: code=" + root.path("code").asInt());
    }

    @Test
    @DisplayName("评论新增 — 添加评论")
    void commentAdd_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/postsComment/add?postsId=3&content=集成测试评论");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  新增评论: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("评论点赞 — 点赞评论")
    void commentLike_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/postsComment/addCommentLike?commentId=1&deleted=0");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  评论点赞: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("评论删除 — 删除评论")
    void commentDel_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/postsComment/del?id=1");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  删除评论: " + root.path("msg").asText());
    }

    // ── 点赞 ──

    @Test
    @DisplayName("点赞新增 — 点赞帖子")
    void likeAdd_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/postsLike/add?postsId=3&deleted=0");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  点赞结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("点赞/取消点赞 — 切换点赞状态")
    void likeToggle_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/postsLike/like?postsId=3");
        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  点赞: " + root.path("msg").asText());

        resp = get("/postsLike/dislike?postsId=3");
        assertOk(resp);
        root = mapper.readTree(resp.getBody());
        System.out.println("  取消点赞: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("点赞分页 — 查询点赞记录")
    void likeGetPage_shouldReturnPage() throws Exception {
        com.love.product.entity.req.HistoryPageReq req = new com.love.product.entity.req.HistoryPageReq();

        ResponseEntity<String> resp = post("/postsLike/getPage", req);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  点赞分页: code=" + root.path("code").asInt());
    }
}
