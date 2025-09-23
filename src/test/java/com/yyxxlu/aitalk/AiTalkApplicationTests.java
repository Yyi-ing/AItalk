package com.yyxxlu.aitalk;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiTalkApplicationTests {

    @Autowired
    private ChatClient chatClient;
    @Test
    void contextLoads() {
        System.out.println(chatClient.prompt()
                .user("你好，你是谁")
                .call()
                .content());
    }

}
