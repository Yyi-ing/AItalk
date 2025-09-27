package com.yyxxlu.aitalk.config;

import com.yyxxlu.aitalk.mapper.AiMessageMapper;
import com.yyxxlu.aitalk.po.AiMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


//自定义实现聊天记录保存 暂时先使用默认的
@Component
@Slf4j
@RequiredArgsConstructor
public class MysqlChatMemory implements ChatMemory {

//    private final AiMessageMapper aiMessageMapper;

    /**
     新增对话记忆，不通过此处管理，置空
     */
    @Override
    public void add(String conversationId, List<Message> messages) {

    }

    @Override
    public List<Message> get(String conversationId) {
        // conversationId对应的问答对数据列表，经过处理作为记忆上下文
//        List<AiMessage> pairs = aiMessageMapper.selectBySessionId(Long.valueOf(conversationId));
//        // 对应封装的消息列表
//        List<Message> messages = new ArrayList<>();
//
//        for (AiMessage pair : pairs) {
//            // 只处理状态正常（完成）的一对消息
//            if (pair.getStatus() != null &&
//                    pair.getStatus() == AiMessageStatusEnum.FINISHED.getCode()) {
//                if (StrUtil.isNotBlank(pair.getUserContent())) {
//                    messages.add(new UserMessage(pair.getUserContent()));
//                }
//                if (StrUtil.isNotBlank(pair.getAiContent())) {
//                    messages.add(new AssistantMessage(pair.getAiContent()));
//                }
//            }
//        }
//
//        return messages;
        return null;
    }

    /**
     清除对话记忆，不通过此处管理，置空
     */
    @Override
    public void clear(String conversationId) {

    }
}

