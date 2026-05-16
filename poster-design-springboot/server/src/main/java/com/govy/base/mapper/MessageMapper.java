package com.govy.base.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.model.dto.MessageQueryLikeDTO;
import com.govy.model.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.govy.model.vo.MessageVO;

/**
* @author Govy
* @description 针对表【message】的数据库操作Mapper
* @createDate 2025-07-04 09:53:35
* @Entity com.govy.model.entity.Message
*/
public interface MessageMapper extends BaseMapper<Message> {

    Page<MessageVO> selectPageBySendMessageDTO(Page<Message> page, MessageQueryLikeDTO messageDTO);

    Page<MessageVO> selectPageByReceiveMessageDTO(Page<Message> page, MessageQueryLikeDTO messageDTO);
}




