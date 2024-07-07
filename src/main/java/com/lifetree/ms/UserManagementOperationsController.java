package com.lifetree.ms;

import java.time.LocalDateTime;
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

import com.lifetree.advice.ErrorDetails;
import com.lifetree.advice.UserNotFoundException;
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
	public ResponseEntity<String> registerUser(@RequestBody UserAccount user) throws Exception {
		try {
			String msg = userService.registerUser(user);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
		} /*catch (UserNotFoundException e) {
		System.out.println("UserManagementOperationsController.registerUser()");
			ErrorDetails details= new ErrorDetails(LocalDateTime.now(), e.getMessage(), "404-Unable to register");
			return new ResponseEntity<ErrorDetails>(details,HttpStatus.NOT_FOUND);}*/
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate")
	public ResponseEntity<String> activateUser(@RequestBody ActivateUser user) {
			String msg = userService.activateUserAccount(user);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginCredentials user) {
			String loginMsg = userService.login(user);
			return new ResponseEntity<String>(loginMsg, HttpStatus.OK);
		}

	// public List<UserAccount> listUsers();
	@GetMapping("/report")
	public ResponseEntity<?> getAllUsers() {
			List<UserAccount> list = userService.listUsers();
			return new ResponseEntity<List<UserAccount>>(list, HttpStatus.OK);
		}

	@GetMapping("/showUserById/{id}")
	public ResponseEntity<UserAccount> showUserByUserId(@PathVariable Integer id) {
		// try {
		UserAccount userAccount = userService.showUserByUserId(id);
		return new ResponseEntity<UserAccount>(userAccount, HttpStatus.OK);
		/*
		 * } catch (Exception e) { e.printStackTrace(); return new
		 * ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
		 */
	}

	@GetMapping("showUserByEmailAndName/{email}/{name}")
	public ResponseEntity<UserAccount> showUsersByMainAndName(@PathVariable String email, @PathVariable String name) {
		// try {
		UserAccount userAccount = userService.showUserByEmailAndName(email, name);
		return new ResponseEntity<UserAccount>(userAccount, HttpStatus.OK);
		/*
		 * } catch (Exception e) { e.printStackTrace(); return new
		 * ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
		 */
	}

	@PutMapping("/updateUser")
	public ResponseEntity<String> updateUserDetails(@RequestBody UserAccount user) {
		String msg = userService.updateUser(user);
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
		String msg = userService.deleteUserById(id);
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}

	@PatchMapping("/changeStatus/{id}/{status}")
	public ResponseEntity<String> changeStatus(@PathVariable Integer id, @PathVariable String status) {
		String msg = userService.changeUserStatus(id, status);
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}

	@PostMapping("/recoverPassword")
	public ResponseEntity<?> recoverPassword(@RequestBody RecoverPassword password) {
		try {
			String resultMsg = userService.recoverPassword(password);
			return new ResponseEntity<String>(resultMsg, HttpStatus.OK);
		}catch(UserNotFoundException unf) {
			System.out.println("UserManagementOperationsController.recoverPassword()");
			ErrorDetails details= new ErrorDetails(LocalDateTime.now(),unf.getMessage(),"404-User Not Found to recover password");
			return new ResponseEntity<ErrorDetails>(details,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
