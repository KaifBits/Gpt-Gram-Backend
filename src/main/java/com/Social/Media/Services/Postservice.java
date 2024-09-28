package com.Social.Media.Services;

import com.Social.Media.Entity.Post;
import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Repository.Userrepo;
import com.Social.Media.Repository.postrepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class Postservice {

    @Autowired
    private postrepo pr;

    @Autowired
    private Userrepo ur;

    public Post save(Post p){
        return pr.save(p);
    }

    public Post savelike(String idStr, ObjectId userId) {
        if (!isValidObjectId(idStr)) {
            throw new IllegalArgumentException("Invalid ObjectId format: " + idStr);
        }

        ObjectId postId = new ObjectId(idStr);
        System.out.println(postId.getClass().getName());
        Optional<Post> optionalPost = pr.findById(postId);

        if (optionalPost.isEmpty()) {
            System.out.println("Post with ID " + postId + " not found");
            throw new NoSuchElementException("Post with ID " + postId + " not found");
        }

        Post post = optionalPost.get();
        List<ObjectId> likes = post.getLikes();
        if (likes == null) {
            likes = new ArrayList<>();
            post.setLikes(likes);
        }

        if (!likes.contains(userId)) {
            likes.add(userId);
        }

        return pr.save(post);
    }
    public void deletePostById(ObjectId postId) {
        if (pr.existsById(postId)) {
            // Delete the post from the Post collection
            pr.deleteById(postId);

            // Find the user that owns the post
            Optional<UserRegister> userOptional = ur.findUserByPostId(postId);
            if (userOptional.isPresent()) {
                UserRegister user = userOptional.get();

                // Remove the post from the user's posts list
                user.getPosts().removeIf(post -> post.getId().equals(postId));

                // Save the updated user entity
                ur.save(user);
            }
        } else {
            throw new RuntimeException("Post not found with id: " + postId);
        }
    }

    private boolean isValidObjectId(String id) {
        return id != null && id.length() == 24 && id.matches("[a-fA-F0-9]+");
    }



}
