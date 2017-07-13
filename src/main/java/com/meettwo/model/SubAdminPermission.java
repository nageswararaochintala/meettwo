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
@Table(name="sub_admin_permissions")
public class SubAdminPermission implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id",updatable=false,insertable=false)
	@JsonBackReference
	private User user;
	
	@Column(name="user_id")
	private Long userId; 
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="admin_permission_id",updatable=false,insertable=false)
	private AdminPermissions adminPermissions;
	
	@Column(name="admin_permission_id")
	private Long adminPermissionsId; 
	
	
	@Column(name="created_date",updatable=false)
	private Date createdDate;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
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

	

	public AdminPermissions getAdminPermissions() {
		return adminPermissions;
	}


	public void setAdminPermissions(AdminPermissions adminPermissions) {
		this.adminPermissions = adminPermissions;
	}


	public Long getAdminPermissionsId() {
		return adminPermissionsId;
	}


	public void setAdminPermissionsId(Long adminPermissionsId) {
		this.adminPermissionsId = adminPermissionsId;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
}
