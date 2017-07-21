package com.meettwo.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.meettwo.dto.UserDto;
import com.meettwo.dto.UserSearchDto;
import com.meettwo.model.User;
import com.meettwo.util.MeetTwoUtil;

import jersey.repackaged.com.google.common.collect.Lists;

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

	@Override
	public Map<String, Object> getAllUser(Integer page, Integer size) {
		
		Session session = getSession();
		List<User> list = null;
		Map<String, Object> result = new HashMap<String,Object>();
				
		Query query = session.createQuery("from User");
		
		ScrollableResults resultScroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
		if ((resultScroll.first()) && (resultScroll.scroll(page * size))) {
			list = Lists.newArrayList();
			int i = 0;
			while (size > i++) {
				list.add((User) resultScroll.get(0));
				if (!resultScroll.next())
					break;
			}
			result.put("User", list);
			resultScroll.last();
			if (size != 0) {
				
				Integer totalPages=(resultScroll.getRowNumber() + 1) / size;
				
				if((resultScroll.getRowNumber()+1)%size>0){
					result.put("TotalPages", totalPages+1);	
				}else {
					result.put("TotalPages", totalPages);
				}
				
			} else {
				result.put("TotalPages", "page size zero");
			}
		} else {
			result.put("User", Lists.newArrayList());
			result.put("TotalPages", "no pages available");
		}
		return result;
	}

	@Override
	public Map<String, Object> userSearch(UserSearchDto userSearchDto) throws ParseException {
		
		Session session = getSession();
		List<UserDto> list = null;
		Map<String, Object> result = new HashMap<String,Object>();
		
		StringBuilder queryString = new StringBuilder("select u.emailId as emailId,"
				+"u.createdDate as createdDate,u.isActive as isActive from User u where u.deletedYn=false"
				+" and u.userRoleId !=1");
		
		
		Query query=null;
		if(userSearchDto.getUserSearch() !=null && userSearchDto.getUserSearch().getUserPredicateObject() !=null)
		{
			if(!MeetTwoUtil.isEmptyString(userSearchDto.getUserSearch().getUserPredicateObject().getEmailId()))
			{
				queryString=queryString.append(" and u.emailId like :emailId");
			}
			
          if(userSearchDto.getUserSearch().getUserPredicateObject().getCreatedDate()!=null){
				if(!MeetTwoUtil.isEmptyString(userSearchDto.getUserSearch().getUserPredicateObject().getCreatedDate().getAfter())){
					queryString=queryString.append(" and u.createdDate >=:after " );
				}
				if(!MeetTwoUtil.isEmptyString(userSearchDto.getUserSearch().getUserPredicateObject().getCreatedDate().getBefore())){
					queryString=queryString.append(" and u.createdDate <=:before " );
				}
			}
          
          if(userSearchDto.getUserSearch().getUserPredicateObject().getStatus()!=null){
  			      queryString=queryString.append(" and u.isActive =:status " );
  		   }
          
          if(userSearchDto.getSort()!=null){
				if(!MeetTwoUtil.isEmptyString(userSearchDto.getSort().getPredicate())){
					queryString=queryString.append(" order by "+userSearchDto.getSort().getPredicate());
					if(userSearchDto.getSort().getReverse()){
						queryString=queryString.append(" asc ");
					}else {
						queryString=queryString.append(" desc ");
					}
				}
			}
          
          query=session.createQuery(queryString.toString())
					.setResultTransformer(Transformers.aliasToBean(UserDto.class));
          
          if(!MeetTwoUtil.isEmptyString(userSearchDto.getUserSearch().getUserPredicateObject().getEmailId()))
			{
        	  query.setParameter("emailId", "%"+userSearchDto.getUserSearch().getUserPredicateObject().getEmailId()+"%");
			}
          
          if(userSearchDto.getUserSearch().getUserPredicateObject().getCreatedDate()!=null){
				if(!MeetTwoUtil.isEmptyString(userSearchDto.getUserSearch().getUserPredicateObject().getCreatedDate().getAfter())){
					SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
					 query.setParameter("after", dmyFormat.parse(userSearchDto.getUserSearch().getUserPredicateObject().getCreatedDate().getAfter()));
				}
				if(!MeetTwoUtil.isEmptyString(userSearchDto.getUserSearch().getUserPredicateObject().getCreatedDate().getBefore())){
					SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
					 query.setParameter("before", dmyFormat.parse(userSearchDto.getUserSearch().getUserPredicateObject().getCreatedDate().getBefore()));
				}
			}
          
          if(userSearchDto.getUserSearch().getUserPredicateObject().getStatus()!=null){
        	  query.setParameter("status",userSearchDto.getUserSearch().getUserPredicateObject().getStatus());
		   }
			
	   }else {
		   if(userSearchDto.getSort() !=null){
			   if(!MeetTwoUtil.isEmptyString(userSearchDto.getSort().getPredicate())){
				       queryString=queryString.append(" order by "+userSearchDto.getSort().getPredicate());
				  if(userSearchDto.getSort().getReverse()){
					   queryString=queryString.append(" asc ");
				  }else {
					   queryString=queryString.append(" desc ");
				  }
			}
		}else {
			queryString=queryString.append(" order by createdDate desc ");
		}
		   
		   query=session.createQuery(queryString.toString())
					.setResultTransformer(Transformers.aliasToBean(UserDto.class));
	}
		
		ScrollableResults resultScroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
		if ((resultScroll.first()) && (resultScroll.scroll(userSearchDto.getPagination().getStart()))) {
			list = Lists.newArrayList();
			int i = 0;
			while (userSearchDto.getPagination().getNumber() > i++) {
				list.add((UserDto) resultScroll.get(0));
				if (!resultScroll.next())
					break;
			}
			result.put("User", list);
			resultScroll.last();
			if (userSearchDto.getPagination().getNumber() != 0) {
				
				Integer totalPages=(resultScroll.getRowNumber() + 1) / userSearchDto.getPagination().getNumber();
				
				if((resultScroll.getRowNumber()+1)%userSearchDto.getPagination().getNumber()>0){
					result.put("TotalPages", totalPages+1);	
				}else {
					result.put("TotalPages", totalPages);
				}
				
			} else {
				result.put("TotalPages", "page size zero");
			}
		} else {
			result.put("User", Lists.newArrayList());
			result.put("TotalPages", "no pages available");
		}
		return result;

	}

	@Override
	public void deleteUser(String userId) {
		Session session = getSession();
        session.createQuery("update User set deletedYn=true where emailId=:userId").setParameter("userId", userId).executeUpdate();
	}
	
}
