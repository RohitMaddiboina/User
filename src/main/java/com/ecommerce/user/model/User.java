package com.ecommerce.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Min(value = 3)
    @Pattern(regexp = "^[a-zA-Z \\-\\']+")
    private String firstName;
    
    private  String lastName;
    
    @Column(unique=true)
    @Pattern(regexp = "^[a-zA-Z0-9_.+]+(?<!^[0-9]*)@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;
    private String gender;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dob;
    
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{8,})")
    private String password;
    
    @Pattern(regexp = "[7-9]{1}[0-9]{9}")
    private String phone;
    private String houseNo;
    private String street;
    private String city;
    private String district;
    private String state;
    private int pincode;
    private String landmark;
    @Column(name = "security_questions")
    private String securityQuestions;
    @Column(name="security_answer")
    private String securityAnswer;
    private String roles;
    
	public User(String firstName, String lastName, String email, String gender, Date dob, String password, String phone,
			String houseNo, String street, String city, String district, String state, int pincode, String landmark,
			String securityQuestions, String securityAnswer, String roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.dob = dob;
		this.password = password;
		this.phone = phone;
		this.houseNo = houseNo;
		this.street = street;
		this.city = city;
		this.district = district;
		this.state = state;
		this.pincode = pincode;
		this.landmark = landmark;
		this.securityQuestions = securityQuestions;
		this.securityAnswer = securityAnswer;
		this.roles = roles;
	}
   
}
