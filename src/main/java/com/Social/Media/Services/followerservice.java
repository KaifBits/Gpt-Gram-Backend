package com.Social.Media.Services;

import com.Social.Media.Entity.UserRegister;
import com.Social.Media.Repository.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class followerservice {
    @Autowired
    private Userrepo userRegisterRepository;
    public boolean addFollower(String username, String followerUsername) {
        Optional<UserRegister> userOptional = userRegisterRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            UserRegister user = userOptional.get();

            // Check if the follower is already in the list
            if (!user.getFollowers().contains(followerUsername)) {
                user.getFollowers().add(followerUsername);
                userRegisterRepository.save(user); // Save updated user info to MongoDB
                return true;
            } else {
                // Follower already exists in the list
                return false;
            }
        } else {
            // User not found
            return false;
        }
    }
}
