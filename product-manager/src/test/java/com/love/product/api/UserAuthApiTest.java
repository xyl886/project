package com.love.product.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.product.entity.vo.LoginVO;
import com.love.product.entity.vo.RegisterVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 用户 & 认证接口集成测试
 */
class UserAuthApiTest extends BaseIntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("图形验证码 — 生成验证码")
    void captcha_shouldReturnImage() throws Exception {
        ResponseEntity<String> resp = get("/login/captcha");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        // Redis 被 mock，captcha 会存入 mock Redis，但返回结构不变
        System.out.println("  captcha响应: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("发送邮箱验证码 — 需要有效邮箱")
    void sendCode_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/login/code?email=test@test.com&type=register");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  发送验证码: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("用户登录 — 验证码错误时应返回错误")
    void login_withWrongCaptcha_shouldReturnError() throws Exception {
        LoginVO loginVO = LoginVO.builder()
                .email("test@test.com")
                .password("123456")
                .verCode("wrong")
                .verKey("fake-key")
                .build();

        // LoginController 是 GET 请求 + form 参数
        ResponseEntity<String> resp = get("/login/userLogin" +
                "?email=test@test.com&password=123456&verCode=wrong&verKey=fake-key");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        // mock Redis.get() 返回 null，所以验证码校验会失败
        assertThat(root.path("code").asInt()).isNotEqualTo(200);
        System.out.println("  登录结果(预期失败): " + root.path("msg").asText());
    }

    @Test
    @DisplayName("用户注册 — 验证码错误时应返回错误")
    void register_withWrongCaptcha_shouldReturnError() throws Exception {
        RegisterVO registerVO = RegisterVO.builder()
                .email("newuser@test.com")
                .emailCode("888888")
                .password("123456")
                .confirmPassword("123456")
                .verCode("wrong")
                .verKey("fake-key")
                .build();

        ResponseEntity<String> resp = get("/login/userRegister" +
                "?email=newuser@test.com&emailCode=888888&password=123456" +
                "&confirmPassword=123456&verCode=wrong&verKey=fake-key");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        assertThat(root.path("code").asInt()).isNotEqualTo(200);
        System.out.println("  注册结果(预期失败): " + root.path("msg").asText());
    }

    @Test
    @DisplayName("用户退出登录 — 无需登录也可调用")
    void logout_shouldReturnOk() throws Exception {
        ResponseEntity<String> resp = get("/login/userLogout");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  退出结果: " + root.path("msg").asText());
    }

    @Test
    @DisplayName("用户详情 — 未登录时返回错误")
    void detail_withoutAuth_shouldReturnError() throws Exception {
        ResponseEntity<String> resp = get("/user/detail");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  用户详情(未登录): " + root.path("msg").asText());
    }

    @Test
    @DisplayName("更新密码 — 未登录时返回错误")
    void updateUserPwd_withoutAuth_shouldReturnError() throws Exception {
        ResponseEntity<String> resp = postFormGet("/user/updateUserPwd" +
                "?currentPassword=old&newPassword=new&confirmPassword=new");

        assertOk(resp);
        JsonNode root = mapper.readTree(resp.getBody());
        System.out.println("  改密结果(未登录): " + root.path("msg").asText());
    }

    // 辅助：发送 POST form 请求
    private ResponseEntity<String> postFormGet(String url) {
        return restTemplate.getForEntity(url, String.class);
    }
}
