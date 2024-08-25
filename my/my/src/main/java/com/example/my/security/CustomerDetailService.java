package com.example.my.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.my.model.Employee;
import com.example.my.repository.EmployeeRepository;
@Service

public class CustomerDetailService implements UserDetailsService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Employee employee=employeeRepository.findByEmail(username);
		
		if(username==null) {
			throw new UsernameNotFoundException("user not found");
		}
		
		return new User(employee.getUsername(), employee.getPassword(), Collections.singleton(()->employee.getRole()));
	}

}
