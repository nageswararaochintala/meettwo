package com.meettwo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.meettwo.spring.config.MeetTwoConfig;

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
	
	/**
	 * this method is used to compare given string matches with given regex or not
	 * @param regexStr and str
	 * 
	 * @return {@link Boolean}
	 */
	
	public static Boolean isRegexTrue(String regexStr , String str)
	{
		if(!isEmptyString(str)){
			Pattern pattern =Pattern.compile(regexStr);
			Matcher matcher = pattern.matcher(str);
			return matcher.matches();
			
		}else{
			return false;
		}
		
	}
	
	/**
	 * this method is used to write files in external file directory
	 * 
	 *@param content
	 *       specify the file content
	 *@param fileDirectory
	 *       specify the file directory          
	 *@param fileName
	 *       specify the file name        
	 * @throws IOException specify exception that may occur during file writing
	 * 
	 */
	
	public static void writeFiles(byte[] content,String fileDirectory,String fileName)throws IOException
	{
		File file = new File(fileDirectory);
		if(!file.exists())
			file.mkdirs();
		file = new File(fileDirectory + File.separator +fileName);
		if(!file.exists())
			file.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(file);
		
		fos.write(content);
		fos.flush();
		fos.close();
	}
	
	/**
	 * this method is used to read file content as a string 
	 * @param path
	 *        specify the path of the file to be read
	 *@return {@link String} specify the content of the file       
	 *          
	 */
	
	public static String readFile(String path)
	{
		Path filepath;
		try{
			filepath = Paths.get(path);
		}
		catch (Exception e) {
			return null;
		}
		
		byte[] data;
		try{
			if(Files.exists(filepath))
			{
				data = Files.readAllBytes(filepath);
				String base64String = Base64.getEncoder().encodeToString(data);
				return base64String;
			}
			
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * this method is used to read bytes from given path
	 * 
	 * @param path 
	 *        specify the path of the file
	 * @return byte[] specify the content in bytes
	 * 
	 */
	public static byte[] readFileBytes(String path){
		Path filepath = Paths.get(path);
		
		byte[] data;
		try{
			if(Files.exists(filepath))
			{
				data = Files.readAllBytes(filepath);
				return data;
			}
			
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return null;
	}
	
	public static Map<String , Object> uploadFiles(CommonsMultipartFile fileparts,String fileDirectory ,String fileName) throws IOException
	{
		 Map<String, Object> uploadStatus = new HashMap();
		 Integer uploadCount = 0;
		 String materialUrls = "";
		
		 byte[] materialBytes = fileparts.getBytes();
	      String materialFileName = fileparts.getOriginalFilename();
	      materialFileName=fileName+"_"+materialFileName;
	      String materialDirectory = MeetTwoConfig.getInstance().getConfigValue(fileDirectory);
	      
	      if ((materialDirectory != null) && 
	        (!isEmptyString(materialDirectory)))
	      {
	      //  materialDirectory = materialDirectory;
	        
	        writeFiles(materialBytes, materialDirectory, materialFileName);
	        materialUrls = materialDirectory + materialFileName ;
	        uploadCount =uploadCount+1;
	      }
	      else {
	        uploadStatus.put("error", "directory not found");
	      }
	    uploadStatus.put("materialUrl", materialUrls);
	    uploadStatus.put("uploadCount", uploadCount);
	    return uploadStatus;
	}
	
	public static String fileExtention(String fileName){
		
		    if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".")+1);
			else return "";
	}
	
	
}
