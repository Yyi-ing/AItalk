package com.yyxxlu.aitalk.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.ByteBuffer;


public interface VoiceService {


    public ByteBuffer getVoice(String text);

    public void test() ;
}
