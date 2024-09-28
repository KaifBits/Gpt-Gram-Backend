package com.Social.Media.Controller;

import com.Social.Media.Entity.Post;
import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Repository.Userrepo;
import com.Social.Media.Services.followerservice;
import com.Social.Media.Services.imgProfileService;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ImgProfileRetrive {

    @Autowired
    private imgProfileService imgs;
    @Autowired
    private followerservice fl;

    @Autowired
    private Userrepo r;

// fetching all upload img which is present in profile
@GetMapping("/get/{username}")
public ResponseEntity<List<Map<String, Object>>> getImagesByUsername(@PathVariable String username) {
    Optional<UserRegister> userOptional = r.findByUsername(username);

    if (userOptional.isPresent()) {
        UserRegister user = userOptional.get();
        List<Post> posts = user.getPosts();
        List<Map<String, Object>> imagesDataList = new ArrayList<>();

        try {
            for (Post post : posts) {
                ObjectId imageId = post.getImage();
                byte[] imageData = imgs.getImage(imageId);

                if (imageData != null) {
                    Map<String, Object> postImageMap = new HashMap<>();
                    postImageMap.put("postId", post.getId().toString());  // Add postId to the map
                    postImageMap.put("imageData", imageData);  // Add image data to the map
                    imagesDataList.add(postImageMap);  // Add the map to the list
                }
            }

            if (!imagesDataList.isEmpty()) {
                return ResponseEntity.ok(imagesDataList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
    @PostMapping("/{username}/follow")
    public ResponseEntity<String> followUser(@PathVariable String username, @RequestParam String followerUsername) {
        boolean isFollowed = fl.addFollower(username, followerUsername);

        if (isFollowed) {
            return ResponseEntity.ok("User followed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to follow user or already followed.");
        }
    }

    @GetMapping("/get/profile/{username}")
    public ResponseEntity<List<byte[]>> getprofileimg(@PathVariable String username){
        Optional<UserRegister> userOptional = r.findByUsername(username);
        try {
            if (userOptional.isPresent()) {
                UserRegister user = userOptional.get();
                ObjectId p = user.getProfile();
                byte[] imgdata = imgs.getImage(p);
                List<byte[]> imagesDataList = new ArrayList<>();
                imagesDataList.add(imgdata);
                if(imgdata.length !=0){

                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    return new ResponseEntity<>(imagesDataList,headers,HttpStatus.OK);

                }
                else {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }

            }
            else{
                return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            System.out.println(e);
        }
      return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
