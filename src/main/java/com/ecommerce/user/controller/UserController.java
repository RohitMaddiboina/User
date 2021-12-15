package com.ecommerce.user.controller;

import java.io.IOException;
import java.sql.Blob;

import org.springframework.http.ResponseEntity;


import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.ImageModel;
import com.ecommerce.user.model.PasswordEntity;
import com.ecommerce.user.model.User;


@RequestMapping("/fasscio")
public interface UserController {

	@PostMapping("/user-validate/")
	ResponseEntity<Object> createAuthenticationToken(AuthRequest authenticationRequest);

	@PostMapping("/validate")
	ResponseEntity<Object> validateToken(String token) throws UsernameNotFoundException;

	@PostMapping("/save")
	ResponseEntity<User> saveUser(User user, BindingResult bResult);

	@GetMapping("/get")
	ResponseEntity<User> getUser(String token);

	@GetMapping("/hint/{email}/{que}/{ans}")
	ResponseEntity<Void> validateHint(String email, String question, String ans);

	@PutMapping("/update")
	ResponseEntity<User> updateUser(PasswordEntity pwdEntity);

	@PutMapping("/updateAccount")
	ResponseEntity<User> updateAccountDetails(String token, User user);
	
	@GetMapping("/wallet")
	ResponseEntity<Float> getUserWalletAmount(String token);
	
	@PutMapping("/debit/{amount}")
	public ResponseEntity<Object> debitFromUserWallet(String token,float amount);
	
	@PutMapping("/cedit/{amount}")
	public ResponseEntity<User> addAmountToUserWallet(String token,float amount);
	
	@GetMapping("/profilePicture")
	public ResponseEntity<ImageModel> getProfilePicture(String token);
	
	@PutMapping(value="/profilePicture")
	public ResponseEntity<Void> updateProfilePicture(String token,MultipartFile profilePicture) throws IOException;
	
}