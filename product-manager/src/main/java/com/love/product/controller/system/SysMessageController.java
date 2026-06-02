package com.love.product.controller.system;

import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.req.MessagePageReq;
import com.love.product.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @PackageName: com.love.product.controller.system
 * @Description: SysMessageController
 * @Author: Administrator
 * @Date: 2023/8/7 10:56
 */
@RestController
@RequestMapping("/system/message")
@Api(tags = "留言管理")
@RequiredArgsConstructor
public class SysMessageController {

    private final MessageService messageService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    @ApiOperation(value = "留言列表", httpMethod = "GET", response = Result.class, notes = "留言列表")
    public ResultPage list(MessagePageReq messagePageReq){
        return messageService.listMessage(messagePageReq);
    }

    @RequestMapping(value="/passBatch",method = RequestMethod.POST)
    @ApiOperation(value = "批量通过", httpMethod = "POST", response = Result.class, notes = "批量通过")
    public Result<?> passBatch(@RequestBody List<Integer> ids){
        messageService.passBatch(ids);
        return Result.OK();
    }


    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除留言", httpMethod = "DELETE", response = Result.class, notes = "删除留言")
    public Result<?> deleteMessageById(int id){
        messageService.deleteMessageById(id);
        return Result.OK();
    }


    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除留言", httpMethod = "DELETE", response = Result.class, notes = "批量删除留言")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        messageService.deleteBatch(ids);
        return Result.OK();
    }
}
