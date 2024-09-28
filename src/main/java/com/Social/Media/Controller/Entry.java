package com.Social.Media.Controller;

import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Repository.Userrepo;
import com.Social.Media.Services.UserEntry;
import com.Social.Media.Services.imgProfileService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class Entry {
    @Autowired
    private imgProfileService imgs;
    @Autowired
    private UserRegister user;
    @Autowired
    private UserEntry ue;

    @Autowired
    private Userrepo rrr;

    //for Registration ----------------------------------------->
    @PostMapping("/register")
    public void enter(@RequestBody UserRegister ur) {
        ue.enter(ur);
    }

    //for login authetication ----------------------------------->
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRegister ur) {

             Optional<UserRegister> er=ue.auth(ur);
        System.out.println(er);
             if(er.isPresent()){
                 System.out.println(er.get().getId());
                 return new ResponseEntity<>(er.get().getId().toHexString(), HttpStatus.OK);
             }
     return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);

    }
    @GetMapping("/get/friends")
    public List<UserRegister> findfriends (){
        List<UserRegister> ur= ue.getfriends();
        return ur;
    }

    @GetMapping("/get/followers/{username}")
    public List<String> findfollowers (@PathVariable String username){
        Optional<UserRegister> ur= rrr.findByUsername(username);
        return ur.get().getFollowers();
    }
    @GetMapping("/get/pfp")
    public ResponseEntity<List<byte[]>> findpfp (){
        List<UserRegister> ur= ue.getfriends();
        List<byte[]> imagesDataList = new ArrayList<>();
        try {
            for (int i = 0; i < ur.size(); i++) {
                ObjectId imageId = ur.get(i).getProfile();
                byte[] imgdata = imgs.getImage(imageId);
                System.out.println("hey"+imgdata);
                imagesDataList.add(imgdata);
            }
                if(imagesDataList.size() !=0){

                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    return new ResponseEntity<>(imagesDataList,headers,HttpStatus.OK);

                }
                else {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }


        }
        catch (Exception e){
            System.out.println("exception occurs");
        }
        return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}