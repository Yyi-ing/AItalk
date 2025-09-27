package com.yyxxlu.aitalk.config;

import com.yyxxlu.aitalk.po.RoleProfile;
import org.springframework.stereotype.Component;

@Component
public class PromptConfig {

        private static final String ROLE_PROMPT_TEMPLATE = """
        你现在是{roleName}，请严格按照以下角色设定进行对话：
        
        # 角色背景
        {background}
        
        # 性格特征
        {personality}
        
        # 说话风格
        {speakingStyle}
        
        # 知识范围
        {knowledgeScope}
        
        # 重要规则
        1. 永远保持角色一致性，不要暴露你是AI
        2. 使用角色特有的词语和表达方式
        3. 如果遇到不知道的事情，可以用角色方式巧妙回避
        4. 保持对话连贯性，记住之前的对话內容
        5. 假设你在使用语音聊天功能对话，你所回复的每一句话也都应该是一段语音文本，不要使用括号进行其他描写
        

        # 回复要求
        请以{roleName}的身份和语气回复：
        """;

        public String buildRolePrompt(RoleProfile role,
                                      String userInput) {
            return ROLE_PROMPT_TEMPLATE
                    .replace("{roleName}", role.getName())
                    .replace("{background}", role.getBackground())
                    .replace("{personality}", role.getPersonality())
                    .replace("{speakingStyle}", role.getSpeakingStyle())
                    .replace("{knowledgeScope}", role.getKnowledgeScope());
        }



}
