package com.quintor.api.mongoConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private GridFsTemplate template;
    @Autowired
    private GridFsOperations operations;

//    private FileRepository fileRepository;

    public String addFile(MultipartFile file) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("filesize", file.getSize());

        Object fileId = template.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);
        return fileId.toString();
    }

//    public Mt940 getMt940(String id) {
//        return fileRepository.findById(id).get();
//    }

}
