package com.Social.Media.Services;

import com.Social.Media.Entity.Post;
import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Repository.Userrepo;
import com.Social.Media.Repository.postrepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedService {
    @Autowired
    private Userrepo ur;
    @Autowired
    private postrepo p;
    public List<Post> feedpost(){


        List<Post> po=p.findAll();
        if(po.isEmpty()){
            return null;
        }
        else {
            return po;
        }
    }
    public Optional<UserRegister> findUserByPostId(ObjectId postId) {
        try {
            System.out.println("Looking for User with Post ID: " + postId);
            Optional<UserRegister> user = ur.findUserByPostId(postId);
            System.out.println("Found User: " + user);
            return user;
        } catch (Exception e) {
            System.err.println("Error while finding user by post ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public List<Post> getPaginatedPosts(int page, int limit) {
        // Create a PageRequest object to handle pagination and sorting
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Fetch the posts using the repository method
        return p.findAllByOrderByCreatedAtDesc(pageRequest);
    }




}
