package com.example.springbootmongodb.controller;

import com.example.springbootmongodb.model.Chunk;
import com.example.springbootmongodb.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chunks")
public class TextController {

    @Autowired
    private ChunkService chunkService;

    @PostMapping
    public Chunk saveChunk(@RequestBody Chunk chunk) {
        return chunkService.saveChunk(chunk);
    }

}
