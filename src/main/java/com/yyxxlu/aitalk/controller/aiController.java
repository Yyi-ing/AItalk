package com.yyxxlu.aitalk.controller;

import com.yyxxlu.aitalk.service.VoiceService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;

@RestController
public class aiController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VoiceService voiceService;

    @GetMapping("/chat")
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }



    //没有角色功能的接口
//    @GetMapping(value = "/voice")
    public ResponseEntity<byte []> chatWithVoice(@RequestParam("prompt") String prompt)  {
        ByteBuffer voice = voiceService.getAudio(chatClient.prompt()
                .user(prompt)
                .call()
                .content());

        byte[] bytes = new byte[voice.remaining()];
        voice.get(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"voice.mp3\"")
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(bytes);
    }

    @GetMapping(value = "/voice")
    public ResponseEntity<byte []> chatWithRole(@RequestParam("prompt") String prompt)  {
        ByteBuffer voice = voiceService.getAudioOptimize(prompt);

        byte[] bytes = new byte[voice.remaining()];
        voice.get(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"voice.mp3\"")
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(bytes);
    }





}
