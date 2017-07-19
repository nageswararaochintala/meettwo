package com.meettwo.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meettwo.constants.MeetTwoConstants;
import com.meettwo.dto.UserSearchDto;
import com.meettwo.service.UserService;
import com.meettwo.util.ServiceStatus;

/**
 * {@link ServerSidePagination} class provides the all pagination related services
 * 
 * @author nageswararao.ch
 * @version 1.0
 * 
 */


@RestController
@RequestMapping("/ServerSidePagination")
public class ServerSidePagination {

	@Autowired
	private UserService userService;
	
 @RequestMapping(value="/allUser/{page}/{size}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)	
 ServiceStatus getAllUsers(@PathVariable("page") Integer page,@PathVariable("size") Integer size){
	 
	 ServiceStatus serviceStatus = new ServiceStatus();
	 
	 if(page==null)
		 page = MeetTwoConstants.DEFAULT_PAGE_NUM;
	 
	 if(size == null)
		 size=MeetTwoConstants.DEFAULT_PAGE_SIZE;
	 
	 serviceStatus.setStatus("success");
	 serviceStatus.setResult(userService.getAllUser(page, size));
	 
	 return serviceStatus;
 }
 
 @RequestMapping(value="/search",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
 ServiceStatus seekerSearch(@RequestBody UserSearchDto userSearchDto){
 	ServiceStatus serviceStatus = new ServiceStatus();
 	try {

 		if (userSearchDto.getPagination()==null
 				|| userSearchDto.getPagination().getStart() == null){
 			userSearchDto.getPagination().setStart(MeetTwoConstants.DEFAULT_PAGE_NUM); 
 		}
 		
 		if (userSearchDto.getPagination()==null
 				|| userSearchDto.getPagination().getNumber() == null) {
 			userSearchDto.getPagination().setNumber(MeetTwoConstants.DEFAULT_PAGE_SIZE);
 		}
 		
 		serviceStatus.setMessage("successfully got the result");;
 		serviceStatus.setStatus("success");
 		serviceStatus.setResult(userService.userSearch(userSearchDto));
 		

 	} catch (Exception e) {
 		e.printStackTrace();
 		serviceStatus.setMessage("failure");
 		serviceStatus.setStatus("failure");
 	}
 	return serviceStatus;
 }
 
	
}
