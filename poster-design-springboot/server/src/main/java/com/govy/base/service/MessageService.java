package com.govy.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.model.dto.MessageChangeDTO;
import com.govy.model.dto.MessageDTO;
import com.govy.model.dto.MessageQueryLikeDTO;
import com.govy.model.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.govy.model.vo.MessageVO;

/**
* @author Govy
* @description 针对表【message】的数据库操作Service
* @createDate 2025-07-04 09:53:35
*/
public interface MessageService extends IService<Message> {

    void updateStatusById(MessageChangeDTO messageChangeDTO);

    void saveMessage(MessageDTO messageVO);

    Page<MessageVO> getBySendMessageDTO(MessageQueryLikeDTO messageDTO);

    Page<MessageVO> getByReceiveMessageDTO(MessageQueryLikeDTO messageDTO);
}
