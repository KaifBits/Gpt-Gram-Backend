package com.Social.Media.Services;

import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Repository.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserEntry {
    @Autowired
    private Userrepo r;
    //handle for registration -------------------------->
    public void enter(UserRegister u){
        r.save(u);

    }

    //handel for authetication ---------------------------->
    public Optional<UserRegister> auth(UserRegister ur){

        String name=ur.getUsername();
        String pass=ur.getPassword();

        Optional<UserRegister> obj=r.findByUsername(name);

        if(obj.isPresent()){

            if(pass.equals(obj.get().getPassword())){

                return obj;
            }
        }

       return Optional.empty();
    }

    public List<UserRegister> getfriends(){

       List<UserRegister> ur= r.findAll();
       return ur;
    }
}
