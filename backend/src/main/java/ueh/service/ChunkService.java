package ueh.service;

import ueh.repository.VectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import ueh.model.Chunk;
import java.util.List;
import java.util.Map;

@Service
public class ChunkService {

    @Autowired
    private VectorRepository vectorRepository;

    // @Autowired
    // private EmbeddingModel embeddingModel;

    @Autowired
    private MongoDBAtlasVectorStore vectorStore;

    


    // public Chunk saveChunk(Chunk chunk) {
    //     // Chunk savedChunk = chunkRepository.save(chunk);

    //     Map<String, Object> metadata = chunk.getMetadata();

    //     Document document = new Document(chunk.getText());
    //     // System.out.println("Document: " + document.getContent());
    //     vectorStore.add(List.of(document)); 

    //     return chunk;
    // }


    public void embededHTML(String htmlContent) {
        Document document = new Document(htmlContent);

        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(List.of(document));

        for (Document chunk : chunks) {
            Chunk chunkEntity = new Chunk();
            chunkEntity.setText(chunk.getContent());
            // System.out.println("Chunk: " + chunkEntity.getText());
            // chunkEntity.setEmbedding(chunkEntity.getText(), embeddingModel);
            vectorRepository.saveChunk(chunkEntity);
        }
    }

    public List<Document> findSimilarDocuments(String searchText) {
        return vectorRepository.findSimilarDocuments(searchText);
    }


   
}
