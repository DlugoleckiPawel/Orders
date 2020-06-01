package com.app.model;

import com.app.model.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

	private String name;
	private String surname;
	private Integer age;
	private String email;

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "customer")
	private Set<Order> orders;

}
