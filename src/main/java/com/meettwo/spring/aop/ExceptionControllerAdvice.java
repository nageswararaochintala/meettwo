package com.meettwo.spring.aop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.meettwo.util.ServiceStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {
 
	@ExceptionHandler(Exception.class)
    @ResponseBody
    public ServiceStatus exception(Exception e) {
    	ServiceStatus serviceStatus = new ServiceStatus();
    	e.printStackTrace();
    	serviceStatus.setStatus("Error");
    	serviceStatus.setMessage("Server error. Please contact Administrator");
    	
        return serviceStatus;
    }
	
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    public ServiceStatus resourceNotFound(Exception e) {
    	ServiceStatus serviceStatus = new ServiceStatus();
    	e.printStackTrace();
    	serviceStatus.setStatus("Invalid resource path");
    	serviceStatus.setMessage("URL does not exist");
    	
        return serviceStatus;
    }
}