package com.example.springbootmongodb.repository;

import com.example.springbootmongodb.model.Chunk;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChunkRepository extends MongoRepository<Chunk, String> {
}
