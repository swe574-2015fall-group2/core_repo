package edu.boun.swe74.pinkelephant.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_user")
public class User extends PersistentObject{

	private static final long serialVersionUID = -3662097184007883933L;

	@Column(name ="username")
    private String username;
	
	@Column(name ="password")
    private String password;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creationTime")
	private Date creationTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updateTime")
	private Date updateTime;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Date getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
}