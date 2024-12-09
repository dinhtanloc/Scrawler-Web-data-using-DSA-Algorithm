import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ueh.util.vectorStore;
@Configuration
public class VectorStoreConfig {

    @Bean
    public VectorStore vectorStore() {
        return VectorStore("mongodb://localhost:27017/DSAFinal", "DSA", "chunks");
    }
}
