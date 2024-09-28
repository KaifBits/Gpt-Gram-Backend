package com.Social.Media.Controller;

import com.Social.Media.Entity.Post;
import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Services.FeedService;
import com.Social.Media.Services.imgProfileService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class Feed {

    @Autowired
    private FeedService feedService;
    @Autowired
    private imgProfileService imgs;

    @Autowired
    private imgProfileService imgProfileService;

    @GetMapping("/feed")
    public ResponseEntity<List<Map<String, Object>>> feedpost(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit) {


        List<Post> posts = feedService.getPaginatedPosts(page, limit); // Updated method to support pagination
        List<Map<String, Object>> responseList = new ArrayList<>();

        try {
            for (Post post : posts) {
                System.out.println("hii");
                ObjectId imageId = post.getImage();
                String caption=post.getCaption();
                byte[] imageData = imgProfileService.getImage(imageId);


                if (imageData != null) {
                    Optional<UserRegister> userOpt = feedService.findUserByPostId(post.getId());
                    Map<String, Object> postWithUserDetails = new HashMap<>();

                    postWithUserDetails.put("post", post);
                    postWithUserDetails.put("id", post.getId().toHexString());
                    postWithUserDetails.put("imageData", imageData);
                    if(caption!=null) {
                        postWithUserDetails.put("caption", caption);
                    }

                    if (userOpt.isPresent()) {
                        ObjectId obj = userOpt.get().getProfile();
                        byte[] imgdata = imgs.getImage(obj);
                        postWithUserDetails.put("profile", imgdata);
                        postWithUserDetails.put("user", userOpt.get());
                    } else {
                        postWithUserDetails.put("user", "User not found");
                    }

                    responseList.add(postWithUserDetails);
                }
            }

            if (!responseList.isEmpty()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                return new ResponseEntity<>(responseList, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
