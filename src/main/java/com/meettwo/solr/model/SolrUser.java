package com.meettwo.solr.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName="user")
public class SolrUser implements ISolrUser {
 
	private @Id @Indexed(USERID) Long userId;
	private @Indexed(EMAILID) List<String> emailId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<String> getEmailId() {
		return emailId;
	}
	public void setEmailId(List<String> emailId) {
		this.emailId = emailId;
	}
	
	@Override
	public String toString() {
		return "SolrUser [userId=" + userId + ", emailId=" + emailId + "]";
	}
	
}
