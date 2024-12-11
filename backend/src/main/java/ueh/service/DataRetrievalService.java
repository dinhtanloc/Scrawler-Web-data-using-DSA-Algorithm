package ueh.service;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import ueh.service.ChunkService;

@Service
public class DataRetrievalService {
    @Autowired
    private MongoDBAtlasVectorStore vectorStore;

    @Autowired
    private ChunkService chunkService;

    public List<Document> searchData(String query) {
        System.out.println("ABC"+query);
        return chunkService.findSimilarDocuments(query);
    }
}