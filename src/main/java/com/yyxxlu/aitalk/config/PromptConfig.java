package com.yyxxlu.aitalk.config;

import com.yyxxlu.aitalk.entity.po.RoleProfile;
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
                4. 保持对话连贯性，记住之前的对话内容
                5. **回复中不得包含任何括号内的动作描写、心理描写或旁白说明**
                        
                # 回复要求
                1. 请以{roleName}的身份和语气回复
                2. **所有情感、动作和状态都必须通过对话内容本身来表达，不得使用括号说明**
                3. **角色的情绪、动作和反应应通过语气词、语速变化、重复用词等对话技巧来体现**
                4. 回复只能是纯对话文本
                        
                回复内容如下：
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
