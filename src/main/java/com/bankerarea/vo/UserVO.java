package com.bankerarea.vo;

import lombok.Data;

@Data
public class UserVO {
	private String id;
	private String password;
	private String email;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "UserVO [id=" + id + ", password=" + password + ", email=" + email + "]";
	}
	
}
