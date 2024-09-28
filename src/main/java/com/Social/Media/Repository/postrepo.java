package com.Social.Media.Repository;

import com.Social.Media.Entity.Post;
import org.bson.types.ObjectId;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface postrepo extends MongoRepository<Post, ObjectId> {

    List<Post> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
}
