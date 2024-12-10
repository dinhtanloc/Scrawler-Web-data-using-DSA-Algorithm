package ueh.config;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.List;
@Configuration
public class EmbeddingModelConfig {

    private String apiKey;

    private String model;

    @Bean
    public EmbeddingModel embeddingModel() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API key is not set in the environment variables.");
        }

        Dotenv dotenv = Dotenv.load();
        apiKey = dotenv.get("OPENAI_API_KEY", apiKey); 

        OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions.builder()
            .withModel(model)
            .build();

        OpenAiApi openAiApi = new OpenAiApi(apiKey);
        return new OpenAiEmbeddingModel(openAiApi,MetadataMode.EMBED, options);
    }

    public void computeSimilarity() {
        EmbeddingResponse embeddingResponse = this.embeddingModel()
            .embedForResponse(List.of("Hello World", "World is big and salvation is near"));
        
        System.out.println(embeddingResponse);
    }
}
