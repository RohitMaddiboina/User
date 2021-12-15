package com.ecommerce.user.service;

import com.ecommerce.user.dao.UserDao;
import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.ImageModel;
import com.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	// To save user details during registration
	public User saveUser(User user) {
		if (getUserByEmail(user.getEmail()) == null) {
			User userToTransmit = userDao.saveUser(user);
			userToTransmit.setPassword("");
			return userToTransmit;
		} else {
			return null;
		}
	}

	// This is to validate user login
	public User validateUser(AuthRequest req) {
		Optional<User> user = Optional.of(getUserByEmail(req.getUsername()));

		if (user.isPresent() && user.get().getPassword().equals(req.getPassword())) {
			user.get().setPassword("");
			return user.get();
		} else {
			return null;
		}
	}

	// This retrieves the User details by using email
	public User getUserByEmail(String email) {
		User user = userDao.getUserByEmail(email);
		if (user == null) {

			return null;
		} else {

			return user;
		}

	}

	// This is to validate the hint during forgot password
	public boolean validateHint(String email, String que, String ans) {
		User user = getUserByEmail(email);
		if (user != null) {
			if (user.getSecurityQuestions().equals(que) && user.getSecurityAnswer().equals(ans)) {
				return true;
			}
		}
		return false;
	}

	// this is to update thte password during reset password
	public User updatePassword(String email, String password) {
		if (!email.isEmpty() && !password.isEmpty()) {

			User userDetails = userDao.getUserByEmail(email);

			userDetails.setPassword(password);

			userDetails = userDao.saveUser(userDetails);

			return userDetails;
		}
		return null;
	}

	// This is to update the user details when user edits his/her details
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
		User u = getUserByEmail(email);
		if (u.getWalletAmount() > amount) {
			u.setWalletAmount(u.getWalletAmount() - amount);
			userDao.saveUser(u);
			return true;
		}
		return false;
	}

	public User creditToUserWallet(String email, float amount) {
		User user = userDao.getUserByEmail(email);
		if (user == null) {

		} else {
			user.setWalletAmount(user.getWalletAmount() + amount);
		}
		return userDao.saveUser(user);
	}

	public ImageModel getProfilePicture(String email) {
		User user = userDao.getUserByEmail(email);
		ImageModel imageModel = new ImageModel();
		if (user == null) {
			return null;
		} else {
			imageModel.setEmail(email);
			imageModel.setImage(decompressBytes(user.getProfilePic()));
			
		}
		return imageModel;
	}

	public User updateProfilePicture(String email, MultipartFile file) throws IOException {
		
		User user = userDao.getUserByEmail(email);
		if (user == null) {

		} else {
			user.setProfilePic(compressBytes(file.getBytes()));
		}
		return userDao.saveUser(user);
	}

	// compress the image bytes before storing it in the database
	public byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}

	// uncompress the image bytes before returning it to the angular application
	public  byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
}
