package com.meettwo.solr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Service;

import com.meettwo.dto.UserSolrSearchDto;
import com.meettwo.solr.model.SolrUser;
import com.meettwo.solr.repositories.SolrUserRepository;

@Service("solrUserService")
public class SolrUserServiceImpl implements SolrUserService {

	@Autowired
	SolrTemplate solrTemplateUser;
	
	@Autowired
	SolrUserRepository solrUserRepository;
	
	@Override
	public Page<SolrUser> getUsers(UserSolrSearchDto userSolrSearchDto) {

	
		if(userSolrSearchDto.getPage()==null){
			userSolrSearchDto.setPage(0);	
		}
		if(userSolrSearchDto.getSize()==null){
			userSolrSearchDto.setSize(10);
		}
		
		Pageable pageable = new PageRequest(userSolrSearchDto.getPage(),userSolrSearchDto.getSize());
		
		Criteria criteria=createSearchCriteria(userSolrSearchDto);
		
		Query query=new SimpleQuery(criteria, pageable).addProjectionOnField(new Field() {
			
			@Override
			public String getName() {
				
				return "userId";
			}
		}) ;
		
		Page<SolrUser> page = solrTemplateUser.queryForPage(query, SolrUser.class);

		return page;
		
	}

	private Criteria createSearchCriteria(UserSolrSearchDto userSolrSearchDto) {
		Criteria criteria = null;
        	     
		//criteria=new Criteria("userId").greaterThanEqual(1l);
		
        if(userSolrSearchDto.getEmailId()!=null){
        	
        	//criteria = new Criteria("emailId").contains(userSolrSearchDto.getEmailId());
        	criteria = new Criteria("emailId").is(userSolrSearchDto.getEmailId()).or("emailId").contains(userSolrSearchDto.getEmailId());
        }
        
        return criteria;
	}

}
