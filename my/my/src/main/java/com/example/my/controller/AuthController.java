package com.example.my.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my.dto.LoginRequest;
import com.example.my.dto.LoginResponse;
import com.example.my.filter.JwtProvider;
import com.example.my.model.Employee;
import com.example.my.service.EmployeeService;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signUp")
	public ResponseEntity<String> createEmployee(@RequestBody Employee employee){
		employeeService.employeeService(employee);
		return ResponseEntity.status(HttpStatus.CREATED).body("employee has created");
		 
		
	}
	
	@PostMapping("/sigIn")
	public ResponseEntity<LoginResponse> loginEmployee(@RequestBody LoginRequest loginRequest){
		Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails details=(UserDetails) authentication.getPrincipal();
		
		String token=jwtProvider.generateToken(details);
		List<String> roles=details.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
			return  ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(details.getUsername(), roles, token));
	}
	
	
	
}
