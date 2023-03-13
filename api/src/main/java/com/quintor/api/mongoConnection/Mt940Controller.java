package com.quintor.api.mongoConnection;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("api/mt940")
public class Mt940Controller {
    private final Mt940Service mt940Service;

    public Mt940Controller(Mt940Service mt940Service) {
        this.mt940Service = mt940Service;
    }

    public Mt940Service getMt940Service() {
        return this.mt940Service;
    }

    @PostMapping("/insert")
    public String insertMt940(@RequestParam("file") MultipartFile file, @RequestParam("userId") int userId) throws IOException {
        String document = new String(file.getBytes(), StandardCharsets.UTF_8);
        mt940Service.insertMt940(new Mt940(document, userId));
        return document;
    }
}
