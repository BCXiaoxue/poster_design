package com.govy.base.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.govy.base.service.MessageService;
import com.govy.common.result.Result;
import com.govy.model.dto.MessageChangeDTO;
import com.govy.model.dto.MessageDTO;
import com.govy.model.dto.MessageQueryLikeDTO;

import com.govy.model.vo.MessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "消息管理", description = "消息相关接口")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    /**
     * 根据信息DTO获取我发送的信息
     * @param messageDTO
     * @return
     */
    @Operation(summary = "根据信息DTO获取我发送的信息")
    @PostMapping("/getBySendMessageDTO")
    public Result<Page<MessageVO>> getBySendMessageDTO(@RequestBody MessageQueryLikeDTO messageDTO) {
//        log.info("根据信息DTO获取信息: {}", messageDTO);

        Page<MessageVO> list = messageService.getBySendMessageDTO(messageDTO);

        return Result.success(list);
    }

    /**
     * 根据信息DTO获取我发送的信息
     * @param messageDTO
     * @return
     */
    @Operation(summary = "根据信息DTO获取我接收的信息")
    @PostMapping("/getByReceiveMessageDTO")
    public Result<Page<MessageVO>> getByReceiveMessageDTO(@RequestBody MessageQueryLikeDTO messageDTO) {
//        log.info("根据信息DTO获取信息: {}", messageDTO);

        Page<MessageVO> list = messageService.getByReceiveMessageDTO(messageDTO);

        return Result.success(list);
    }


    /**
     * 根据id更新信息状态
     * @param messageChangeDTO
     * @return
     */
    @Operation(summary = "根据id更新信息状态")
    @PutMapping("/updateStatusById")
    public Result<String> updateStatusById(@RequestBody MessageChangeDTO messageChangeDTO) {
//        log.info("根据id更新信息状态: {}", messageChangeDTO);

        messageService.updateStatusById(messageChangeDTO);

        return Result.success("更新成功");
    }

    /**
     * 发送信息
     * @param messageDTO
     * @return
     */
    @Operation(summary = "发送信息")
    @PostMapping("/send")
    public Result<String> send(@RequestBody MessageDTO messageDTO) {
//        log.info("发送信息: {}", messageDTO);

        messageService.saveMessage(messageDTO);

        return Result.success("发送成功");
    }
}
