package com.bankerarea.vo;

import lombok.Data;

@Data
public class UserVO {
	private String id;
	private String password;
	private String email;
	private String accessKey;
	
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
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	@Override
	public String toString() {
		return "UserVO [id=" + id + ", password=" + password + ", email=" + email + ", accessKey=" + accessKey + "]";
	}
	
}
