package com.meettwo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="user_subscriptions")
public class UserSubscriptions implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id",updatable=false,insertable=false)
	@JsonBackReference
	private User user;
	
	@Column(name="user_id")
	private Long userId;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="subscription_id",updatable=false,insertable=false)
	private subscription subscription;
	
	@Column(name="subscription_id")
	private Long subscriptionId; 
	
	
	@Column(name="created_date",updatable=false)
	private Date createdDate;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public subscription getSubscription() {
		return subscription;
	}


	public void setSubscription(subscription subscription) {
		this.subscription = subscription;
	}


	public Long getSubscriptionId() {
		return subscriptionId;
	}


	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	

}
