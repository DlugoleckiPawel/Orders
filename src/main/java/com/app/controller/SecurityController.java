package com.app.controller;

import com.app.dto.InfoDto;
import com.app.dto.RegisterUserDto;
import com.app.security.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
public class SecurityController {
	private final RegistrationService registrationService;

	@PostMapping("/register")
	public ResponseEntity<InfoDto<String>> register(@RequestBody RegisterUserDto registerUserDto) {
		return new ResponseEntity<>(
				InfoDto.<String>builder().data(registrationService.create(registerUserDto) + " created!").build(),
				HttpStatus.CREATED);
	}
}