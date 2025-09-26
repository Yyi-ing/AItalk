package com.yyxxlu.aitalk.service;

import java.nio.ByteBuffer;


public interface VoiceService {


    public ByteBuffer getAudio(String text);


    public ByteBuffer getAudioOptimize(String text);


    //测试接口使用，无其他作用
    public void test() ;
}
