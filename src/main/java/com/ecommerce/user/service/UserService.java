package com.ecommerce.user.service;

import com.ecommerce.user.dao.UserDao;
import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.PasswordEntity;
import com.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;


    public User saveUser(User user) {

            System.out.println(getUserByEmail(user.getEmail()));
        if (getUserByEmail(user.getEmail()) == null) {
            User userToTransmit = userDao.saveUser(user);
            userToTransmit.setPassword("");
            return userToTransmit;
        } else {
            return null;
        }
    }

    public User validateUser(AuthRequest req) {
        Optional<User> user = Optional.of(getUserByEmail(req.getUsername()));

        if (user.isPresent() && user.get().getPassword().equals(req.getPassword())) {
            user.get().setPassword("");
            return user.get();
        } else {
            return null;
        }
    }

    public User getUserByEmail(String email) {
        User user = userDao.getUserByEmail(email);
        if (user == null) {

            return null;
        } else {
        	
            return user;
        }

    }

    public boolean validateHint(String email,String que,String ans) {
    	User user = getUserByEmail(email);
    	if(user!=null) {
    		if(user.getSecurity_questions().equals(que) && user.getSecurity_answer().equals(ans)) {
    			return true;
    		}
    	}
    	return false;
    }

    public User updatePassword(String email, String password) {
        if(!email.isEmpty() && !password.isEmpty()) {
        	
        	User userDetails = userDao.getUserByEmail(email);
        	
        	userDetails.setPassword(password);
        	
        	userDetails = userDao.saveUser(userDetails);
        	
        	return userDetails;
        }
        return null;
    }

    public User updateUserAccount(String email, User user) {
        User userO = getUserByEmail(email);
        user.setId(userO.getId());
        user.setPassword(userO.getPassword());
        user.setSecurity_questions(userO.getSecurity_questions());
        user.setSecurity_answer(userO.getSecurity_answer());
        user.setRoles(userO.getRoles());
        return userDao.saveUser(user);
    }

}

