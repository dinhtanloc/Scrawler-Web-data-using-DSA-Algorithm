package com.example.springbootmongodb.service;

import com.example.springbootmongodb.model.Chunk;
import com.example.springbootmongodb.repository.ChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChunkService {

    @Autowired
    private ChunkRepository chunkRepository;

    public Chunk saveChunk(Chunk chunk) {
        return chunkRepository.save(chunk);
    }

    // Các phương thức khác nếu cần
}
