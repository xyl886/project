package com.love.product.api;

import com.love.product.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 集成测试基类
 * 启动完整 Spring 应用，通过真实 HTTP 请求测试接口
 * 注意：需要 MySQL 运行在 localhost:3306
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected RedisService redisService;

    protected String authToken;
    protected HttpHeaders authHeaders = new HttpHeaders();

    @BeforeEach
    void setUpAuth() {
        authToken = null;
        authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    // ── HTTP 请求快捷方法 ──

    protected ResponseEntity<String> get(String url) {
        return restTemplate.getForEntity(url, String.class);
    }

    protected ResponseEntity<String> get(String url, Map<String, Object> params) {
        String expanded = UriComponentsBuilder.fromPath(url)
                .buildAndExpand(params).toUriString();
        return restTemplate.getForEntity(expanded, String.class);
    }

    protected ResponseEntity<String> post(String url, Object body) {
        HttpEntity<Object> entity = new HttpEntity<>(body, authHeaders);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    protected ResponseEntity<String> delete(String url) {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders);
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
    }

    // ── 带认证的请求 ──

    protected HttpEntity<Void> authEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (authToken != null) {
            headers.set("Authorization", authToken);
        }
        return new HttpEntity<>(headers);
    }

    protected ResponseEntity<String> authGet(String url) {
        return restTemplate.exchange(url, HttpMethod.GET, authEntity(), String.class);
    }

    protected ResponseEntity<String> authPost(String url, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) {
            headers.set("Authorization", authToken);
        }
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
    }

    protected ResponseEntity<String> authDelete(String url) {
        return restTemplate.exchange(url, HttpMethod.DELETE, authEntity(), String.class);
    }

    // ── 解析响应 ──

    protected int extractCode(ResponseEntity<String> response) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readTree(response.getBody()).path("code").asInt();
        } catch (Exception e) {
            return -1;
        }
    }

    protected String extractMsg(ResponseEntity<String> response) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readTree(response.getBody()).path("msg").asText();
        } catch (Exception e) {
            return "";
        }
    }

    // ── 断言快捷方法 ──

    protected void assertOk(ResponseEntity<String> response) {
        org.assertj.core.api.Assertions.assertThat(response.getStatusCode())
                .isIn(HttpStatus.OK, HttpStatus.CREATED);
    }

    protected void assertBizOk(ResponseEntity<String> response) {
        assertOk(response);
        org.assertj.core.api.Assertions.assertThat(extractCode(response)).isEqualTo(200);
    }

    /**
     * TestConfiguration 提供假的 RedisService，避免测试依赖实际 Redis 服务
     */
    @TestConfiguration
    public static class MockRedisConfig {
        @Bean
        @Primary
        public RedisService mockRedisService() {
            RedisService mock = mock(RedisService.class);
            // @AccessLimit 限流注解需要 lua 脚本返回 1
            when(mock.executeLuaScript(anyString(), anyList(), any())).thenReturn(1L);
            // 默认 get 返回 null
            when(mock.get(anyString())).thenReturn(null);
            // del 模拟成功
            when(mock.del(anyString())).thenReturn(true);
            // hasKey 模拟不存在
            when(mock.hasKey(anyString())).thenReturn(false);
            // expire 模拟成功
            when(mock.expire(anyString(), anyLong())).thenReturn(true);
            when(mock.expire(anyString(), anyLong(), any())).thenReturn(true);
            // set 模拟成功
            when(mock.sAdd(anyString(), any())).thenReturn(1L);
            // incr/dec
            when(mock.incr(anyString(), anyLong())).thenReturn(1L);
            when(mock.decr(anyString(), anyLong())).thenReturn(0L);
            return mock;
        }
    }
}
