package com.quintor.api.mt940;

import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

//declares the collection of the database
@Document(collection = "file")
public class Json {
    @Id
    private String id;
    private JSONObject file;
    private int uploadUser;

    public Json(JSONObject file, int uploadUser) {
        this.file = file;
        this.uploadUser = uploadUser;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject getFile() {
        return this.file;
    }

    public void setFile(JSONObject file) {
        this.file = file;
    }

    public int getUploadUser() {
        return this.uploadUser;
    }

    public void setUploadUser(int uploadUser) {
        this.uploadUser = uploadUser;
    }
}
