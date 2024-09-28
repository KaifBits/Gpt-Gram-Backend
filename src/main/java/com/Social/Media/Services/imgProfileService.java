package com.Social.Media.Services;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class imgProfileService {

    private final GridFsTemplate gridFsTemplate;
    private final GridFSBucket gridFSBucket;

    @Autowired
    public imgProfileService(GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket) {
        this.gridFsTemplate = gridFsTemplate;
        this.gridFSBucket = gridFSBucket;
    }

    public byte[] getImage(ObjectId id) throws IOException {
        try {
            // Retrieve the GridFS file using its ObjectId
            GridFSFile gridFSFile = gridFSBucket.find(new Document("_id", id)).first();
            if (gridFSFile == null) {
                return loadDefaultImage();
            }
            else if(id==null){
                return loadDefaultImage();
            }


            // Read the file from GridFS
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            gridFSBucket.downloadToStream(id, outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            return loadDefaultImage();
        }
    }

    private byte[] loadDefaultImage() {
        String defaultImageUrl = "https://i.pinimg.com/280x280_RS/e1/08/21/e10821c74b533d465ba888ea66daa30f.jpg";
        try (InputStream inputStream = new URL(defaultImageUrl).openStream()) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0]; // Return empty array if default image cannot be loaded
        }
    }
}
