package ueh.repository;

import ueh.model.Chunk;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChunkRepository extends MongoRepository<Chunk, String> {
}
