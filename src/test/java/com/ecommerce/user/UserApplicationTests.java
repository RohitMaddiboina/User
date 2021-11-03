package com.ecommerce.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.user.model.AuthRequest;
import com.ecommerce.user.model.PasswordEntity;
import com.ecommerce.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class UserApplicationTests {

	@Autowired 
	private MockMvc mock;

	private static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2hpdHRlamExOThAZ21haWwuY29tIiwiZXhwIjoxNjM2MDMzNzE5LCJpYXQiOjE2MzU3NzQ1MTl9.TP3qQilvCG9O2nDUx74rfcXlWj8yljWNMsMVrRacAJQ";

	@Test
	void createAuthenticationToken() throws Exception {
		AuthRequest authRequest = new AuthRequest();
		authRequest.setUsername("rohitteja198@gmail.com");
		authRequest.setPassword("R@hIt3675");
		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writeValueAsString(authRequest);
		mock.perform(post("/fasscio/user-validate/").content(jsonData).contentType("application/json"))
				.andExpect(status().isOk()).andExpect(jsonPath("$").exists());

	}

	@Test
	void saveUser() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		Date dob = format.parse("1998-01-24");
		User user = new User("vamsi", "krishna", "posting@gmail.com", "male", dob, "password", "9550762955", "2-38/48",
				"Barampeta", "guntur", "guntur", "Andhra", 522601, "landmark", "What is your favorite color?", "green",5000,
				"role");

		ObjectMapper mapper = new ObjectMapper();
		String jsonData = mapper.writeValueAsString(user);
		mock.perform(post("/fasscio/save").content(jsonData).contentType("application/json")).andExpect(status().isOk()).andExpect(jsonPath("$").exists());

	}

	@Test
	void allMethods() throws Exception {
//getUser()
		mock.perform(get("/fasscio/get").header("Authorization", "Bearer " + token)).andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());

// @Test
// void updateAccountDetails() throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

		Date dob = format.parse("1998-01-24");

		User user = new User("krishna", "krishna", "rohitteja198@gmail.com", "male", dob, "password", "9550762955",
				"2-38/48", "Barampeta", "guntur", "guntur", "Andhra", 522601, "landmark",
				"What is your favorite color?", "green", 5000,"role");

		ObjectMapper mapper = new ObjectMapper();

		String jsonData = mapper.writeValueAsString(user);

		mock.perform(put("/fasscio/updateAccount").content(jsonData).contentType("application/json")
				.header("Authorization", "Bearer " + token)).andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());

//validateHint()
		mock.perform(get("/fasscio/hint/rohitteja198@gmail.com/What is your favorite color/red").content(jsonData)
				.contentType("application/json")).andExpect(status().isAccepted());

//updateUser(@RequestBody PasswordEntity pwdEntity)
		PasswordEntity pe = new PasswordEntity();

		pe.setEmail("rohitteja198@gmail.com");
		pe.setPassword("R@hIt3675");

		ObjectMapper mapper1 = new ObjectMapper();

		String jsonData1 = mapper1.writeValueAsString(pe);

		mock.perform(put("/fasscio/update").content(jsonData1).contentType("application/json"))
				.andExpect(status().isOk()).andExpect(jsonPath("$").exists());

	}

}