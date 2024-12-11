package ueh.repository;

import ueh.model.Chunk;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class VectorRepository {
    // private final VectorStore vectorStore;

    @Autowired
    private MongoDBAtlasVectorStore vectorStore;

    // public VectorRepository(MongoDBAtlasVectorStore vectorStore) {
    //     this.vectorStore = vectorStore;
    // }


    public void saveChunk(Chunk chunk) {
        // Chunk savedChunk = chunkRepository.save(chunk);

        Map<String, Object> metadata = chunk.getMetadata();

        Document document = new Document(chunk.getText());
        // System.out.println("Document: " + document.getContent());
        vectorStore.add(List.of(document)); 

    }

    // public List<Document> findSimilarDocuments(String searchText) {

    //     return vectorStore
    //       .similaritySearch(SearchRequest
    //         .query(searchText)
    //         .withSimilarityThreshold(0.87)
    //         .withTopK(10))
    //       .stream()
    //       .map(chunk -> new Document(chunk.getText()))
    //       .collect(Collectors.toList());
    // }
    public List<Document> findSimilarDocuments(String searchText) {
        System.out.println("=== Starting search ===");
        System.out.println("Search query: " + searchText);
    
        // Gửi yêu cầu similarity search
        List<Document> searchResults = vectorStore
            .similaritySearch(SearchRequest
                .query(searchText)
                .withSimilarityThreshold(0.3)
                .withTopK(10));
        
        // Log kết quả từ similarity search
        System.out.println("Search results (raw chunks): " + searchResults);
    
        // Chuyển đổi Chunk sang Document
        List<Document> documents = searchResults.stream()
            .map(chunk -> {
                System.out.println("Chunk text: " + chunk.getText());
                return new Document(chunk.getText());
            })
            .collect(Collectors.toList());
    
        // Log kết quả cuối cùng
        System.out.println("Mapped documents: " + documents);
    
        System.out.println("=== Search completed ===");


        return documents;
    }
    
}