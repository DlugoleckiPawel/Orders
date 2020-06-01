package com.app.security.service;

import com.app.dto.RegisterUserDto;
import com.app.exceptions.RegistrationException;
import com.app.mappers.ModelMapper;
import com.app.model.User;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RegistrationService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public Long create(RegisterUserDto registerUserDto) {
		if (Objects.isNull(registerUserDto)) {
			throw new RegistrationException("User can not be null, " + LocalDateTime.now());
		}
		if (userRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
			throw new RegistrationException("User with given name: " + registerUserDto.getUsername() + " already exists, " + LocalDateTime.now());
		}
		if (!Objects.equals(registerUserDto.getPassword(), registerUserDto.getPasswordConfirmation())) {
			throw new RegistrationException("Password and password confirmation must be the same, " + LocalDateTime.now());
		}
		if (Objects.isNull(registerUserDto.getRoles())) {
			throw new RegistrationException("Role can not be null, " + LocalDateTime.now());
		}
		User user = ModelMapper.fromRegisterUserDtoToUser(registerUserDto);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return user.getId();
	}
}
