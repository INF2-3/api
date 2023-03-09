package com.quintor.api.mongoConnection;

import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {
    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/add")
    public String addFile(@RequestParam("file")MultipartFile file) throws IOException {
        String id = fileService.addFile("test", file);
        return "test";
    }

    @GetMapping("/get/{id}")
    public String getFile(@PathVariable String id, Model model) {
        Mt940 mt940 = fileService.getMt940(id);
        return "files";
    }

}
