package com.lifetree.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="JRTP_USER_MASTER")
@NoArgsConstructor
@AllArgsConstructor
public class UserMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // identity means auto increment. 
	//whenever you add values to other columns, the value to id column will come automatically
	//start with 1 increment by 1
	private Integer userId;
	
	@Column(length = 20)
	private String name;
	@Column(length = 20)
	private String password;
	@Column(length = 30,unique = true,nullable = false)
	private String email;
	private Long mobileNo;
	private Long aadharNo;
	@Column(length = 10)
	private String active_sw;
	@Column(length = 10)// default length is 255
	private String gender;
	private LocalDateTime dob;
	
	@Column(updatable = false,insertable = true)
	@CreationTimestamp()
	private LocalDateTime createdOn;
	
	@Column(updatable = true,insertable = false)
	@UpdateTimestamp
	private LocalDateTime updatedOn;
	
	@Column(length = 20)
	private String createdBy;
	@Column(length = 20)
	private String updatedBy;
}
