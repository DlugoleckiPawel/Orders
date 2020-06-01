package com.app.model;

import com.app.model.base.BaseEntity;
import com.app.model.enums.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	private String username;
	private String password;

	@Enumerated(EnumType.STRING)
	private Role roles;
}
