package com.bankerarea.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserVO {
	@NotNull @NotEmpty
	private String id;
	@NotNull @NotEmpty
	private String password;
	@NotNull @NotEmpty @Email
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
