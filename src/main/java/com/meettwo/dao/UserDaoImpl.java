package com.meettwo.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.meettwo.model.User;

@Repository
public class UserDaoImpl extends AbstractDao implements UserDao {

	@Override
	public User getUserByEmailId(String emailId) {
		
		Session session=getSession();
		
		return (User)session.createQuery("from User where emailId =:emailId and deletedYn=false")
		.setParameter("emailId",emailId)
		.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSubscriptionPermissions(Long userId) {
		
		Session session=getSession();
		
		return session.createQuery("select  "
				+ " s.subscription.permission "
				+ " from UserSubscriptions s "
				+ " where s.userId =:userId ")
		.setParameter("userId", userId)
		.list();
		
	}
	
}
