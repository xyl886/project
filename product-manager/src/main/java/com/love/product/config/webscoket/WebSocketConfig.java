package com.love.product.config.webscoket;

import com.love.product.config.rabbitmq.RabbitMQProperties;
import com.love.product.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author timeless
 * @create 2023-06-24 2:32
 * @Description: 注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，这时候控制器（controller）
 * 开始支持@MessageMapping,就像是使用@requestMapping一样。
 */
@Configuration
@Slf4j
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(RabbitMQProperties.class)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public WebSocketConfig(RabbitMQProperties rabbitMQProperties){
        log.error(rabbitMQProperties.getHost());
    }

    /**
     * 注册端点，发布或者订阅消息的时候需要连接此端点
     * setAllowedOrigins 非必须，*表示允许其他域进行连接
     * withSockJS  表示开始sockejs支持
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/webSocket")
                .setAllowedOrigins("http://localhost:8087",CommonConstant.ALLOWED_ORIGINS)
                .withSockJS();
    }

    /**
     * 配置消息代理(中介)
     * enableSimpleBroker 服务端推送给客户端的路径前缀
     * setApplicationDestinationPrefixes  客户端发送数据给服务器端的一个前缀
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 基于内存的消息代理
        registry.enableSimpleBroker(CommonConstant.SIMPLE_BROKER);
        registry.setApplicationDestinationPrefixes(CommonConstant.DESTINATION_PREFIXES);

        // Use this for enabling a Full featured broker like RabbitMQ
//        registry.enableStompBrokerRelay(Constants.SIMPLE_BROKER)
//                .setVirtualHost(rabbitMQProperties.getVirtualHost())
//                .setRelayHost(rabbitMQProperties.getHost())
//                .setRelayPort(61613)
//                .setClientLogin(rabbitMQProperties.getUsername())
//                .setClientPasscode(rabbitMQProperties.getPassword())
//                .setSystemLogin(rabbitMQProperties.getUsername())
//                .setSystemPasscode(rabbitMQProperties.getPassword())
//                .setSystemHeartbeatSendInterval(5000)
//                .setSystemHeartbeatReceiveInterval(5000);

    }
}
