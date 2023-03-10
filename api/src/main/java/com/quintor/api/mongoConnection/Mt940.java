package com.quintor.api.mongoConnection;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.time.LocalDate;
//@Document("files")
public class Mt940 {
//    @Id
    private String id;
    private MultipartFile file;
    private LocalDate uploadDate;

    public Mt940(MultipartFile file, LocalDate uploadDate) {
        this.file = file;
        this.uploadDate = uploadDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MultipartFile getFile() {
        return this.file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public LocalDate getUploadDate() {
        return this.uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }
}
