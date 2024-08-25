package com.example.my.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.my.dto.LoginRequest;
import com.example.my.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByEmail(String username);

	LoginRequest findByEmailAndPassword(String email, String password);

}
