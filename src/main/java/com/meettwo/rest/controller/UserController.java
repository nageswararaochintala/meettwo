package com.meettwo.rest.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.meettwo.model.User;
import com.meettwo.model.UserProfile;
import com.meettwo.model.UserSubscriptions;
import com.meettwo.security.MeetTwoUserDetails;
import com.meettwo.service.UserService;
import com.meettwo.util.ServiceStatus;

/**
 * {@link UserController} class provides the user related services like login
 * 
 * @author Nageswara rao.ch
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired 
	TokenStore tokenStore;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	ConsumerTokenServices consumerTokenServices;
	
	static final Logger logger = Logger.getLogger(UserController.class);

	
	@RequestMapping(value = "/checkSession", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "application/json" })
	public ServiceStatus checkToken(
			@RequestHeader(name = "Authorization", required = true) String authHeaderToken) {
        
		ServiceStatus serviceStatus = new ServiceStatus();
		serviceStatus.setStatus("success");
		serviceStatus.setMessage("Valid user session.");

		if (tokenStore == null) {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			WebApplicationContext applicationContext = WebApplicationContextUtils
					.getRequiredWebApplicationContext(requestAttributes
							.getRequest().getServletContext());

			tokenStore = (TokenStore) applicationContext.getBean("tokenStore");
			
		}

	
		String authToken = authHeaderToken.toString().substring("Bearer ".length());
		OAuth2AccessToken oauth2TokenDetails = tokenStore
				.readAccessToken(authToken);

		if (oauth2TokenDetails == null || oauth2TokenDetails.isExpired()) {

			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("User session does not exist..");

			return serviceStatus;
		} else {
			OAuth2Authentication oauth2Authentication = tokenStore
					.readAuthentication(oauth2TokenDetails);
		

			if (oauth2Authentication != null
					&& oauth2Authentication.getUserAuthentication() != null
					&& oauth2Authentication.getUserAuthentication()
							.getPrincipal() != null
					&& oauth2Authentication.getUserAuthentication()
							.getPrincipal() instanceof MeetTwoUserDetails) {

				Map<String, Object> result = new HashMap<>();

				MeetTwoUserDetails userDetails = (MeetTwoUserDetails) oauth2Authentication
						.getUserAuthentication().getPrincipal();

				result.put("userId", userDetails.getUserId());
				result.put("userRole", userDetails.getUserRole().getName());
				
			/*	List<EmployerServicePermission> employerServicePermissions = 
					    userDetails.getSubEmployerPermissions().stream()
					              .map(SubEmployerPermission::getEmployerServicePermission)
					              .collect(Collectors.toList());
				
				List<String> privileges = 
					    employerServicePermissions.stream()
					              .map(EmployerServicePermission::getPermission)
					              .collect(Collectors.toList());
				
				List<AdminServicePermission> adminServicePermissions = 
					    userDetails.getSubAdminPermissions().stream()
					              .map(SubAdminPermission::getAdminServicePermission)
					              .collect(Collectors.toList());
				
				
				
				List<String> adminPrivileges=adminServicePermissions.stream()
	              .map(AdminServicePermission::getPermission)
	              .collect(Collectors.toList());
				
				if(adminPrivileges!=null&&!adminPrivileges.isEmpty()){
					
					privileges.addAll(adminPrivileges);
				}*/
				
				
				//List<String> subscriptionPermissions=subscriptionService.getSubscriptionPermissions(userDetails.getUserId());
				
				/*if(subscriptionPermissions!=null&&!subscriptionPermissions.isEmpty()){
					privileges.addAll(subscriptionPermissions);
				}*/
				
				//result.put("privileges",privileges);
				result.put("email", userDetails.getEmailId());
									
				serviceStatus.setResult(result);
			}

		}

		return serviceStatus;
	}
	
	
	@RequestMapping(value = "/checkSessionOnPageRefresh", method = RequestMethod.POST, produces = { "application/json" }, consumes = { "application/json" })
	public ServiceStatus checkTokenOnPageRefresh(
			@RequestHeader(name = "Authorization", required = true) String authHeaderToken) {

		ServiceStatus serviceStatus = new ServiceStatus();
		serviceStatus.setStatus("success");
		serviceStatus.setMessage("Valid user session..");

		if (tokenStore == null) {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			WebApplicationContext applicationContext = WebApplicationContextUtils
					.getRequiredWebApplicationContext(requestAttributes
							.getRequest().getServletContext());

			tokenStore = (TokenStore) applicationContext.getBean("tokenStore");
		}

		String authToken = authHeaderToken.substring("Bearer ".length());
		OAuth2AccessToken oauth2TokenDetails = tokenStore
				.readAccessToken(authToken);

		if (oauth2TokenDetails == null || oauth2TokenDetails.isExpired()) {

			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("User session does not exist..");

			return serviceStatus;
		} else {
			OAuth2Authentication oauth2Authentication = tokenStore
					.readAuthentication(oauth2TokenDetails);

			if (oauth2Authentication != null
					&& oauth2Authentication.getUserAuthentication() != null
					&& oauth2Authentication.getUserAuthentication()
							.getPrincipal() != null
					&& oauth2Authentication.getUserAuthentication()
							.getPrincipal() instanceof MeetTwoUserDetails) {

				Map<String, Object> result = new HashMap<>();

				MeetTwoUserDetails userDetails = (MeetTwoUserDetails) oauth2Authentication
						.getUserAuthentication().getPrincipal();

			/*	List<EmployerServicePermission> employerServicePermissions = 
					    userDetails.getSubEmployerPermissions().stream()
					              .map(SubEmployerPermission::getEmployerServicePermission)
					              .collect(Collectors.toList());
				
				List<String> privileges = 
					    employerServicePermissions.stream()
					              .map(EmployerServicePermission::getPermission)
					              .collect(Collectors.toList());*/
				//result.put("privileges", privileges);
				result.put("userId", userDetails.getUserId());
				result.put("userRole", userDetails.getUserRole().getName());
				result.put("email", userDetails.getEmailId());
									
				
					
				serviceStatus.setResult(result);
			}

		}

		return serviceStatus;
	}

	
	
	
	/**
	 * This method used to user login
	 * 
	 * @param user
	 *            contains the user email and password for login
	 * @return {@link ServiceStatus} class object with login status
	 */
	@RequestMapping(value = "/login", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus loginUser(@RequestBody User user) {

		ServiceStatus serviceStatus = new ServiceStatus();
		User dbUser = null;

		/*if (!ArtemisUtil.isEmptyString(user.getEmailId()) && !ArtemisUtil.isEmptyString(user.getPassword())
				&& ArtemisUtil.isRegexTrue(ArtemisConstant.REGEX_EMAIL, user.getEmailId())
				&& ArtemisUtil.isRegexTrue(ArtemisConstant.REGEX_EMAIL, user.getPassword())
				 ) {*/
			try {

				dbUser = userService.getUserByEmailId(user.getEmailId());
				if (dbUser != null) {
					
						if (user.getEmailId().trim().equalsIgnoreCase(dbUser.getEmailId())
								&& passwordEncoder.matches(user.getPassword().trim(), dbUser.getPassword())) {
							Timestamp currentLogin=new Timestamp(System.currentTimeMillis());
							
							if(dbUser.getCurrentLogin()!=null){
								dbUser.setLastLogin(dbUser.getCurrentLogin());	
							}else{
								dbUser.setLastLogin(currentLogin);
							}
							
							dbUser.setCurrentLogin(currentLogin);
							//userService.updateUser(dbUser);
							serviceStatus.setMessage("successfully logged in ");
							serviceStatus.setStatus("success");
						} else {
							serviceStatus.setMessage("check email and password");
							serviceStatus.setStatus("failure");
						}
				
					
				} else {
					serviceStatus.setMessage("user not present");
					serviceStatus.setStatus("failure");
				}
			} catch (Exception e) {
				serviceStatus.setMessage("failure");
				serviceStatus.setStatus("failure");
			}
/*
		} else {
			serviceStatus.setMessage("invalid user credentail");
			serviceStatus.setStatus("failure");
		}*/

		return serviceStatus;
	}
	
	
	@RequestMapping(value="/registration",method= RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceStatus  userRegistration(@RequestBody UserProfile userprofile){
		ServiceStatus serviceStatus = new ServiceStatus();
		
		userService.userRegistration(userprofile);
		
		serviceStatus.setMessage("user registered successfully");
		serviceStatus.setStatus("success");
		
		return serviceStatus;
	}
	
	@RequestMapping(value="/subscribeService",method= RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceStatus  subscribeService(@RequestBody UserSubscriptions userSubscriptions){
		ServiceStatus serviceStatus = new ServiceStatus();
		
		userService.subscribeService(userSubscriptions);
		
		serviceStatus.setMessage("user subscribed for service successfully");
		serviceStatus.setStatus("success");
		
		return serviceStatus;
	}
	
	@RequestMapping(value="/accessMap",method= RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceStatus  accessMap(){
		ServiceStatus serviceStatus = new ServiceStatus();
		
		int result = userService.accessMap();
		
		serviceStatus.setMessage("accessed map successfully");
		serviceStatus.setStatus("success");
		serviceStatus.setResult(result);
		return serviceStatus;
	}
	
	@RequestMapping(value="/userManagement",method= RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceStatus  userManagement(){
		ServiceStatus serviceStatus = new ServiceStatus();
		
		int result = userService.accessMap();
		
		serviceStatus.setMessage("getting all users successfully");
		serviceStatus.setStatus("success");
		serviceStatus.setResult(result);
		return serviceStatus;
	}
	
	private  String getFileExtension(String fileName) {
	    
	    if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	    return fileName.substring(fileName.lastIndexOf(".")+1);
	    else return "";
	}
	private String generatePassword(){
		 String password = RandomStringUtils.random(4, true, true);
		 password += RandomStringUtils.random(1, false, true);
		 password+=RandomStringUtils.random(1, "!@#$%^&");
		 return password;
	}


}
