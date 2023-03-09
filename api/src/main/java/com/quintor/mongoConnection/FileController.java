package com.quintor.mongoConnection;

import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping("mt940")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/add")
    public String addFile(@RequestParam("title") String title, @RequestParam("file")MultipartFile file, Model model) throws IOException {
        String id = fileService.addFile(title, file);
        System.out.println("Ik ben bij /add");
        return "Er is iets gelukt";
    }

    @GetMapping("/get/{id}")
    public String getFile(@PathVariable String id, Model model) {
        Mt940 mt940 = fileService.getMt940(id);
        return "files";
    }

}
