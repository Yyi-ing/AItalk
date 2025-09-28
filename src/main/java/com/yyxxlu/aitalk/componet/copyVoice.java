package com.yyxxlu.aitalk.componet;

//import com.alibaba.dashscope.audio.tts.v2.VoiceEnrollmentService;
//import com.alibaba.dashscope.audio.tts.v2.SpeechSynthesizer;
import com.alibaba.dashscope.audio.ttsv2.enrollment.Voice;
import com.alibaba.dashscope.audio.ttsv2.enrollment.VoiceEnrollmentService;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import org.springframework.stereotype.Component;

@Component
public class copyVoice{

    private String apiKey;

    public String copy() {

        Constants.apiKey = apiKey;

        String url ="https://aitalk-voice.oss-cn-chengdu.aliyuncs.com/16497664779690.wav?Expires=1759063713&OSSAccessKeyId=TMP.3KmYGEA72LbM4BFjqzb79e5t6n5qC3tJiPa51mFX5nvvr5SRJj7yTN7GRMc5XoQdHpTyZd7Bym6F3sG4kuGvzzQn7o7hzQ&Signature=iMHRSiX6b1eDsnR5X47tNpiVweE%3D"; // 请按实际情况进行替换
        String prefix = "vyase"; // 可自定义音色前缀
        String targetModel = "cosyvoice-v2";

        // 创建语音注册服务实例
        VoiceEnrollmentService service = new VoiceEnrollmentService(apiKey );

        Voice voice = null;
        try {
            // 调用createVoice方法复刻声音，并生成voiceId
            voice = service.createVoice(targetModel, prefix, url);
            System.out.println("your voice id is " + voice.getVoiceId());
            // 输出 cosyvoice-prefix-xxxxx
        } catch (InputRequiredException e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        }

        return voice.getVoiceId();

    }

}
