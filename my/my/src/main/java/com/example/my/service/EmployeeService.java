package com.example.my.service;

import com.example.my.dto.LoginRequest;
import com.example.my.model.Employee;

public interface EmployeeService {

	void employeeService(Employee employee);

	LoginRequest loginEmployee(LoginRequest loginRequest);

}