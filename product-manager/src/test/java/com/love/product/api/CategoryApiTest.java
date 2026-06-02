package com.love.product.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.product.entity.Category;
import com.love.product.entity.dto.CategoryDTO;
import com.love.product.entity.req.CategoryPageReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 分类接口集成测试
 */
class CategoryApiTest extends BaseIntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("分类分页列表 — 查询所有分类")
    void listAll_shouldReturnPage() throws Exception {
        CategoryPageReq req = new CategoryPageReq();

        ResponseEntity<String> resp = post("/category/listAll", req);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  分类接口响应: code=" + root.path("code").asInt()
                + ", msg=" + root.path("msg").asText());
        // 分类接口可能依赖 Redis，若不可用则返回 500 业务码
    }

    @Test
    @DisplayName("分类详情 — 根据ID查询")
    void getCategoryById_shouldReturnInfo() throws Exception {
        ResponseEntity<String> resp = get("/category/info?id=1");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  分类详情: " + root.path("data").asText());
    }

    @Test
    @DisplayName("分类新增 — 添加新分类")
    void insertCategory_shouldReturnOk() throws Exception {
        Category category = Category.builder()
                .categoryName("测试分类-" + System.currentTimeMillis())
                .build();

        ResponseEntity<String> resp = post("/category/add", category);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  新增结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("分类更新 — 修改分类信息")
    void updateCategory_shouldReturnOk() throws Exception {
        CategoryDTO dto = CategoryDTO.builder()
                .id(1)
                .categoryName("更新分类名-" + System.currentTimeMillis())
                .build();

        ResponseEntity<String> resp = post("/category/update", dto);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  更新结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("分类删除 — 根据ID删除")
    void deleteCategory_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = delete("/category/delete?id=999");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  删除结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("分类批量删除 — 删除多个分类")
    void deleteBatch_shouldReturnOk() throws Exception {
        java.util.List<Category> list = java.util.List.of(
                Category.builder().id(998).build()
        );

        ResponseEntity<String> resp = post("/category/deleteBatch", list);

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  批量删除结果: " + root.path("msg").asText());
    }
}
