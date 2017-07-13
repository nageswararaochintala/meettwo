package com.meettwo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author Nageswara rao.ch
 *
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

/*	Table: user
	---------------

	Column Information
	----------------------

	Field          Type          Collation        Null    Key     Default            Extra           Privileges                       Comment  
	-------------  ------------  ---------------  ------  ------  -----------------  --------------  -------------------------------  ---------
	user_id        int(11)       (NULL)           NO      PRI     (NULL)             auto_increment  select,insert,update,references           
	email_id       varchar(100)  utf8_general_ci  YES     UNI     (NULL)                             select,insert,update,references           
	password       varchar(200)  utf8_general_ci  YES             (NULL)                             select,insert,update,references           
	user_role_id   int(11)       (NULL)           YES     MUL     (NULL)                             select,insert,update,references           
	created_date   timestamp     (NULL)           YES             (NULL)                             select,insert,update,references           
	deleted_yn     tinyint(4)    (NULL)           YES             (NULL)                             select,insert,update,references           
	is_active      tinyint(4)    (NULL)           YES             (NULL)                             select,insert,update,references           
	current_login  timestamp     (NULL)           NO              CURRENT_TIMESTAMP                  select,insert,update,references           
	last_login     timestamp  */
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "password")
	private String password;

	@ManyToOne
	@JoinColumn(name = "user_role_id", updatable = false, insertable = false)
	private UserRole userRole;

	@Column(name = "user_role_id")
	private Long userRoleId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "deleted_yn")
	private Boolean deletedYn;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "current_login")
	private Date currentLogin;

	@Column(name = "last_login")
	private Date lastLogin;
	
	@OneToMany(mappedBy = "user",fetch=FetchType.EAGER)
	@JsonManagedReference
	private List<SubAdminPermission > subAdminPermissions ;
	

	public User() {

	}

	public User(User user) {
		super();
		this.userId = user.userId;
		this.emailId = user.emailId;
		this.password = user.password;
		this.userRole = user.userRole;
		this.userRoleId = user.userRoleId;
		this.createdDate = user.createdDate;
		this.deletedYn = user.deletedYn;
		this.isActive = user.isActive;
		this.currentLogin = user.currentLogin;
		this.lastLogin = user.lastLogin;
		this.subAdminPermissions=user.subAdminPermissions;
	}

	

	public List<SubAdminPermission> getSubAdminPermissions() {
		return subAdminPermissions;
	}

	public void setSubAdminPermissions(List<SubAdminPermission> subAdminPermissions) {
		this.subAdminPermissions = subAdminPermissions;
	}

	public Date getCurrentLogin() {
		return currentLogin;
	}

	public void setCurrentLogin(Date currentLogin) {
		this.currentLogin = currentLogin;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}



	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getDeletedYn() {
		return deletedYn;
	}

	public void setDeletedYn(Boolean deletedYn) {
		this.deletedYn = deletedYn;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", emailId=" + emailId + ", password=" + password + ", userRole=" + userRole
				+ ", userRoleId=" + userRoleId + ", createdDate=" + createdDate + ", deletedYn=" + deletedYn
				+ ", isActive=" + isActive + ", currentLogin=" + currentLogin + ", lastLogin=" + lastLogin + "]";
	}


}