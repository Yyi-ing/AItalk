package com.yyxxlu.aitalk;

import com.yyxxlu.aitalk.componet.copyVoice;
import com.yyxxlu.aitalk.service.VoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class AiTalkApplicationTests {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private VoiceService voiceService;

    @Test
    void contextLoads() {
        System.out.println(chatClient.prompt()
                .user("你好，你是谁？")
                .call()
                .content());
    }

    @Test
    void testVoice(){
        voiceService.test();

    }

    @Test
    void testV() throws IOException {
        // 保存为MP3文件进行验证
        ByteBuffer buffer = voiceService.getAudio("今天天气怎么样？");
        byte[] audioData = new byte[buffer.remaining()];
        buffer.get(audioData);

// 保存到文件
        Files.write(Paths.get("test.mp3"), audioData);



    }


    @Test
    void testRoleAudio() throws IOException {
        // 保存为MP3文件进行验证
        ByteBuffer buffer = voiceService.getAudioOptimize("你好","2");
        byte[] audioData = new byte[buffer.remaining()];
        buffer.get(audioData);

// 保存到文件
        Files.write(Paths.get("test.mp3"), audioData);

    }



    //音色复制测试
    @Test
    void testCopyVoice() throws IOException {
        // 获取音色ID
        String voiceId = new copyVoice().copy();
        System.out.println("--------------------------------------");
        System.out.println(voiceId);
    }

}
