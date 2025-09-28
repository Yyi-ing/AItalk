package com.yyxxlu.aitalk.entity.vo;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;

@Data
public class HistoryMessageVo {
    private String role; // "user" 或 "assistant"
    private String content; // 消息内容

    public HistoryMessageVo(Message message) {
        switch (message.getMessageType()){

            case USER -> this.role = "user";
            case ASSISTANT -> this.role = "assistant";
            default -> this.role = "";
        }

        this.content = message.getText();
    }

}
