package com.lifetree.service;

import java.util.List;

import com.lifetree.bindings.ActivateUser;
import com.lifetree.bindings.LoginCredentials;
import com.lifetree.bindings.RecoverPassword;
import com.lifetree.bindings.UserAccount;

public interface IUserManagementService {

	public String registerUser(UserAccount user)throws Exception;
	public String activateUserAccount(ActivateUser user);
	public String login(LoginCredentials user);
	
	public List<UserAccount> listUsers();
	public UserAccount showUserByUserId(Integer id);
	public UserAccount showUserByEmailAndName(String email,String name);
	
	public String updateUser(UserAccount user);
	public String deleteUserById(Integer id);
	public String changeUserStatus(Integer id,String status);
	public String recoverPassword(RecoverPassword recover)throws Exception;
}
