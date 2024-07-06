package com.lifetree.ms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lifetree.bindings.ActivateUser;
import com.lifetree.bindings.LoginCredentials;
import com.lifetree.bindings.RecoverPassword;
import com.lifetree.bindings.UserAccount;
import com.lifetree.service.IUserManagementService;

@RestController
@RequestMapping("/user/management")
public class UserManagementOperationsController {

	@Autowired
	private IUserManagementService userService;

	@PostMapping("/save")
	public ResponseEntity<String> registerUser(@RequestBody UserAccount user) throws Exception{
		try {
			String msg = userService.registerUser(user);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate")
	public ResponseEntity<String> activateUser(@RequestBody ActivateUser user){
		try {
			String msg = userService.activateUserAccount(user);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginCredentials user) {
		try {
			String loginMsg = userService.login(user);
			return new ResponseEntity<String>(loginMsg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// public List<UserAccount> listUsers();
	@GetMapping("/report")
	public ResponseEntity<?> getAllUsers() {
		try {
			List<UserAccount> list = userService.listUsers();
			return new ResponseEntity<List<UserAccount>>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/showUserById/{id}")
	public ResponseEntity<?> showUserByUserId(@PathVariable Integer id) {
		try {
			UserAccount userAccount = userService.showUserByUserId(id);
			return new ResponseEntity<UserAccount>(userAccount, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("showUserByEmailAndName/{email}/{name}")
	public ResponseEntity<?> showUsersByMainAndName(@PathVariable String email, @PathVariable String name) {
		try {
			UserAccount userAccount = userService.showUserByEmailAndName(email, name);
			return new ResponseEntity<UserAccount>(userAccount, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// public String updateUser(UserAccount user);
	@PutMapping("/updateUser")
	public ResponseEntity<String> updateUserDetails(@RequestBody UserAccount user) {
		try {
			String msg = userService.updateUser(user);
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// public String deleteUserById(Integer id);
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
		try {
			String msg = userService.deleteUserById(id);
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	// public String changeUserStatus(Integer id,String status);
	@PatchMapping("/changeStatus/{id}/{status}")
	public ResponseEntity<String> changeStatus(@PathVariable Integer id,@PathVariable String status) {
		try {
			String msg = userService.changeUserStatus(id, status);
			return new ResponseEntity<String>(msg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/recoverPassword")
	public ResponseEntity<String> recoverPassword(@RequestBody RecoverPassword password){
		try {
			String resultMsg = userService.recoverPassword(password);
			return new ResponseEntity<String>(resultMsg, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}


