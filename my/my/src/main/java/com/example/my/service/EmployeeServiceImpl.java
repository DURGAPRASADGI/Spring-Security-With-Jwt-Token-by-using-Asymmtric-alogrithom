package com.example.my.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.my.dto.LoginRequest;
import com.example.my.model.Employee;
import com.example.my.repository.EmployeeRepository;
import com.example.my.security.CustomerDetailService;

@Service
public class EmployeeServiceImpl  implements EmployeeService{
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	

	@Override
	public void employeeService(Employee employee) {
		// TODO Auto-generated method stub
		employee.setPassword(passwordEncoder.encode(employee.getPassword()));
		
		employeeRepository.save(employee);
		
		
		
	}

	@Override
	public LoginRequest loginEmployee(LoginRequest loginRequest) {
		// TODO Auto-generated method stub
		LoginRequest login=employeeRepository.findByEmailAndPassword(loginRequest.getEmail(),loginRequest.getPassword());
		if(login==null) {
			throw new UsernameNotFoundException("user not found");
		}
		return login;
	}

}
