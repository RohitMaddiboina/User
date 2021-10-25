package com.ecommerce.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private  String lastName;
    @Column(unique=true)
    private String email;
    private String gender;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dob;
    private String password;
    private String phone;
    private String houseNo;
    private String street;
    private String city;
    private String district;
    private String state;
    private int pincode;
    private String landmark;
    private String security_questions;
    private String security_answer;

    public User(String firstName, String lastName, String email, String gender, Date dob, String password,
                String phone, String houseNo, String street, String city, String district, String state, int pincode,
                String landmark, String security_questions, String security_answer) {
        super();
    }}
