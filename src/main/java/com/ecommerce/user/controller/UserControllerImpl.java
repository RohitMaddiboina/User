package com.ecommerce.user.controller;

import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.AuthResponse;
import com.ecommerce.user.model.PasswordEntity;
import com.ecommerce.user.model.User;
import com.ecommerce.user.service.UserService;
import com.ecommerce.user.util.JwtUtil;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin
@RequestMapping("/fasscio")
public class UserControllerImpl implements UserController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;
	public static final String TOKEN_STRING = "Authorization";

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	//This is to create the JWT token and also to check user login
	@Override
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword());

		authenticationManager.authenticate(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String jwt = jwtUtil.generateToken(userDetails);
       
		return ResponseEntity.ok(new AuthResponse(jwt));

	}

	//This is to validate the token sent by other microservices to verify that the request is genuine
	@Override
	public ResponseEntity<Object> validateToken(@RequestHeader(TOKEN_STRING) String token)
			throws UsernameNotFoundException {
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
		try {

			return new ResponseEntity<>(jwtUtil.validateToken(token, userDetails), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}

	}

	//This is to save the user details
	@Override
	public ResponseEntity<User> saveUser(@Valid @RequestBody User user, BindingResult bResult) {

		if (!bResult.hasErrors()) {		
			System.out.println(user);
			user.setRoles("ROLE_USER");
			user.setWalletAmount(10000);
			if (userService.saveUser(user) != null) {
				
				return ResponseEntity.ok(user);
			}
		}

		return ResponseEntity.notFound().build();

	}

	//This is to get User details by using token
	@Override
	public ResponseEntity<User> getUser(@RequestHeader(TOKEN_STRING) String token) {

		if (token != null && token.startsWith("Bearer")) {
			String jwt = token.substring(7);
			String email = jwtUtil.extractUsername(jwt);
			User userUpdated = userService.getUserByEmail(email);
			if (userUpdated != null) {
				return ResponseEntity.ok(userService.getUserByEmail(email));

			}
			return ResponseEntity.notFound().build();
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	//This is to validate the user hint during forgot password
	@Override
	public ResponseEntity<Void> validateHint(@PathVariable("email") String email, @PathVariable("que") String question,
			@PathVariable("ans") String ans) {
		question = question + '?';
		if (userService.validateHint(email, question, ans)) {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	//This is to update the user password after submitting new password
	@Override
	public ResponseEntity<User> updateUser(@RequestBody PasswordEntity pwdEntity) {

		User userUpdated = userService.updatePassword(pwdEntity.getEmail(), pwdEntity.getPassword());
		if (userUpdated != null) {

			return ResponseEntity.ok(userUpdated);

		}
		return ResponseEntity.notFound().build();

	}

	//This is to update the user after the editing the user details
	@Override
	public ResponseEntity<User> updateAccountDetails(@RequestHeader(TOKEN_STRING) String token,
			@RequestBody User user) {

		if (token != null && token.startsWith("Bearer")) {
			String jwt = token.substring(7);
			String email = jwtUtil.extractUsername(jwt);
			User userUpdated = userService.updateUserAccount(email, user);
			if (userUpdated != null) {

				return ResponseEntity.ok(userUpdated);
			}
			return ResponseEntity.notFound().build();
		}

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}
	
	@Override
	public ResponseEntity<Float> getUserWalletAmount(@RequestHeader(TOKEN_STRING) String token) {
		if (token != null && token.startsWith("Bearer")) {
			String jwt = token.substring(7);
			String email = jwtUtil.extractUsername(jwt);
			
			return ResponseEntity.ok(userService.getUserWalletAmount(email));
		}

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@Override
	public ResponseEntity<Object> debitFromUserWallet(@RequestHeader(TOKEN_STRING) String token,@PathVariable float amount) {
		if (token != null && token.startsWith("Bearer")) {
			String jwt = token.substring(7);
			String email = jwtUtil.extractUsername(jwt);
			
			return ResponseEntity.ok(userService.debitFromUserWallet(email,amount));
		}

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@Override
	public ResponseEntity<User> addAmountToUserWallet(@RequestHeader(TOKEN_STRING) String token,@PathVariable float amount) {
		if (token != null && token.startsWith("Bearer")) {
			String jwt = token.substring(7);
			String email = jwtUtil.extractUsername(jwt);
			return new ResponseEntity<User>(userService.creditToUserWallet(email,amount),HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

}
