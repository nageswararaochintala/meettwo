package com.meettwo.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.docx4j.Docx4J;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jboss.logging.Logger;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.meettwo.dto.MeetTwoMail;
import com.meettwo.model.User;
import com.meettwo.model.UserProfile;
import com.meettwo.model.UserSubscriptions;
import com.meettwo.security.MeetTwoUserDetails;
import com.meettwo.service.UserService;
import com.meettwo.util.MeetTwoUtil;
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
	RabbitTemplate template;
	
	@Autowired 
	DirectExchange direct;
	
	@Autowired
	Environment environment;
	
	@Autowired
	ConsumerTokenServices consumerTokenServices;
	
	static final Logger logger = Logger.getLogger(UserController.class);
	
	 private static final int BUFFER_SIZE = 4096;

	
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
		List<MeetTwoMail> mails=new ArrayList<MeetTwoMail>();
		MeetTwoMail mail =null;
		mail=new MeetTwoMail();
		mail.setTo(new String[]{userprofile.getUser().getEmailId()});
		mail.setSubject("Welcome to MeetTwo ");
		mail.setBody("HI ,\n "+userprofile.getFirstName()+"\n Your email is "
				+ "Successfully registered"
				+ "\n Your login mail & password \n"
				+ " username :"+userprofile.getUser().getEmailId() 
				+ " \n psssword :"+userprofile.getUser().getPassword()
				+ " \n Note: this is your temporary(machine generated) password highly recommanded to reset the password after login "
				+ "\n Thanks&Regards, \n "
				+ " MeetTwo Team ");
		mails.add(mail);
		template.convertAndSend(direct.getName(), environment.getRequiredProperty("rabbitmq.mail.key"),mails);
		
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
	
	@RequestMapping(value="/uploadFile",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	ServiceStatus uploadFile(@RequestParam(value="resumeFile",required=false) CommonsMultipartFile resumeFile){	
		
		ServiceStatus serviceStatus=new ServiceStatus();
		Boolean flag=false;
		try{
		if(resumeFile!=null && !resumeFile.isEmpty()){
			if(validateType(resumeFile)){
				flag=true;
				
			}else{
				flag=false;
			}
		}else{
			flag = false;
		}
		
		if(flag){
			
		userService.uploadFile(resumeFile);	
		serviceStatus.setMessage("successfully uploaded");
		serviceStatus.setStatus("success");
		}else {
			serviceStatus.setStatus("failure");
			serviceStatus.setMessage("invalid file type");
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			serviceStatus.setStatus("failure");
			serviceStatus.setStatus("failure");

		}
	
		return serviceStatus;
	}
	
	
	
	 @RequestMapping(value="/downloadFile",method = RequestMethod.GET)
	    public void downloadFile(HttpServletRequest request,
	            HttpServletResponse response,@RequestParam("fileName") String fileName) throws IOException {
		 ServletContext context = request.getServletContext();
		 Boolean isTwoFile=fileName.contains("<>");
		 String downloadFilePath="";
		 
		    if(isTwoFile){
		    	downloadFilePath=fileName.substring(0, fileName.indexOf("<>"));
		    }else{
		    	downloadFilePath=fileName;
		    }
	        	
	    	File downloadFile=new File(downloadFilePath);
		
		if(downloadFile.isFile()){
			   FileInputStream inputStream = new FileInputStream(downloadFile);

            // get MIME type of the file
            String mimeType = context.getMimeType(downloadFilePath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
         
            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());
     
            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    downloadFile.getName());
            response.setHeader(headerKey, headerValue);
     
            // get output stream of the response
            OutputStream outStream = response.getOutputStream();
     
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
     
            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
     
            inputStream.close();
            outStream.close();

		} 
		 
	 }
	 @RequestMapping(value="/deleteUsers",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
		ServiceStatus deleteUsersByUserId(@RequestBody List<String> users){	
			
			ServiceStatus serviceStatus=new ServiceStatus();
	   
			template.convertAndSend(direct.getName(), environment.getRequiredProperty("rabbitmq.userDelete.key"),users);
			
			serviceStatus.setMessage("user deleted successfully");
			serviceStatus.setStatus("success");
			
			return serviceStatus;
			
	 }
	 
	 @RequestMapping(value="/downloadPDFFile",method = RequestMethod.GET, produces="application/pdf")
	    public void downloadPDFFile(HttpServletRequest request,
	            HttpServletResponse response,@RequestParam("fileName") String filePath) throws IOException, Docx4JException {
		 ServletContext context = request.getServletContext();

		 if(filePath !=null){

			 System.out.println(filePath);
			 Boolean isTwoFile=filePath.contains("<>");
         
 		File downloadFile = null;
         String downloadFilePath="";       	
     	if(isTwoFile){
     	
     		downloadFilePath=filePath.substring(filePath.lastIndexOf("<>")+2);
     		if(!MeetTwoUtil.isEmptyString(downloadFilePath)){
         		downloadFile =new File(downloadFilePath);
     		}else {
     			downloadFilePath=filePath.substring(0,filePath.indexOf("<>"));
					downloadFile= new File(downloadFilePath);
				}
     		
     	}else {
     		downloadFilePath=filePath;
     		downloadFile=new File(filePath);
     		
			}
     
     	
        if(downloadFile.isFile()){
     	   
     	   FileInputStream inputStream = new FileInputStream(downloadFile);

     	   
            // get MIME type of the file
            String mimeType = context.getMimeType(downloadFilePath);
            
            // get output stream of the response
            OutputStream outStream = response.getOutputStream();
            
            
            String ext=getFileExtensions(downloadFile);

            if(!MeetTwoUtil.isEmptyString(ext)
         		   && ext.equalsIgnoreCase("docx")){
         	   
         	// Font regex (optional)
           		// Set regex if you want to restrict to some defined subset of fonts
           		// Here we have to do this before calling createContent,
           		// since that discovers fonts
           		String regex = null;
           		// Windows:
           		// String
           		// regex=".*(calibri|camb|cour|arial|symb|times|Times|zapf).*";
           		//regex=".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
           		// Mac
           		// String
           		// regex=".*(Courier New|Arial|Times New Roman|Comic Sans|Georgia|Impact|Lucida Console|Lucida Sans Unicode|Palatino Linotype|Tahoma|Trebuchet|Verdana|Symbol|Webdings|Wingdings|MS Sans Serif|MS Serif).*";
           		PhysicalFonts.setRegex(regex);

           		// Document loading (required)
           		WordprocessingMLPackage wordMLPackage;
           		
           			// Load .docx or Flat OPC .xml
           			System.out.println("Loading file from " + downloadFilePath);
           			wordMLPackage = WordprocessingMLPackage.load(new java.io.File(downloadFilePath));
           		
           		
           		// Refresh the values of DOCPROPERTY fields 
           		FieldUpdater updater = new FieldUpdater(wordMLPackage);
           		updater.update(true);

           		
           		
           	  // set content attributes for the response
           	  response.setHeader("X-Frame-Options", "SAMEORIGIN");
                
           	  response.setContentType("application/pdf");
                /* response.setContentLength((int) downloadFile.length());*/
          
                 // set headers for the response
                 String headerKey = "Content-Disposition";
                 String headerValue = String.format("filename=\"%s\"",
                         downloadFile.getName());
                 response.setHeader(headerKey, headerValue);
                  
                  Docx4J.toPDF(wordMLPackage, outStream);
                 
                 
           		 inputStream.close();
                  outStream.close();
           
            }else if (!MeetTwoUtil.isEmptyString(ext) && ext.equalsIgnoreCase("doc")) {
			
         	   
                FileInputStream in=new FileInputStream(downloadFile);
               /* HWPFDocument document=new HWPFDocument(in);
               
                
                PdfOptions options=null;
                */
                
          		
            	  // set content attributes for the response
            	  response.setHeader("X-Frame-Options", "SAMEORIGIN");
                 
            	  response.setContentType("application/pdf");
                 /* response.setContentLength((int) downloadFile.length());*/
           
                  // set headers for the response
                  String headerKey = "Content-Disposition";
                  String headerValue = String.format("filename=\"%s\"",
                          downloadFile.getName());
                  response.setHeader(headerKey, headerValue);
                   
                  /*PdfConverter.getInstance().convert(arg0, arg1, arg2);;
                  PdfConverter.getInstance().convert(document, out, options);
                  */
                  
                  POIFSFileSystem fs = null;  
                  Document document = new Document();

                   try {  
                        
                       fs = new POIFSFileSystem(in);  

                       HWPFDocument doc = new HWPFDocument(fs);  
                       WordExtractor we = new WordExtractor(doc);  

                       

                       PdfWriter writer = PdfWriter.getInstance(document, outStream);  

                       Range range = doc.getRange();
                       document.open();  
                       writer.setPageEmpty(true);  
                       document.newPage();  
                       writer.setPageEmpty(true);  

                       String[] paragraphs = we.getParagraphText();  
                       for (int i = 0; i < paragraphs.length; i++) {  

                           org.apache.poi.hwpf.usermodel.Paragraph pr = range.getParagraph(i);
                          // CharacterRun run = pr.getCharacterRun(i);
                          // run.setBold(true);
                          // run.setCapitalized(true);
                          // run.setItalic(true);
                           paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");  
                       System.out.println("Length:" + paragraphs[i].length());  
                       System.out.println("Paragraph" + i + ": " + paragraphs[i].toString());  

                       // add the paragraph to the document  
                       document.add(new Paragraph(paragraphs[i]));  
                       }  

                   } catch (Exception e) {  
                       e.printStackTrace();  
                   } finally {  
                                   // close the document  
                      document.close();  
                               }  

                  
            		 inputStream.close();
                   outStream.close(); 
         	   
         	   
         	   
			} else{
         	   
         	   if (mimeType == null) {
                    // set to binary type if MIME mapping not found
                    mimeType = "application/octet-stream";
                }
             
                // set content attributes for the response
                response.setContentType(mimeType);
                response.setContentLength((int) downloadFile.length());
         
                // set headers for the response
                response.setHeader("X-Frame-Options", "SAMEORIGIN");
                String headerKey = "Content-Disposition";
                String headerValue = String.format("filename=\"%s\"",
                        downloadFile.getName());
                response.setHeader(headerKey, headerValue);
         
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
         
                // write bytes read from the input stream into the output stream
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
         
            
         	   
            }
        }        	
	 
		 }
	 }
	
	private Boolean validateType(CommonsMultipartFile commonsMultipartFile) {
		
		Boolean flag=false;
		
		String[] fileTypes=new String[]{"application/msword",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
				"application/pdf","text/plain"};
		
		for (String fileType : fileTypes) {
			
			if(fileType.equalsIgnoreCase(commonsMultipartFile.getContentType())){
				flag=true;
				break;
			}else {
				flag=false;
			}
		}
		return flag;
	}
	
	 private static String getFileExtensions(File file) {
	        String fileName = file.getName();
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "";
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
