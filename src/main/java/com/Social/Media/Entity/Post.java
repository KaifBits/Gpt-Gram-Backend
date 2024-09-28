package com.Social.Media.Entity;

import com.Social.Media.Controller.Entry;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
@Document(collection = "Post")
public class Post {

    @Id
    private ObjectId id;


    private ObjectId image;
    private String caption="demo caption";
    @CreatedDate
    private LocalDateTime createdAt;

    private List<ObjectId> Likes=new ArrayList<>();

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<ObjectId> getLikes() {
        return Likes;
    }

    public void setLikes(List<ObjectId> likes) {
        Likes = likes;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getImage() {
        return image;
    }

    public void setImage(ObjectId image) {
        this.image = image;
    }


}
