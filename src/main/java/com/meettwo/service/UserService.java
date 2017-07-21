package com.meettwo.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.meettwo.dto.UserSearchDto;
import com.meettwo.model.User;
import com.meettwo.model.UserProfile;
import com.meettwo.model.UserSubscriptions;

public interface UserService {

	User getUserByEmailId(String emailId);
    void userRegistration(UserProfile userprofile);
    List<String> getSubscriptionPermissions(Long userId);
    void subscribeService(UserSubscriptions userSubscriptions);
    int accessMap();
    
    void uploadFile(CommonsMultipartFile commonsMultipartFile)throws IOException;
    
    Map<String, Object> getAllUser(Integer page,Integer size);
    
    Map<String,Object> userSearch(UserSearchDto userSearchDto)throws ParseException;
    
    void deleteUser(String userId);
}
