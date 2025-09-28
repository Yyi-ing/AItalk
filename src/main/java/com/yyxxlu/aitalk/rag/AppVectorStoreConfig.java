package com.yyxxlu.aitalk.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppVectorStoreConfig {

    @Resource
    private AppDocumentLoader appDocumentLoader;

//    @Bean
//    VectorStore appVectorStore(OpenAiApi dashscopeEmbeddingModel) {
//        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
//        //加载文档
//        List<Document> documents = appDocumentLoader.loadMarkdowns();
//        simpleVectorStore.add( documents);
//        return simpleVectorStore;
//    }
}
