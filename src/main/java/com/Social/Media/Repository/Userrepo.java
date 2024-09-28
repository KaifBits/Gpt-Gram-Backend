package com.Social.Media.Repository;

import com.Social.Media.Entity.UserRegister;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Userrepo extends MongoRepository<UserRegister, ObjectId> {
    Optional<UserRegister> findByUsername(String  username);
    @Query("{ 'posts._id': ?0 }")
    Optional<UserRegister> findUserByPostId(ObjectId postId);
}
