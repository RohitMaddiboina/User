package com.ecommerce.user.controller;


import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.AuthResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.service.UserService;
import com.ecommerce.user.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/fasscio")
public class UseController {

	  @Autowired
	    private AuthenticationManager authenticationManager;
	    @Autowired
	    private JwtUtil jwtUtil;
	    public static final String   TOKEN_STRING  = "Authorization";

	    @Autowired
	    private UserDetailsService userDetailsService;
	    
	    @Autowired
	    private UserService userService;
	    @GetMapping("/hello")
	    public String greet(){
	        return "Hello";
	    }
	    @PostMapping("/user-validate/")
	    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest){
//	    	log.info("createAuthentication method started ");
	        UsernamePasswordAuthenticationToken token=
	                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword());

	        authenticationManager.authenticate(token);
	        System.out.println(authenticationRequest.getPassword());
	        UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	        System.out.println("-----"+userDetails);
	        String jwt = jwtUtil.generateToken(userDetails);
//	        log.debug("JWT-->",jwt);
//	        log.info("createAuthentication method Ended ");
	        return ResponseEntity.ok(new AuthResponse(jwt));




	    }
	    @PostMapping("/validate")
	    public ResponseEntity<Object> validateToken(@RequestHeader(TOKEN_STRING) String token) throws UsernameNotFoundException {
//	    	log.info("ValidateToken method started");
	        UserDetails userDetails=userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
	        try {
	        		
//	        	log.info("validateTokem method ended");
	        		return new ResponseEntity<>(jwtUtil.validateToken(token,userDetails), HttpStatus.OK);
	        }
	        catch(Exception ex) {
//	        	log.error("There is an error -->"+ex.getMessage());
//	        	log.info("validateTokem method ended");
	            return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
	        }
	        
	    }
///---------------------------------------------------//
    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){

    	
    	user.setRoles("ROLE_USER");
        if(userService.saveUser(user)!=null)
        {

            return ResponseEntity.ok(user);
        }
        return   ResponseEntity.notFound().build();
    }


    @GetMapping("/get")
    public User  getUser(@RequestHeader(TOKEN_STRING) String token){
        System.out.println(token);
        if(token!=null && token.startsWith("Bearer")){
            String jwt=token.substring(7);
            String email = jwtUtil.extractUsername(jwt);
	        if(userService.getUserByEmail(email)!=null){
	            System.out.println(userService.getUserByEmail(email));
	            return userService.getUserByEmail(email);
	
	        }
	        return  null;
        }
        else {
        	return null;
        }
    }

    @PutMapping("/update")
    public User  updateUser(@RequestHeader(TOKEN_STRING) String token,@RequestBody User user){

      
        if(token!=null && token.startsWith("Bearer")){
            String jwt=token.substring(7);
            String email = jwtUtil.extractUsername(jwt);

  
            if(userService.updatePassword(email,user)!=null){
            	
            	return userService.updatePassword(email,user);
            	
            }
            return null;

        }
        return null;
    }
    @PutMapping("/updateAccount")
    public User updateAccountDetails(@RequestHeader(TOKEN_STRING) String token, @RequestBody User user) {
        
        if(token!=null && token.startsWith("Bearer")){
            String jwt=token.substring(7);
            String email = jwtUtil.extractUsername(jwt);
            return userService.updateUserAccount(email, user);
        }
        else {
        	return null;
        }

    }

}
