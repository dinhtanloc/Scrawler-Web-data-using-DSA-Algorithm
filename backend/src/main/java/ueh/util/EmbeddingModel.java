@Bean
public EmbeddingModel embeddingModel() {
    // Can be any other EmbeddingModel implementation.
    return new OpenAiEmbeddingModel(new OpenAiApi(System.getenv("SPRING_AI_OPENAI_API_KEY")));
}