package com.love.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.love.product.config.BizException;
import com.love.product.entity.Message;
import com.love.product.entity.base.ResultPage;
import com.love.product.entity.dto.MessageDTO;
import com.love.product.entity.req.MessagePageReq;
import com.love.product.entity.vo.UserInfoVO;
import com.love.product.mapper.MessageMapper;
import com.love.product.service.MessageService;
import com.love.product.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.love.product.entity.base.ResultCode.PARAMS_ILLEGAL;

/**
 * @PackageName: com.love.product.service.impl
 * @Description: MessageServiceImpl
 * @Author: Administrator
 * @Date: 2023/7/4 11:48
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    private HttpServletRequest request;
    @Resource
    private UserInfoService userInfoService;
    /**
     * 留言列表
     * @param messagePageReq
     * @return
     */
    @Override
    public ResultPage listMessage(MessagePageReq messagePageReq) {
        LambdaQueryWrapper<Message> lambdaQueryWrapper = new QueryWrapper<Message>().lambda()
                .orderByDesc(Message::getCreateTime);
        Page<Message> page=page(messagePageReq.build(),lambdaQueryWrapper);
        List<Message> list = page.getTotal() > 0 ? page.getRecords() : new ArrayList<>();
        return ResultPage.OK(page.getTotal(), page.getCurrent(), page.getSize(), list);
    }

    /**
     * 批量通过留言
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passBatch(List<Integer> ids) {
        Assert.notEmpty(ids, PARAMS_ILLEGAL.getMsg());
        baseMapper.passBatch(ids);
    }

    /**
     * 删除留言
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessageById(int id) {
        baseMapper.deleteById(id);
    }

    /**
     * 批量删除留言
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        if (rows <= 0) {
            throw new BizException("批量删除留言失败");
        }
    }



    //    -------web端方法开始-------
    /**
     * 留言列表
     * @return
     */
    @Override
    public List<MessageDTO> selectMessageList() {
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
        return messageDTOList;
    }

    /**
     * 添加留言
     * @param messageDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMessage(MessageDTO messageDTO) {
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
    }
}
