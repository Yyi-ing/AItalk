package com.yyxxlu.aitalk.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class aiConfig {


    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)

                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 添加advisor增强环绕通知：日志记录器
                        MessageChatMemoryAdvisor.builder(chatMemory).build() // 添加聊天记录记忆的环绕增强器
                )

                .build();
    }

    @Bean
    public ChatMemory chatMemory() {
        return  MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }

}
