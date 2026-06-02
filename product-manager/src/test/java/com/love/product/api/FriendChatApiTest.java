package com.love.product.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.product.entity.ChatMessage;
import com.love.product.entity.req.FriendPageReq;
import com.love.product.entity.req.HistoryPageReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 好友 & 聊天 & 浏览记录 接口集成测试
 */
class FriendChatApiTest extends BaseIntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    // ── 好友 ──

    @Test
    @DisplayName("好友列表 — 查询好友列表")
    void friendList_shouldReturnList() throws Exception {
        FriendPageReq req = new FriendPageReq();

        ResponseEntity<String> resp = post("/friend/getFriendList", req);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  好友列表: code=" + root.path("code").asInt());
    }

    @Test
    @DisplayName("添加好友 — 发送好友请求")
    void addFriend_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = post("/friend/addFriend?id=2", null);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  添加好友: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("删除好友 — 解除好友关系")
    void deleteFriend_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = delete("/friend/deleteFriend?friendUserId=2");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  删除好友: " + root.path("msg").asText());
    }

    // ── 聊天消息 ──

    @Test
    @DisplayName("聊天好友列表 — 查询聊天好友")
    void chatFriendList_shouldReturnList() throws Exception {
        FriendPageReq req = new FriendPageReq();

        ResponseEntity<String> resp = post("/chat_message/getFriendList", req);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  聊天好友列表: code=" + root.path("code").asInt());
    }

    @Test
    @DisplayName("发送消息 — 保存聊天消息")
    void saveOneMsg_shouldReturnOk() throws Exception {
        ChatMessage msg = ChatMessage.builder()
                .fromId(1L)
                .toId(2L)
                .message("集成测试消息-" + System.currentTimeMillis())
                .build();

        ResponseEntity<String> resp = post("/chat_message/saveOneMsg", msg);

        assertOk(resp);
        System.out.println("  发送消息: HTTP " + resp.getStatusCodeValue());
    }

    @Test
    @DisplayName("消息列表 — 查询历史消息")
    void listMessages_shouldReturnMessages() throws Exception {
        ResponseEntity<String> resp = get("/chat_message/listMessages?fromId=1&toId=2");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  消息列表: code=" + root.path("code").asInt());
    }

    @Test
    @DisplayName("删除消息 — 删除聊天记录")
    void deleteMsg_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = delete("/chat_message/deleteMsg?id=1&fromId=1&toId=2");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  删除消息: " + root.path("msg").asText());
    }

    // ── 浏览记录 ──

    @Test
    @DisplayName("浏览记录分页 — 查询浏览历史")
    void historyGetPage_shouldReturnPage() throws Exception {
        HistoryPageReq req = new HistoryPageReq();

        ResponseEntity<String> resp = post("/history/getPage", req);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  浏览记录: code=" + root.path("code").asInt());
    }

    @Test
    @DisplayName("清空浏览记录 — 删除历史")
    void historyDel_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = delete("/history/del?userId=1&ids=1,2,3");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  清空记录: " + root.path("msg").asText());
    }
}
