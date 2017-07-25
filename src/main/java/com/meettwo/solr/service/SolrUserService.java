package com.meettwo.solr.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.meettwo.dto.UserSolrSearchDto;
import com.meettwo.solr.model.SolrUser;

public interface SolrUserService {

	Page<SolrUser> getUsers(UserSolrSearchDto userSolrSearchDto);
}
