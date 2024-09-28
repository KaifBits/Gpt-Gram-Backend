package com.Social.Media.Controller;

import com.Social.Media.Entity.Post;
import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Repository.Userrepo;
import com.Social.Media.Services.ImageServices;
import com.Social.Media.Services.Postservice;
import com.Social.Media.Services.UserEntry;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class Imageupload {
    @Autowired
    private ImageServices imgs;
    @Autowired
    private Userrepo r;

    @Autowired
    private Postservice ps;

    @Autowired
    private UserEntry ue;


// POST CREATE AND LINK WITH USER --------------------------------------------------------------------->
    @PostMapping("/upload/{username}")
    public ObjectId upload(@RequestParam("file") MultipartFile file,@RequestParam("caption") String caption,  @PathVariable String username){

        try{
            ObjectId fileif=null;

            Optional<UserRegister> u=r.findByUsername(username);
            if(u.isPresent()) {
            // upload image with creating post
                fileif=imgs.UploadImage(file);
            Post p =new Post();
            p.setImage(fileif);
            p.setCaption(caption);
                p.setCreatedAt(LocalDateTime.now());
            Post p1=ps.save(p);
            System.out.println(username);

           //save the post in user


               UserRegister u1 = u.get();
               u1.getPosts().add(p1);
               ue.enter(u1);
           }
           else{
               System.out.println("user not found");
           }


            return fileif;

        }catch (IOException e){
            return null;
        }
    }
    // Profile pic upload and link with user --------------------------------------------------------------------->
    @PostMapping("/upload/profile/{username}")
    public ResponseEntity<ObjectId> profileupload(@RequestParam("file") MultipartFile file, @PathVariable String username){

        try{
            ObjectId fileif=null;

            Optional<UserRegister> u=r.findByUsername(username);
            if(u.isPresent()) {
                // upload image with creating post
                fileif = imgs.UploadImage(file);
                UserRegister ur=u.get();
                ur.setProfile(fileif);
                ue.enter(ur);
                return new ResponseEntity("good",HttpStatus.OK);

            }
            else{
                return new ResponseEntity("user not found",HttpStatus.NOT_FOUND);
            }








        }catch (IOException e){
            return new ResponseEntity("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id) {
        try {
            ps.deletePostById(new ObjectId(id));
            return ResponseEntity.ok("Post deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }



}
