package com.rabin.jobportal;

import java.io.Serializable;

public class JobSeeker implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String username;
	public String password;
	

	public String fName;
	public String lName;
	public String email;
	public String phone;
	public String location;
	public String skill_set;
	

	public String js_likes;

	public JobSeeker(){}

	public JobSeeker(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}


	public JobSeeker(String username, String password, String js_likes) {
		super();
		this.username = username;
		this.password = password;
		this.js_likes = js_likes;
	}


	public JobSeeker(String username, String password, String fName,
			String lName, String email, String phone, String location,
			String skill_set, String js_likes) {
		super();
		this.username = username;
		this.password = password;
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.phone = phone;
		this.location = location;
		this.skill_set = skill_set;
		this.js_likes = js_likes;
	}
	
	
	
	
}
