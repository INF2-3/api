package com.quintor.mongoConnection;

import jdk.incubator.vector.VectorOperators;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public String addFile(String title, MultipartFile file) throws IOException {
        Mt940 mt940 = new Mt940(title);
        mt940.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        mt940 = fileRepository.insert((mt940));
        return mt940.getId();
    }

    public Mt940 getMt940(String id) {
        return fileRepository.findById(id).get();
    }

}
