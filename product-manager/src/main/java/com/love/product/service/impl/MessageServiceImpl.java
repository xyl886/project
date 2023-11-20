package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.entity.Category;
import com.love.product.entity.Message;
import com.love.product.entity.base.Result;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.MessageDTO;
import com.love.product.entity.req.MessagePageReq;
import com.love.product.entity.vo.CategoryVO;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.mapper.MessageMapper;
import com.love.product.service.MessageService;
import com.love.product.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.love.product.entity.base.ResultCode.PARAMS_ILLEGAL;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: MessageServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/4 11:48
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final HttpServletRequest request;
    @Resource
    private UserInfoService userInfoService;
    /**
     * 留言列表
     * @param messagePageReq
     * @return
     */
    @Override
    public Result listMessage(MessagePageReq messagePageReq) {
        LambdaQueryWrapper<Message> lambdaQueryWrapper = new QueryWrapper<Message>().lambda()
                .orderByDesc(Message::getCreateTime);
        Page<Message> page=page(messagePageReq.build(),lambdaQueryWrapper);
        List<Message> list=new ArrayList<>();
        if (page.getTotal() > 0) {
            list = page.getRecords().stream().map(message -> new Message())
                    .collect(Collectors.toList());
        }
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    /**
     * 批量通过留言
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result passBatch(List<Integer> ids) {
        Assert.notEmpty(ids,PARAMS_ILLEGAL.getMsg());
        baseMapper.passBatch(ids);
        return Result.OK();
    }

    /**
     * 删除留言
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteMessageById(int id) {
        baseMapper.deleteById(id);
        return Result.OK();
    }

    /**
     * 批量删除留言
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? Result.OK(): Result.failMsg("批量删除留言失败");
    }



    //    -------web端方法开始-------
    /**
     * 留言列表
     * @return
     */
    @Override
    public Result selectMessageList() {
        // 查询留言列表
        List<Message> messageList = baseMapper.selectList(
                new LambdaQueryWrapper<Message>().select(
                        Message::getId, Message::getUserId, Message::getContent, Message::getTime, Message::getCreateTime));
        List<MessageDTO> messageDTOList = new ArrayList<>();

        for (Message message : messageList) {
            // 根据用户ID查询用户信息
            UserInfoVO userInfo = userInfoService.getUserInfoById(message.getUserId());

            // 创建 MessageDTO 对象，将留言和用户信息组合
            MessageDTO messageDTO = new MessageDTO();
            BeanUtil.copyProperties(message,messageDTO);
            messageDTO.setAvatar(userInfo.avatar);
            messageDTO.setNickname(userInfo.nickname);
            messageDTO.setEmail(userInfo.email);
            messageDTOList.add(messageDTO);
        }
log.info(messageDTOList.toString());
        return Result.OK(messageDTOList);
    }

    /**
     * 添加留言
     * @param messageDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertMessage(MessageDTO messageDTO) {
        // 获取用户ip
//        String ipAddress = IpUtil.getIp(request);
//        String ipSource = IpUtil.getCityInfo(ipAddress);
//        message.setIpAddress(ipAddress);
//        message.setIpSource(ipSource);
        log.info(String.valueOf(messageDTO));
        messageDTO.setId(IdWorker.getId());
        Message message=new Message();
        BeanUtil.copyProperties(messageDTO,message);
        log.info(String.valueOf(message));
        baseMapper.insert(message);
        return Result.OK("留言成功");
    }
}
