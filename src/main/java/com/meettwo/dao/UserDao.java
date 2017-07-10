package com.meettwo.dao;

import com.meettwo.model.User;

public interface UserDao extends Dao {

	User getUserByEmailId(String emailId);
	
}
