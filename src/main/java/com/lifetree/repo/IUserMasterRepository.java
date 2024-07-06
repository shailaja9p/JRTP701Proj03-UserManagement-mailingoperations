package com.lifetree.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lifetree.entity.UserMaster;

public interface IUserMasterRepository extends JpaRepository<UserMaster, Integer> {

		public UserMaster findByEmailAndPassword(String email,String password);
		
		public UserMaster findByEmailAndName(String email,String name);
}
