package com.meettwo.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * {@link ServiceStatus} class commonly used to wrap the application's rest
 * services response to common format.
 * 
 * @author amit.patel
 * @version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServiceStatus {

	private String status;
	private String message;
	private Object result;
	private String apiKey;
	
	public ServiceStatus(){}
	
	public ServiceStatus(String status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Object getResult() {
		return result;
	}
	
	public void setResult(Object result) {
		this.result = result;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
}
