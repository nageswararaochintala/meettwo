package com.meettwo.util;

/**
 * {@link MeetTwoUtil} class contains common methods
 * 
 * @author nageswararao.ch
 * @version 1.0
 * 
 */

public class MeetTwoUtil {

	/**
	 * this method is used to validate empty or null string
	 * 
	 * @param str
	 *         specify the string that we will be validate
	 *@return {@link Boolean} class's object with true or false
	 * 
	 */
	
	public static Boolean isEmptyString(String str)
	{
		Boolean flag= true;
		if(str !=null){
			String trimedStr = str.trim();
		
		if(trimedStr.length()>0)
		{
			flag = false;
		}
		}
		return flag;
		
	}
	
}
