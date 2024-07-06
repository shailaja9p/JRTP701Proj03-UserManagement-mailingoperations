package com.lifetree.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lifetree.bindings.ActivateUser;
import com.lifetree.bindings.LoginCredentials;
import com.lifetree.bindings.RecoverPassword;
import com.lifetree.bindings.UserAccount;
import com.lifetree.config.AppConfig;
import com.lifetree.constants.AppConstants;
import com.lifetree.entity.UserMaster;
import com.lifetree.repo.IUserMasterRepository;
import com.lifetree.utils.EmailUtils;

@Service
public class UserManagementServiceImpl implements IUserManagementService {

	@Autowired
	private IUserMasterRepository userRepo;
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private Environment env;
	
	private Map<String,String> messages;
	
	public UserManagementServiceImpl(AppConfig properties) {
		messages=properties.getMessages();
		System.out.println("messages::"+this.messages);
	}

	@Override
	public String registerUser(UserAccount user) throws Exception{
		UserMaster master = new UserMaster();
		BeanUtils.copyProperties(user, master);
		String tempPwd=generateRandomPassword(6);
		master.setPassword(tempPwd);
		master.setActive_sw("InActive");
		UserMaster savedUser= userRepo.save(master);
		// TODO  :: send email.
		String subject="User Registartion success";
		String body=readEmailBody(env.getProperty("mailbody.registeruser.location"),user.getName(),tempPwd);
		emailUtils.sendEmailMessage(user.getEmail(), subject, body);
		return savedUser!=null?savedUser.getUserId()+" "+messages.get(AppConstants.saveMsg):messages.get(AppConstants.failMsg);
	}

	/*@Override
	public String activateUserAccount(ActivateUser user) {
		UserMaster master= new UserMaster();
		master.setEmail(user.getEmail());
		master.setPassword(user.getTempPassword());
		
		Example<UserMaster> example=Example.of(master);
		List<UserMaster> list = userRepo.findAll(example);
		
		if(list.size()!=0){
			UserMaster entity=list.get(0);
			entity.setPassword(user.getNewPassword());
			entity.setActive_sw("Active");
			UserMaster updatedEntity = userRepo.save(entity);
			return messages.get(AppConstants.activatesuccess);
		}
		return messages.get(AppConstants.activatefailure);
	}*/
	@Override
	public String activateUserAccount(ActivateUser user) {
			
		UserMaster entity = userRepo.findByEmailAndPassword(user.getEmail(), user.getTempPassword());
		if(entity==null) {
			return messages.get(AppConstants.activatefailure);
		}else {
			entity.setPassword(user.getConfirmPassword());
			entity.setActive_sw("Active");
			UserMaster updatedEntity = userRepo.save(entity);
			return messages.get(AppConstants.activatesuccess);
		}
	}

	@Override
	public String login(LoginCredentials user) {
		UserMaster master= new UserMaster();
		master.setEmail(user.getEmail());
		master.setPassword(user.getPassword());
	
		Example<UserMaster> example=Example.of(master);
		List<UserMaster> list = userRepo.findAll(example);
		if(list.size()!=0) {
			UserMaster entity = list.get(0);
			return	 entity.getActive_sw().equalsIgnoreCase("Active")?messages.get(AppConstants.loginsuccess):messages.get(AppConstants.inactive);
		}
		return messages.get(AppConstants.loginfailure);
	}

	@Override
	public List<UserAccount> listUsers() {
		List<UserMaster> list = userRepo.findAll();
		
		List<UserAccount> listUsers = list.stream().map(entity->{
			UserAccount user= new UserAccount();
			BeanUtils.copyProperties(entity, user);
			return user;
		}).toList();
		
		return listUsers;
	}

	@Override
	public UserAccount showUserByUserId(Integer id) {
		Optional<UserMaster> opt = userRepo.findById(id);
		UserAccount account = null;
		if(opt.isPresent()) {
			 account = new UserAccount();
			BeanUtils.copyProperties(opt.get(), account);
		}
		return account;
	}

