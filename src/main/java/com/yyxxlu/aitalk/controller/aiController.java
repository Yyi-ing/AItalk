package com.yyxxlu.aitalk.controller;

import com.yyxxlu.aitalk.componet.copyVoice;
import com.yyxxlu.aitalk.entity.vo.HistoryMessageVo;
import com.yyxxlu.aitalk.mapper.RoleProfileMapper;
import com.yyxxlu.aitalk.entity.po.RoleProfile;
import com.yyxxlu.aitalk.service.VoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.util.List;

@Slf4j
@RestController
public class aiController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VoiceService voiceService;

    @Autowired
    private RoleProfileMapper roleProfileMapper;

    @Autowired
    private ChatMemory chatMemory;



    @GetMapping("/chat")
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }


//    获取角色列表
    @GetMapping("/roleList")
    public List<RoleProfile> roleList() {
        return roleProfileMapper.selectByRoleList();
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


    //音色克隆接口
    @GetMapping(value = "/copyVoice")
    public String copyVoice(){


        return new copyVoice().copy();
    }


    //查询历史聊天记录
    @GetMapping("/history")
    public List<HistoryMessageVo> history(@RequestParam("id") String id) {
        List<Message> messages = chatMemory.get(id);
        log.info("ID:{}",id);
        log.info("历史消息:{}",messages);
        if (messages == null) {
            return List.of();
        }
        List<HistoryMessageVo> list = messages.stream().map(HistoryMessageVo::new).toList();
        System.out.println("list:"+list);
        return list;
    }





    @GetMapping(value = "/voice")
    public ResponseEntity<byte []> chatWithRole(@RequestParam("prompt") String prompt, @RequestParam("id") String id)  {
        ByteBuffer voice = voiceService.getAudioOptimize(prompt, id);
        
        // 检查voice是否为null
        if (voice == null) {
            // 返回空的音频或者错误信息
            return ResponseEntity.notFound().build();
        }

        byte[] bytes = new byte[voice.remaining()];
        voice.get(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"voice.mp3\"")
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(bytes);
    }





}
