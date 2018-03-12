package com.shankiya.prediictool.repo;

import org.springframework.data.repository.CrudRepository;

import com.shankiya.prediictool.model.User;


/**
 * Repository to do the CRUD operations on certification.
 * 
 * @author madanagopal.nagarajan
 *
 */

public interface UserRepositry extends CrudRepository<User, String> {
	User findByUserName(String userName);
	User findByEmailId(String emailId);
	User findByUserNameAndPassword(String userName,String password);

}
