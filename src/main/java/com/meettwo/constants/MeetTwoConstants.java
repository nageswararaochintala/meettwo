package com.meettwo.constants;

/**
 * {@link MeetTwoConstants} class contains the MeetTwo constants as regular expression
 * for validation and string for roles and keys
 * 
 *  
 *  @Author : Nageswara rao.ch
 *  @Version : 1.0
 */

public class MeetTwoConstants {
	
	//regular expressions 
	public static final String REGEX_PASSWORD ="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	
	public static final String REGEX_EMAIL ="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	public static final String USER_EMAIL  = "^[A-Za-z]+[A-Za-z0-9._]+@[A-Za-z0-9]+(\\.)[A-Za-z.]{2,3}$";
	public static final String USER_PASS  = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{5,15}$";
	public static final String USER_NAME  = "^[A-z ]{1,20}$";
	public static final String USER_MOBILE  = "^[0-9]{10}$";

	
	//MeetTwo Roles
	public static final String ADMIN = "role.admin";
	public static final String SUB_ADMIN = "role.subadmin";
	public static final String USER = "role.user";
	
	// document upload folder 
		public static final String FOLDER_DOC_UPLOAD = "userFileUploadFolder";
		
		/**
		 * THis property specify the default page number for pagination
		 **/
		public static final Integer DEFAULT_PAGE_NUM = 0;

		/**
		 * This property specify the default page size for pagination
		 **/
		public static final Integer DEFAULT_PAGE_SIZE = 10;
	
}
