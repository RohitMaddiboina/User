package com.ecommerce.user.service;


import com.ecommerce.user.dao.UserDao;
import com.ecommerce.user.model.MyUserDetails;
import com.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=getUserByEmail(username);
        return new MyUserDetails(user);

    }



    public User getUserByEmail(String email) {
        User user = userDao.getUserByEmail(email);
        if (user == null) {

            return null;
        } else {
            return user;
        }

    }



}
