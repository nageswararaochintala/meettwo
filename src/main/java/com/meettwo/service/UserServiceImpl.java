package com.meettwo.service;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meettwo.dao.UserDao;
import com.meettwo.model.User;
import com.meettwo.model.UserProfile;
import com.meettwo.model.UserSubscriptions;

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

	@Override
	public void userRegistration(UserProfile userprofile) {
		userprofile.getUser().setPassword(passwordEncoder.encode(userprofile.getUser().getPassword()));
		userprofile.setIsActive(true);
		userprofile.setDeletedYn(false);
		userprofile.getUser().setUserRoleId(2l);
		userprofile.getUser().setIsActive(true);
		userprofile.getUser().setDeletedYn(false);
		userDao.save(userprofile);
	}

	@Override
	public List<String> getSubscriptionPermissions(Long userId) {

		return userDao.getSubscriptionPermissions(userId);
	}

	@Override
	public void subscribeService(UserSubscriptions userSubscriptions) {
		userSubscriptions.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		userDao.save(userSubscriptions);
	}

	@Override
	public int accessMap() {
		// TODO Auto-generated method stub
		return 200;
	}
	
}
