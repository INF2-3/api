package com.quintor.api.mt940;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

//declares the collection of the database
@Document(collection = "file")
public class Mt940 {
    @Id
    private String id;
    private String file;
    private int uploadUser;

    public Mt940(String file, int uploadUser) {
        this.file = file;
        this.uploadUser = uploadUser;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getUploadUser() {
        return this.uploadUser;
    }

    public void setUploadUser(int uploadUser) {
        this.uploadUser = uploadUser;
    }
}