	/*@Override
	public UserAccount showUserByEmailAndName(String email, String name) {
		UserMaster user= new UserMaster();
		user.setEmail(email);
		user.setName(name);
		
		Example<UserMaster> example= Example.of(user);
		List<UserMaster> list = userRepo.findAll(example);
		if(list.size()==0) {
			return null;
		}else {
			UserMaster entity = list.get(0);
			UserAccount account= new UserAccount();
			BeanUtils.copyProperties(entity, account);
			return account;
		}
	}*/
	@Override
	public UserAccount showUserByEmailAndName(String email, String name) {
		UserMaster master = userRepo.findByEmailAndName(email,name);
		UserAccount account=null;
		if(master!=null) {
			account= new UserAccount();
			BeanUtils.copyProperties(master, account);
		}
		return account;
	}

	@Override
	public String updateUser(UserAccount user) {
		 Optional<UserMaster> opt = userRepo.findById(user.getUserId());
		if(opt.isPresent()) {
			UserMaster entity=opt.get();
			BeanUtils.copyProperties(user,entity);
			userRepo.save(entity);
			return messages.get(AppConstants.updateSuccess);
		}
		return  messages.get(AppConstants.updateFailure);
	}

	@Override
	public String deleteUserById(Integer id) {
		Optional<UserMaster> opt = userRepo.findById(id);
		if (opt.isPresent()) {
			userRepo.deleteById(id);
			return "user is deleted";
		}
		return "user is not found for deletion";
	}

	@Override
	public String changeUserStatus(Integer id, String status) {
		Optional<UserMaster> opt = userRepo.findById(id);
		if (opt.isPresent()) {
			UserMaster master = opt.get();
			master.setActive_sw(status);
			userRepo.save(master);
			return "user status is changed";
		}
		return "user not found for chnging status";
	}

	@Override
	public String recoverPassword(RecoverPassword recover)throws Exception {
		UserMaster master = userRepo.findByEmailAndName(recover.getEmail(), recover.getName());
		if(master!=null) {
			String pwd= master.getPassword();
			//TODO:: send the recover password to email acocunt
			String subject="Mail for password recovery";
			String mailBody=readEmailBody(env.getProperty("mailbody.recoverpwd.location"), recover.getName(), pwd);
			emailUtils.sendEmailMessage(recover.getEmail(), subject, mailBody);
			return pwd;
		}
		return "email and name is not found";
	}

	private String generateRandomPassword(int length)
	{
		// a list of characters to choose from in form of a string
		String alphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
		// creating a StringBuffer size of AlphaNumericStr
		StringBuilder randomWord = new StringBuilder(length);
		int i;
		for (i = 0; i < length; i++) {
			// generating a random number using math.random()
			int ch = (int) (alphaNumericStr.length() * Math.random());
			// adding Random character one by one at the end of s
			randomWord.append(alphaNumericStr.charAt(ch));
		}
		return randomWord.toString();
	}

	private String readEmailBody(String fileName,String fullName, String pwd) throws Exception{
		String mailBody=null;
		String url="http://localhost:4041/activate";
		try(	FileReader reader=new FileReader(fileName);
				BufferedReader br= new BufferedReader(reader)) {
			// Read file content to stringBuffer obj line by line.
			//FileReader reader=new FileReader(fileName);
			// when i want to read content line by line by keeping content in Buffer
			//BufferedReader br= new BufferedReader(reader);
			// read line by line and want to append
			StringBuffer buffer= new StringBuffer();
			String line=null;
			do {
				line=br.readLine();
				if(line!=null)
				buffer.append(line);
			}while(line!=null);
			mailBody=buffer.toString();
			mailBody=mailBody.replace("{FULL-NAME}", fullName).replace("{PWD}", pwd).replace("{URL}",url);
			System.out.println("=============================="+mailBody);
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mailBody;
	}

}
