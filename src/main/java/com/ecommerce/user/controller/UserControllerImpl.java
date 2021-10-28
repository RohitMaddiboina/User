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
@CrossOrigin(origins = "http://localhost:4200")
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

	@Override
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				authenticationRequest.getUsername(), authenticationRequest.getPassword());

		authenticationManager.authenticate(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		System.out.println("-----" + userDetails);
		String jwt = jwtUtil.generateToken(userDetails);
       
		return ResponseEntity.ok(new AuthResponse(jwt));

	}

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

	@Override
	public ResponseEntity<User> saveUser(@Valid @RequestBody User user, BindingResult bResult) {

		System.out.println(user.getEmail());
		if (!bResult.hasErrors()) {		
			user.setRoles("ROLE_USER");
			if (userService.saveUser(user) != null) {
				
				return ResponseEntity.ok(user);
			}
		}

		return ResponseEntity.notFound().build();

	}

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
			return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
		}
	}

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

	@Override
	public ResponseEntity<User> updateUser(@RequestBody PasswordEntity pwdEntity) {

		User userUpdated = userService.updatePassword(pwdEntity.getEmail(), pwdEntity.getPassword());
		if (userUpdated != null) {

			return ResponseEntity.ok(userUpdated);

		}
		return ResponseEntity.notFound().build();

	}

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

}
