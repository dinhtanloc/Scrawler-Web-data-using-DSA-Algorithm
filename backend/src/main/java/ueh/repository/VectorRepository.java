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
    @Autowired
    private MongoDBAtlasVectorStore vectorStore;


    public void saveChunk(Chunk chunk) {
        Map<String, Object> metadata = chunk.getMetadata();
        Document document = new Document(chunk.getText());
        vectorStore.add(List.of(document)); 

    }

    public List<Document> findSimilarDocuments(String searchText) {
        return vectorStore
          .similaritySearch(SearchRequest
            .query(searchText)
            .withSimilarityThreshold(0.87)
            .withTopK(10))
          .stream()
          .map(chunk -> new Document(chunk.getText()))
          .collect(Collectors.toList());
    }
  
    
}