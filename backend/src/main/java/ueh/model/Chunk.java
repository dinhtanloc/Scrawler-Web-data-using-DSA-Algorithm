package com.example.springbootmongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chunks")
public class Chunk {

    @Id
    private String id;  // ID duy nhất của chunk
    private String text;  // Nội dung văn bản của chunk
    private double[] embedding;  // Vector embedding của văn bản

}
