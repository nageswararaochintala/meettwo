package com.meettwo.solr.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.meettwo.solr.model.SolrUser;

public interface SolrUserRepository extends SolrCrudRepository<SolrUser, Long> {
	
	Page<SolrUser> getUserByEmailIdLike(String emailId,Pageable pageable);

}
