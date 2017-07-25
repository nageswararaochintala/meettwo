package com.meettwo.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.meettwo.constants.MeetTwoConstants;
import com.meettwo.dao.UserDao;
import com.meettwo.dto.UserDto;
import com.meettwo.dto.UserSearchDto;
import com.meettwo.model.User;
import com.meettwo.model.UserProfile;
import com.meettwo.model.UserSubscriptions;
import com.meettwo.util.MeetTwoUtil;

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

	@Override
	public void uploadFile(CommonsMultipartFile commonsMultipartFile) throws IOException {
		
		if(commonsMultipartFile!=null && !commonsMultipartFile.isEmpty()){
			Map<String , Object> uploadStatus=null;
			uploadStatus = MeetTwoUtil.uploadFiles(commonsMultipartFile,MeetTwoConstants.FOLDER_DOC_UPLOAD, "user");
			if((Integer)(uploadStatus.get("uploadCount"))!=null 
					&& (Integer)(uploadStatus.get("uploadCount"))>0){
			}else{
				throw new IOException();
			}
			
		}
		
	}

	@Override
	public Map<String, Object> getAllUser(Integer page, Integer size) {
		return userDao.getAllUser(page, size);
	}

	@Override
	public Map<String, Object> userSearch(UserSearchDto userSearchDto)throws ParseException {
		return userDao.userSearch(userSearchDto);
	}

	@Override
	public void deleteUser(String userId) {
		userDao.deleteUser(userId);
	}

	@Override
	public List<UserDto> getUserByIds(List<Long> ids) {
		return userDao.getUserByIds(ids);
	}
	
}
