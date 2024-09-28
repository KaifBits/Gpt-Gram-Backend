package com.Social.Media.Controller;

import com.Social.Media.Entity.Post;
import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Repository.Userrepo;
import com.Social.Media.Repository.postrepo;
import com.Social.Media.Services.Postservice;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SetLike {
    @Autowired
    private Userrepo ur;

    @Autowired
    private Postservice ps;

    @Autowired
    private postrepo pr;

    @PostMapping("/setlike/{id}/{username}")
    public void savelike(@PathVariable String id, @PathVariable String username){
        try {
            Optional<UserRegister> userid = ur.findByUsername(username);
            if (userid.isEmpty()) {
                System.out.println("user not found");
            }
            else{
                System.out.println("user found");
                System.out.println(userid.get());
                ObjectId obj=new ObjectId(id);
                System.out.println(obj);
                ps.savelike(id, userid.get().getId());
            }



        }
        catch (Exception e){
            System.out.println(e);
        }


    }
    @GetMapping("/getlike/{id}/{username}")
    public String getlike(@PathVariable String username,@PathVariable String id){
        try {
            ObjectId obj = new ObjectId(id);
            Optional<UserRegister> userid = ur.findByUsername(username);
            ObjectId Id = userid.get().getId();
            Optional<Post> p = pr.findById(obj);
            List<ObjectId> listlikes = p.get().getLikes();
            for (int i = 0; i < listlikes.size(); i++) {
                System.out.println("arr" + listlikes.get(i));
                System.out.println("obj" + Id);
                if (listlikes.get(i).equals( Id)) {
                    return username;
                }

            }
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
       return null;


    }
    @DeleteMapping("/deletelike/{id}/{username}")
    public String deletelike(@PathVariable String username,@PathVariable String id){
        try {
            ObjectId obj = new ObjectId(id);
            Optional<UserRegister> userid = ur.findByUsername(username);
            ObjectId Id = userid.get().getId();
            Optional<Post> p = pr.findById(obj);
            List<ObjectId> listlikes = p.get().getLikes();
            for (int i = 0; i < listlikes.size(); i++) {
                System.out.println("arr" + listlikes.get(i));
                System.out.println("obj" + Id);
                if (listlikes.get(i).equals( Id)) {
                    listlikes.remove(i);
                    p.get().setLikes(listlikes);
                    ps.save(p.get());
                    return username;
                }

            }
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
        return null;


    }

}
