package com.ecommerce.user.dao;

import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserDao {

    @Autowired
    UserRepo userRepo;


    public User saveUser(User user) {


            return userRepo.save(user);

    }


    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);

    }




}