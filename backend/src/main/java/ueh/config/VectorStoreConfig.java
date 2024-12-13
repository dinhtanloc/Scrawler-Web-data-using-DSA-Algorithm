package ueh.config;

import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore.MongoDBVectorStoreConfig;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Configuration
public class VectorStoreConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiKey;
    @Bean
    public EmbeddingModel embeddingModel() {
        return new OpenAiEmbeddingModel(new OpenAiApi(openAiKey));
    }

    @Bean
    public MongoDBAtlasVectorStore vectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
        MongoDBVectorStoreConfig config = MongoDBVectorStoreConfig.builder()
            .withCollectionName("chunks") 
            .withVectorIndexName("vector_index") 
            .withPathName("embedding") 
            .build();

        return new MongoDBAtlasVectorStore(mongoTemplate, embeddingModel, config, true);
    }

    
}
