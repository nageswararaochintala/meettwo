package com.meettwo.dto;



public class UserPredicateObject {
    
	DateRange createdDate;
	String emailId;
    Boolean status;
    
	public DateRange getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateRange createdDate) {
		this.createdDate = createdDate;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
    
}
