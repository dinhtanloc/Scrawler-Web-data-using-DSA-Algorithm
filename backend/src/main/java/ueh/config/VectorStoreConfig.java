package ueh.config;

import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore.MongoDBVectorStoreConfig;
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

    // @Value("${spring.ai.openai.api-key}")
    // private String openAiKey;
    // @Bean
    // public EmbeddingModel embeddingModel() {
    //     return new OpenAiEmbeddingModel(new OpenAiApi(openAiKey));
    // }
    // @Bean
    // public VectorStore mongodbVectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
    //     return new MongoDBAtlasVectorStore(mongoTemplate, embeddingModel,
    //             MongoDBAtlasVectorStore.MongoDBVectorStoreConfig.builder().build(), true);
    // }
}
