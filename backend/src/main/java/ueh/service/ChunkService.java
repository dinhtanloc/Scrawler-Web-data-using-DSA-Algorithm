package com.example.springbootmongodb.service;

import com.example.springbootmongodb.repository.ChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import ueh.model.Chunk;
import java.util.List;

@Service
public class ChunkService {

    @Autowired
    private ChunkRepository chunkRepository;

    /**
     * Lưu một chunk đơn lẻ vào MongoDB.
     * @param chunk Chunk cần lưu.
     * @return Chunk đã được lưu.
     */
    public Chunk saveChunk(Chunk chunk) {
        return chunkRepository.save(chunk);
    }

    /**
     * Xử lý nội dung HTML, tách thành các chunk và lưu chúng vào MongoDB.
     * @param htmlContent Nội dung HTML cần xử lý.
     */
    public void embededHTML(String htmlContent) {
        // Bước 1: Tạo Document từ nội dung HTML
        Document document = new Document(htmlContent);

        // Bước 2: Tách Document thành các chunk
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(List.of(document));

        // Bước 3: Lưu các chunk xuống MongoDB
        for (Document chunk : chunks) {
            Chunk chunkEntity = new Chunk();
            chunkEntity.setText(chunk.getContent()); // Lấy nội dung từ chunk
            chunkEntity.setEmbedding(chunk.getEmbedding()); // Nếu đã tính embedding
            saveChunk(chunkEntity);
        }
    }
}
