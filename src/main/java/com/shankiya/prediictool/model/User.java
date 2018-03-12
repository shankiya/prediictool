package com.shankiya.prediictool.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Bean Class for User
 * 
 * @author madanagopal.nagarajan
 */

@Entity
@Table(name = "userdetail")
public class User implements Serializable {

	private static final long serialVersionUID = -3109157732242241606L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "username")
	private String userName;

	@Column(name = "password")
	private String password;
	
	@Column(name = "firstname")
	private String firstName;
	
	@Column(name = "lastname")
	private String lastName;
	
	@Column(name = "emailid")
	private String emailId;

	@Column(name = "active")
	private Boolean active;
	
	@Transient
	private String errorMsg;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	protected User() {
	}
	
	public User(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public User(String userName, String password,String firstName,String lastName,String emailId, Boolean active) {
		this.userName = userName;
		this.password = password;
		this.firstName=firstName;
		this.lastName=lastName;
		this.emailId=emailId;
		this.active = active;
	}

	@Override
	public String toString() {
		return String.format("User[id=%d, userName='%s', password='%s',firstName='%s',lastName='%s',emailId='%s',active='%b']", id, userName, password, firstName, lastName, emailId, active);
	}
}