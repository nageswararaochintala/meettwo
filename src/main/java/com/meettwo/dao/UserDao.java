package com.meettwo.dao;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.meettwo.dto.UserSearchDto;
import com.meettwo.model.User;

public interface UserDao extends Dao {

	User getUserByEmailId(String emailId);
	List<String> getSubscriptionPermissions(Long userId);
	Map<String, Object> getAllUser(Integer page,Integer size);
	Map<String,Object> userSearch(UserSearchDto userSearchDto) throws ParseException;
}
