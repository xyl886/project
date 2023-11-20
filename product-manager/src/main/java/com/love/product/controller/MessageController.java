package com.love.product.controller;

import com.love.product.entity.Message;
import com.love.product.entity.base.Result;
import com.love.product.entity.dto.MessageDTO;
import com.love.product.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PackageName: com.love.product.controller
 * @Description: MessageController
 * @Author: Administrator
 * @Date: 2023/8/7 14:51
 */
@RestController
@Slf4j
@Api("消息")
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ApiOperation(value = "留言列表", httpMethod = "GET", response = Result.class, notes = "留言列表")
    public Result selectMessageList(){
        return messageService.selectMessageList();
    }


    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ApiOperation(value = "添加留言", httpMethod = "POST", response = Result.class, notes = "添加留言")
    public Result insertMessage(@RequestBody MessageDTO messageDTO){
        return messageService.insertMessage(messageDTO);
    }
}
