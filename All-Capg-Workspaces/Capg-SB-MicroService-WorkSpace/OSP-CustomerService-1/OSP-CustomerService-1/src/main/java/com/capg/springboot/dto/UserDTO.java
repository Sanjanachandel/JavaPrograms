package com.capg.springboot.dto;

public class UserDTO {
	 private String userId;
	    private String role;
	 public UserDTO() {
		 
	 }
	 public UserDTO(String userId, String role) {
		super();
		this.userId = userId;
		this.role = role;
	 }
	 public String getUserId() {
		 return userId;
	 }
	 public void setUserId(String userId) {
		 this.userId = userId;
	 }
	 public String getRole() {
		 return role;
	 }
	 public void setRole(String role) {
		 this.role = role;
	 }
	 
}
