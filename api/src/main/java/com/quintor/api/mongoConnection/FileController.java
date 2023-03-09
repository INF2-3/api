package com.quintor.api.mongoConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/add")
    public String test(@RequestParam("file")MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String test1 = file.getName();
        String test2 = file.getContentType();
        String test3 = file.toString();
        long size = file.getSize();
        String test = file.getInputStream().toString();
        String test5 = Arrays.toString(file.getBytes());

        return test5;
//                "filename: " + fileName + " name: " + test1 + " content type: " + test2 + " bestand to string:  "+ test3;
    }

//    public ResponseEntity<?> addFile(@RequestParam("file")MultipartFile file) throws IOException {
//        return new ResponseEntity<>(fileService.addFile(file), HttpStatus.OK);
//    }

//    @GetMapping("/get/{id}")
//    public String getFile(@PathVariable String id, Model model) {
//        Mt940 mt940 = fileService.getMt940(id);
//        return "files";
//    }

}
