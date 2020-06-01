package com.app.model;

import com.app.model.base.BaseEntity;
import com.app.model.enums.Category;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
	private String name;
	private BigDecimal price;

	@Enumerated(EnumType.STRING)
	private Category category;

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "product")
	private Set<Order> orders;
}
