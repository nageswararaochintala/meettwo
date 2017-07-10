package com.meettwo.dao;

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
	
}
