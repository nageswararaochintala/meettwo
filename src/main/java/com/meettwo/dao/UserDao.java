package com.meettwo.dao;

import java.util.List;

import com.meettwo.model.User;

public interface UserDao extends Dao {

	User getUserByEmailId(String emailId);
	List<String> getSubscriptionPermissions(Long userId);
	
}
