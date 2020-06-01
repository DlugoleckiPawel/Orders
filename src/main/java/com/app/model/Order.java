package com.app.model;

import com.app.model.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

	private Integer quantity;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate orderDate;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "product_id")
	private Product product;

	private final static long DAYS = 2;
	private final static BigDecimal DAYS_DISCOUNT = new BigDecimal("0.02");
	private final static BigDecimal AGE_DISCOUNT = new BigDecimal("0.03");

	public boolean betweenDates(LocalDate dateFrom, LocalDate dateTo) {
		return orderDate.compareTo(dateFrom) >= 0 && orderDate.compareTo(dateTo) <= 0;
	}

	public BigDecimal totalPrice() {
		return product
				.getPrice()
				.multiply(BigDecimal.valueOf(quantity))
				.multiply(BigDecimal.ONE.subtract(calculateDiscount()));
	}

	private BigDecimal calculateDiscount() {
		if (hasCorrectCustomerAge()) {
			return AGE_DISCOUNT;
		}

		if (hasCorrectRealizationDate()) {
			return DAYS_DISCOUNT;
		}

		return BigDecimal.ZERO;
	}

	private boolean hasCorrectRealizationDate() {
		final long DAYS = 2;
		return Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), orderDate)) <= DAYS;
	}

	private boolean hasCorrectCustomerAge() {
		return customer.getAge() <= 25;
	}
}
