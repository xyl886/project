package com.love.product.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.love.product.constant.RabbitMQConstant.*;

@Configuration
//@ConfigurationProperties("rabbitmq")
public class RabbitMQConfig {

    @Bean
    public Queue articleQueue() {
        return new Queue(MAXWELL_QUEUE, true);
    }

    @Bean
    public FanoutExchange maxWellExchange() {
        return new FanoutExchange(MAXWELL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingArticleDirect() {
        return BindingBuilder.bind(articleQueue()).to(maxWellExchange());
    }

    /**
     * 邮箱验证码
     * @return
     */

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

    /**
     * 帖子排行榜
     * @return
     */
    @Bean
    public Queue postsActionQueue() {
        return new Queue(POSTS_ACTION_QUEUE);
    }

    @Bean
    public DirectExchange postsActionExchange() {
        return new DirectExchange(POSTS_ACTION_EXCHANGE);
    }

    @Bean
    public Binding bindingPostsActionDirect() {
        return BindingBuilder.bind(postsActionQueue()).to(postsActionExchange()).with(POSTS_ACTION_ROUTING_KEY);
    }

//    @Bean
//    public Binding bindingPostsActionDirect(Queue queue,DirectExchange directExchange) {
//        return BindingBuilder.bind(queue).to(directExchange).with(POSTS_ACTION_ROUTING_KEY);
//    }
    @Bean
    public Queue subscribeQueue() {
        return new Queue(SUBSCRIBE_QUEUE, true);
    }

    @Bean
    public FanoutExchange subscribeExchange() {
        return new FanoutExchange(SUBSCRIBE_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingSubscribeDirect() {
        return BindingBuilder.bind(subscribeQueue()).to(subscribeExchange());
    }
    @Bean
    public Queue queue() {
        return QueueBuilder.durable("testQueue").build();
    }

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange("testExchange").build();
    }

    @Bean
    public Binding binding(@Qualifier("queue") Queue queue, @Qualifier("exchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("test.#").noargs();
    }
}
