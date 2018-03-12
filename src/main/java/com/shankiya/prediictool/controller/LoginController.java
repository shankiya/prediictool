package com.shankiya.prediictool.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shankiya.prediictool.model.User;
import com.shankiya.prediictool.repo.UserRepositry;

/**
 * Controller to retrieve User Information.
 * 
 * @author madanagopal.nagarajan
 *
 */
@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	UserRepositry repository;

	/**
	 * Retrieve all user details
	 */
	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	public List<User> getAll() {
		List<User> list = new ArrayList<>();
		Iterable<User> userDetails = repository.findAll();
		userDetails.forEach(list::add);
		return list;
	}

	/**
	 * Retrieve certification request based on username and password
	 */
	@RequestMapping(value = "/userDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public User findByUserDetails(@RequestBody User user) {
		System.out.println("user");
		System.out.println("user..." + user);
		User userDetails = repository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
		return userDetails;
	}

	/**
	 * Retrieve certification request based on username and password
	 */
	@RequestMapping(value = "/user/{userName}", method = RequestMethod.GET)
	public User findByUserDetails(@PathVariable final String userName) {

		System.out.println("user");
		System.out.println("user..." + userName);
		User userDetails = repository.findByUserName(userName);
		return userDetails;
	}

	/**
	 * Retrieve certification request based on username and password
	 */
	/*
	 * @RequestMapping(value = "/registerUser", method = RequestMethod.PUT,
	 * produces=MediaType.APPLICATION_JSON_VALUE) public User checkUser(@RequestBody
	 * User user) {
	 * 
	 * System.out.println("user"); System.out.println("user..."+user);
	 * 
	 * User userDetails = repository.findByUserName(user.getUserName()); return
	 * userDetails; }
	 */
/*
	@PostMapping("/registerUser")
	public User registerUser(@RequestBody User user) {

		User userDetails = repository.findByUserName(user.getUserName());
		System.out.println("userDetails" + userDetails);
		if (userDetails == null) {
			return repository.save(user);
		}
		return null;
	}*/
	
	@PostMapping("/registerUser")
	public  User registerUser(@RequestBody User user) {

		User userDetails = repository.findByUserName(user.getUserName());

		if (userDetails == null) {
			System.out.println("save 2");
			User emailUserDetails = repository.findByEmailId(user.getEmailId());
			if(emailUserDetails == null ) {
				System.out.println("save");
				return repository.save(user);
			}else {
				System.out.println(emailUserDetails.getUserName()+emailUserDetails.getEmailId());
				user.setErrorMsg("EmailId is already exist"); 
				user.setEmailId(""); 
				return user;
			}
		}else {
			System.out.println(userDetails.getUserName()+userDetails.getEmailId());
				user.setErrorMsg("UserName is already exist"); 
				user.setUserName(""); 
				return user;
		}
	}


}