package com.lifetree.bindings;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

	private Integer userId;
	private String name;
	private String  email;
	private Long mobileNo;
	private String  gender="Female";
	private LocalDateTime dob=LocalDateTime.now();
	private Long aadharNo;
}
