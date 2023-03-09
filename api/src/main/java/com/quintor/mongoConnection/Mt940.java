package com.quintor.mongoConnection;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "files")
public class Mt940 {
    @Id
    private String id;

    private String title;

    private Binary file;

    public Mt940(String title) {
        this.title = title;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Binary getFile() {
        return this.file;
    }

    public void setFile(Binary file) {
        this.file = file;
    }
}
