package com.govy.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.govy.base.mapper.TeamMapper;
import com.govy.base.service.TeamUserService;
import com.govy.base.service.UserService;
import com.govy.common.constant.TableConstant;
import com.govy.common.handler.BaseException;
import com.govy.common.login.LoginUserHolder;
import com.govy.common.result.ResultCodeEnum;
import com.govy.model.dto.MessageChangeDTO;
import com.govy.model.dto.MessageDTO;
import com.govy.model.dto.MessageQueryLikeDTO;
import com.govy.model.entity.Message;
import com.govy.base.service.MessageService;
import com.govy.base.mapper.MessageMapper;
import com.govy.model.entity.Team;
import com.govy.model.entity.TeamUser;
import com.govy.model.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
* @author Govy
* @description 针对表【message】的数据库操作Service实现
* @createDate 2025-07-04 09:53:35
*/
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    private final MessageMapper messageMapper;
    private final UserService userService;
    private final TeamUserService teamUserService;
    private final TeamMapper teamMapper;

    /**
     * 根据id修改消息状态, 消息发送状态（0待处理、1已接收、2已拒绝、3已撤回）
     * @param messageChangeDTO
     */
    @Override
    @Transactional
    public void updateStatusById(MessageChangeDTO messageChangeDTO) {
        // 0.根据队伍id查询，该队伍是否存在
        Team team = teamMapper.selectById(messageChangeDTO.getTeamId());
        if (ObjectUtil.isNull(team)) {
            throw new BaseException(ResultCodeEnum.TEAM_NOT_EXIST);
        }

        // 1.根据消息id更改
        this.update(new LambdaUpdateWrapper<Message>()
                .set(Message::getStatus, messageChangeDTO.getNewStatus())
                .eq(Message::getId, messageChangeDTO.getId())
        );

        // 2. 获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();
        // 2.根据状态修改数据
       if (messageChangeDTO.getStatus() == 0) {
           if (messageChangeDTO.getNewStatus() == 1) {
               // 0待处理 --> 1已接收
               // 2.1 查看用户是否已存在队伍中
               List<Integer> userIds = teamMapper.selectUserIdByTeamId(messageChangeDTO.getTeamId());
               if (CollUtil.contains(userIds, userId)) {
                   // 已存在队伍中，不能发送信息
                   throw new BaseException(ResultCodeEnum.USER_EXIST_TEAM);
               }

               // 添加到队伍中
               TeamUser teamUser = new TeamUser();
               teamUser.setTeamId(messageChangeDTO.getTeamId());
               teamUser.setUserId(userId);
               teamUser.setStatus(TableConstant.MEMBER);
               teamUserService.save(teamUser);
           } else  {
               // 0待处理 --> 2已拒绝 / --> 3已撤回
               // 直接更改信息状态
               this.update(new LambdaUpdateWrapper< Message>()
                       .set(Message::getStatus, messageChangeDTO.getNewStatus())
                       .eq(Message::getId, messageChangeDTO.getId())
               );
           }
       }
    }

    /**
     * 发送信息
     * @param messageDTO
     */
    @Override
    public void saveMessage(MessageDTO messageDTO) {
        if (messageDTO.getCategory() == 0) {
            // 1.查看团队是否锁定
            Integer teamId = messageDTO.getTeamId();
            Integer teamStatus = teamMapper.selectTeamStatusByTeamId(teamId);
            if (teamStatus.equals(TableConstant.TEAM_LOCKED)) {
                // 锁定中，不能发送信息
                throw new BaseException(ResultCodeEnum.TEAM_LOCKED);
            }

            // 2.查看邀请用户是否已存在队伍中
            List<Integer> userIds = teamMapper.selectUserIdByTeamId(teamId);
            if (CollUtil.contains(userIds, messageDTO.getReceiveId())) {
                // 已存在队伍中，不能发送信息
                throw new BaseException(ResultCodeEnum.USER_EXIST_TEAM);
            }
        }

        // 3.获取用户id
        Integer userId = LoginUserHolder.getLoginUser().getUserId();

        // 4.保存信息
        Message message = BeanUtil.copyProperties(messageDTO, Message.class);
        message.setSendId(userId);
        message.setCreateTime(LocalDateTime.now());
        message.setStatus(TableConstant.MESSAGE_WAIT);

        this.save(message);
    }


    /**
     * 根据信息DTO获取发送信息
     * @param messageDTO
     * @return
     */
    @Override
    public Page<MessageVO> getBySendMessageDTO(MessageQueryLikeDTO messageDTO) {
        // 0.分页条件
        Page<Message> page = Page.of(messageDTO.getPageNo(), messageDTO.getPageSize());

        // 1.根据条件动态查询
        Page<MessageVO> list = messageMapper.selectPageBySendMessageDTO(page, messageDTO);

        // 2.如果数据为空，直接返回空数据
        if (list == null) {
            return CollUtil.empty(MessageVO.class);
        }

        // 3.封装成VO返回
        return list;
    }

    /**
     * 根据信息DTO获取接受信息
     * @param messageDTO
     * @return
     */
    @Override
    public Page<MessageVO> getByReceiveMessageDTO(MessageQueryLikeDTO messageDTO) {
        // 0.分页条件
        Page<Message> page = Page.of(messageDTO.getPageNo(), messageDTO.getPageSize());

        // 1.根据条件动态查询
        Page<MessageVO> list = messageMapper.selectPageByReceiveMessageDTO(page, messageDTO);

        // 2.如果数据为空，直接返回空数据
        if (list == null) {
            return CollUtil.empty(MessageVO.class);
        }

        // 3.封装成VO返回
        return list;
    }
}




