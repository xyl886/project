package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.Result;
import com.shiyi.entity.Message;
import com.shiyi.mapper.MessageMapper;
import com.shiyi.service.MessageService;
import com.shiyi.util.IpUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.shiyi.common.ResultCode.PARAMS_ILLEGAL;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final HttpServletRequest request;

    /**
     * 留言列表
     * @param name
     * @return
     */
    @Override
    public Result listMessage(String name) {
        LambdaQueryWrapper<Message> queryWrapper = new QueryWrapper<Message>().lambda()
                .like(StringUtils.isNotBlank(name),Message::getNickname,name).orderByDesc(Message::getCreateTime);
        Page<Message> list = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),queryWrapper);
        return Result.success(list);
    }

    /**
     * 批量通过留言
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result passBatch(List<Integer> ids) {
        Assert.notEmpty(ids,PARAMS_ILLEGAL.getDesc());
        baseMapper.passBatch(ids);
        return Result.success();
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
        return Result.success();
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
        return rows > 0 ? Result.success(): Result.error("批量删除留言失败");
    }



                //    -------web端方法开始-------
    /**
     * 留言列表
     * @return
     */
    @Override
    public Result webMessage() {
        // 查询留言列表
        List<Message> messageList = baseMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar,
                        Message::getContent, Message::getTime));
        return Result.success(messageList);
    }

    /**
     * 添加留言
     * @param message
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result webAddMessage(Message message) {
        // 获取用户ip
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getCityInfo(ipAddress);
        message.setIpAddress(ipAddress);
        message.setIpSource(ipSource);
        baseMapper.insert(message);
        return Result.success("留言成功");
    }


}
