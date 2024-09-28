package com.Social.Media.Services;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ImageServices {

    private final GridFsTemplate gt;

    @Autowired
   // handle for image store -------------------------------->
    public ImageServices(GridFsTemplate gt){
        this.gt=gt;
    }
    public ObjectId UploadImage(MultipartFile f)throws IOException {
        ObjectId fileId;
        try (InputStream getfile = f.getInputStream()) {

            Document metadata = new Document("type", f.getContentType());
            fileId = gt.store(getfile, f.getOriginalFilename(), f.getContentType(), metadata);
        }


        return fileId;
    }





}
