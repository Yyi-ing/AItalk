package com.yyxxlu.aitalk.po;

import lombok.Data;

@Data
public class RoleProfile {
    private String id;
    private String name;
    private String background;      // 角色背景故事
    private String personality;     // 性格特征
    private String speakingStyle;   // 说话风格
    private String knowledgeScope;  // 知识范围
    private String voiceExample;    // 对话示例
    private boolean enableRAG;      // 是否启用RAG
    private String ragCollection;   // RAG知识库名称
}

