package com.ecommerce.user.controller;

import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.User;
import com.ecommerce.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/fasscio")
public class UseController {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){



        if(userService.saveUser(user)!=null)
        {

            return ResponseEntity.ok(user);
        }
        return   ResponseEntity.notFound().build();
    }

    @PostMapping("/user-validate/")
    public ResponseEntity<Void> validateUser(@RequestBody AuthRequest req){

        if(userService.validateUser(req)!=null){

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/get/{email}")
    public User  getUser(@PathVariable String email){
        if(userService.getUserByEmail(email)!=null){
            System.out.println(userService.getUserByEmail(email));
            return userService.getUserByEmail(email);

        }
        return  null;
    }

    @PutMapping("/update/{email}")
    public User  updateUser(@PathVariable String email,@RequestBody User user){

        System.out.println("-------------------------"+user);

        if(userService.updatePassword(email,user)!=null){

            return userService.updatePassword(email,user);

        }
        return null;
    }
    @PutMapping("/updateAccount/{email}")
    public User updateAccountDetails(@PathVariable String email, @RequestBody User user) {

        return userService.updateUserAccount(email, user);

    }

}
