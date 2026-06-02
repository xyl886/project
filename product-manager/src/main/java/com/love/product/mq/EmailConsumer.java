package com.love.product.mq;

import com.alibaba.fastjson2.JSON;
import com.love.product.entity.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import static com.love.product.constant.RabbitMQConstant.EMAIL_QUEUE;

/**
 * 邮件发送消费者
 * 从 RabbitMQ 获取邮件消息并异步发送
 */
@Slf4j
@Component
public class EmailConsumer {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @RabbitListener(queues = EMAIL_QUEUE)
    public void process(byte[] messageBody) {
        try {
            EmailDTO emailDTO = JSON.parseObject(messageBody, EmailDTO.class);
            if (emailDTO == null || emailDTO.getEmail() == null) {
                log.warn("邮件消息格式错误，跳过");
                return;
            }
            if (mailUsername.isEmpty()) {
                log.warn("邮箱未配置（spring.mail.username 为空），无法发送邮件到: {}", emailDTO.getEmail());
                return;
            }
            sendEmail(emailDTO);
        } catch (Exception e) {
            log.error("处理邮件消息失败", e);
        }
    }

    private void sendEmail(EmailDTO emailDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailUsername);
            helper.setTo(emailDTO.getEmail());
            helper.setSubject(emailDTO.getSubject());

            Context context = new Context();
            context.setVariables(emailDTO.getCommentMap());
            String content = templateEngine.process(emailDTO.getTemplate(), context);
            helper.setText(content, true);

            javaMailSender.send(mimeMessage);
            log.info("邮件发送成功 -> {}", emailDTO.getEmail());
        } catch (Exception e) {
            log.error("邮件发送失败 -> {}", emailDTO.getEmail(), e);
        }
    }
}
