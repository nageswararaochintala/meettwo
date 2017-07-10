package com.meettwo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meettwo.dao.UserDao;
import com.meettwo.model.User;

@Service("userService")
@Transactional("transactionManager")
public class UserServiceImpl implements UserService {

	@Autowired 
	private UserDao userDao;
	
	@Autowired 
	PasswordEncoder passwordEncoder;


	@Autowired
	Environment environment;
	
	@Override
	public User getUserByEmailId(String emailId) {
		 User user =userDao.getUserByEmailId(emailId);
		 Hibernate.initialize(user);
		 return user;
	}
	
	
}
