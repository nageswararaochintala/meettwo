package com.meettwo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Nageswara rao.ch
 *
 */

@Entity
@Table(name="user_role")
public class UserRole implements Serializable {

	
/*	Table: user_role
	--------------------

	Column Information
	----------------------

	Field         Type          Collation        Null    Key     Default            Extra           Privileges                       Comment  
	------------  ------------  ---------------  ------  ------  -----------------  --------------  -------------------------------  ---------
	user_role_id  int(11)       (NULL)           NO      PRI     (NULL)             auto_increment  select,insert,update,references           
	name          varchar(50)   utf8_general_ci  YES             (NULL)                             select,insert,update,references           
	description   varchar(200)  utf8_general_ci  YES             (NULL)                             select,insert,update,references           
	created_date  timestamp     (NULL)           YES             (NULL)                             select,insert,update,references           
	updated_date  timestamp     (NULL)           NO              CURRENT_TIMESTAMP                  select,insert,update,references           
	deleted_yn    tinyint(4)    (NULL)           YES             (NULL)                             select,insert,update,references           
	is_active     tinyint(4)  */
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="user_role_id")
	private Long userRoleId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="created_date",updatable=false)
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="deleted_yn")
	private Boolean deletedYn;
	
	@Column(name="is_active")
	private Boolean isActive;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserRole [userRoleId=" + userRoleId + ", name=" + name + ", description=" + description
				+ ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", deletedYn=" + deletedYn
				+ ", isActive=" + isActive + "]";
	}

	
	
}
