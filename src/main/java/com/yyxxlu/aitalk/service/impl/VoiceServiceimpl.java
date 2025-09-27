package com.yyxxlu.aitalk.service.impl;

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import com.yyxxlu.aitalk.config.PromptConfig;
import com.yyxxlu.aitalk.mapper.RoleProfileMapper;
import com.yyxxlu.aitalk.po.RoleProfile;
import com.yyxxlu.aitalk.service.VoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


@Slf4j
@Service
public class VoiceServiceimpl implements VoiceService {

   @Value("${ai.apikey}")
    private String apiKey;


   @Autowired
    private ChatClient chatClient;

   @Autowired
    private PromptConfig promptConfig;

   @Autowired
   private RoleProfileMapper roleProfileMapper;



   //文本转语音
    @Override
    public ByteBuffer getAudio(String text) {
        // 模型
         String model = "cosyvoice-v1";
        // 音色
          String voice = "longcheng";

            // 请求参数
            SpeechSynthesisParam param =
                    SpeechSynthesisParam.builder()
                            // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将your-api-key替换为自己的API Key
                             .apiKey(apiKey)
                            .model(model) // 模型
                            .voice(voice) // 音色
                            .build();

            // 同步模式：禁用回调（第二个参数为null）
            SpeechSynthesizer synthesizer = new SpeechSynthesizer(param, null);
            ByteBuffer audio = null;
            try {
                // 阻塞直至音频返回
                audio = synthesizer.call(text);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                // 任务结束关闭websocket连接
                synthesizer.getDuplexApi().close(1000, "bye");
            }

            return audio;

    }



    @Override
    public ByteBuffer getAudioOptimize(String text) {
        //构建角色
        RoleProfile roleProfile = roleProfileMapper.selectByPrimaryKey("1");

        log.info("角色信息:{}",roleProfile);

        //构建系统prompt
        String systemPrompt = promptConfig.buildRolePrompt(roleProfile, text);
        String content = chatClient.prompt()
                .system(systemPrompt)
                .user(text)
                .call()
                .content();

        ByteBuffer audio = getAudio(content);


        return audio;
    }







    //测试接口使用
    public void test() {

        // 模型
        String model = "cosyvoice-v1";
        // 音色，根据模型进行替换
        String voice = "longxiaochun_v2";


            // 请求参数
            SpeechSynthesisParam param =
                    SpeechSynthesisParam.builder()
                            // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将your-api-key替换为自己的API Key
                             .apiKey(apiKey)
                            .model(model) // 模型
                            .voice(voice) // 音色
                            .build();

            // 同步模式：禁用回调（第二个参数为null）
            SpeechSynthesizer synthesizer = new SpeechSynthesizer(param, null);
            ByteBuffer audio = null;
            try {
                // 阻塞直至音频返回
                audio = synthesizer.call("今天天气怎么样？");
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                // 任务结束关闭websocket连接
                synthesizer.getDuplexApi().close(1000, "bye");
            }
            if (audio != null) {
                // 将音频数据保存到本地文件“output.mp3”中
                File file = new File("output.mp3");
                // 首次发送文本时需建立 WebSocket 连接，因此首包延迟会包含连接建立的耗时
                System.out.println(
                        "[Metric] requestId为："
                                + synthesizer.getLastRequestId()
                                + "首包延迟（毫秒）为："
                                + synthesizer.getAudioData());
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(audio.array());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

    }

}
