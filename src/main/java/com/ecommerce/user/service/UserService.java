package com.ecommerce.user.service;

import com.ecommerce.user.dao.UserDao;
import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    //To save user details during registration
    public User saveUser(User user) {
        if (getUserByEmail(user.getEmail()) == null) {
            User userToTransmit = userDao.saveUser(user);
            userToTransmit.setPassword("");
            return userToTransmit;
        } else {
            return null;
        }
    }

    //This is to validate user login
    public User validateUser(AuthRequest req) {
        Optional<User> user = Optional.of(getUserByEmail(req.getUsername()));

        if (user.isPresent() && user.get().getPassword().equals(req.getPassword())) {
            user.get().setPassword("");
            return user.get();
        } else {
            return null;
        }
    }

    //This retrieves the User details by using email
    public User getUserByEmail(String email) {
        User user = userDao.getUserByEmail(email);
        if (user == null) {

            return null;
        } else {
        	
            return user;
        }

    }

    //This is to validate the hint during forgot password
    public boolean validateHint(String email,String que,String ans) {
    	User user = getUserByEmail(email);
    	if(user!=null) {
    		if(user.getSecurityQuestions().equals(que) && user.getSecurityAnswer().equals(ans)) {
    			return true;
    		}
    	}
    	return false;
    }

    //this is to update thte password during reset password
    public User updatePassword(String email, String password) {
        if(!email.isEmpty() && !password.isEmpty()) {
        	
        	User userDetails = userDao.getUserByEmail(email);
        	
        	userDetails.setPassword(password);
        	
        	userDetails = userDao.saveUser(userDetails);
        	
        	return userDetails;
        }
        return null;
    }

    //This is to update the user details when user edits his/her details
    public User updateUserAccount(String email, User user) {
        User userO = getUserByEmail(email);
        user.setId(userO.getId());
        user.setPassword(userO.getPassword());
        user.setSecurityQuestions(userO.getSecurityQuestions());
        user.setSecurityAnswer(userO.getSecurityAnswer());
        user.setRoles(userO.getRoles());
        return userDao.saveUser(user);
    }
    
    public float getUserWalletAmount(String email) {
		return getUserByEmail(email).getWalletAmount();
	}

	public Object debitFromUserWallet(String email, float amount) {
		User u=getUserByEmail(email);
		if(u.getWalletAmount()>amount) {
			u.setWalletAmount(u.getWalletAmount()-amount);
			userDao.saveUser(u);
			return true;
		}
		return false;
	}

	public User creditToUserWallet(String email, float amount) {
		User user=userDao.getUserByEmail(email);
		if(user==null) {
			
		}else {
			user.setWalletAmount(user.getWalletAmount()+amount);
		}
		return userDao.saveUser(user);
	}

}

