package ueh.config;

import org.springframework.ai.vectorstore.mongodb.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.mongodb.MongoDBVectorStoreConfig;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Configuration
public class VectorStoreConfig {

    @Bean
    public MongoDBAtlasVectorStore vectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
        // Cấu hình Vector Store
        MongoDBVectorStoreConfig config = MongoDBVectorStoreConfig.builder()
            .withCollectionName("vector_store") 
            .withVectorIndexName("vector_index") 
            .withPathName("vector") 
            .build();

        return new MongoDBAtlasVectorStore(mongoTemplate, embeddingModel, config, true);
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new org.springframework.ai.embedding.openai.OpenAiEmbeddingModel(
            new org.springframework.ai.openai.OpenAiApi(System.getenv("SPRING_AI_OPENAI_API_KEY"))
        );
    }
}
