package com.meettwo.service;

import java.util.List;

import com.meettwo.model.User;
import com.meettwo.model.UserProfile;
import com.meettwo.model.UserSubscriptions;

public interface UserService {

	User getUserByEmailId(String emailId);
    void userRegistration(UserProfile userprofile);
    List<String> getSubscriptionPermissions(Long userId);
    void subscribeService(UserSubscriptions userSubscriptions);
    int accessMap();
    
}
