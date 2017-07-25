package com.meettwo.solr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meettwo.dto.UserDto;
import com.meettwo.dto.UserSolrSearchDto;
import com.meettwo.service.UserService;
import com.meettwo.solr.model.SolrUser;
import com.meettwo.solr.service.SolrUserService;
import com.meettwo.util.ServiceStatus;

@RestController
@RequestMapping("/solr")
public class SolrUserController {

	@Autowired
	SolrUserService solrUserService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/getUsers",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus getJobSeekerProfile(@RequestBody UserSolrSearchDto userSolrSearchDto){
		
		ServiceStatus serviceStatus =new ServiceStatus();
		
		if(userSolrSearchDto!=null){
			
			try {
				
				Page<SolrUser>  page=solrUserService.getUsers(userSolrSearchDto);
				
				List<SolrUser> users=page.getContent();
				
				List<Long> solrUserIds = 
						users.stream()
					              .map(SolrUser::getUserId)
					              .collect(Collectors.toList());
				
				if(solrUserIds!=null && !solrUserIds.isEmpty()){

					Map<String, Object> result=new HashMap<String ,Object>();
					List<UserDto> usersList=userService.getUserByIds(solrUserIds);
					
					if(usersList!=null && !usersList.isEmpty()){
						
						result.put("users", usersList);
						result.put("totalPages", page.getTotalPages());
					
						serviceStatus.setResult(result);
						serviceStatus.setStatus("success");
						serviceStatus.setMessage("successfuly searched");
						
					}else {
						serviceStatus.setMessage("no users found");
	                    serviceStatus.setStatus("failure");
					}
					
				}else {
					serviceStatus.setMessage("no users found");
                    serviceStatus.setStatus("failure");
					
				}	
				
			} catch (Exception e) {
                e.printStackTrace();
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("failure");
			}
			
		}else {
			serviceStatus.setMessage("invalid payload");
			serviceStatus.setStatus("failure");
		}
		
		
		return serviceStatus;
		
	}
	
}
