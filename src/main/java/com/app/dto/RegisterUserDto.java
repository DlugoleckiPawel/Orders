package com.app.dto;

import com.app.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserDto {
	private String username;
	private String password;
	private String passwordConfirmation;
	private Role roles;
}
