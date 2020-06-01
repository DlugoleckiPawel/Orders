package com.app.validator;

import com.app.dto.OrderDto;
import com.app.validator.generic.AbstractValidator;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class OrderValidator extends AbstractValidator<OrderDto> {
	@Override
	public Map<String, String> validate(OrderDto orderDto) {
		errors.clear();

		if (!isCustomerValid(orderDto)) {
			errors.put("Validation error", "Customer field can not be null");
		}
		if (!isProductValid(orderDto)) {
			errors.put("Validation error", "Product field can not be null");
		}
		if (!isQuantityValid(orderDto)) {
			errors.put("Validation error", "The quantity of product ordered must be a positive number");
		}
		if (!isRealizationDate(orderDto)) {
			errors.put("Validation error", "The order realization date must be today or from the future;");
		}
		return errors;
	}

	private boolean isCustomerValid(OrderDto orderDto) {
		return Objects.nonNull(orderDto.getCustomerDto());
	}

	private boolean isProductValid(OrderDto orderDto) {
		return Objects.nonNull(orderDto.getProductDto());
	}

	private boolean isQuantityValid(OrderDto orderDto) {
		return Objects.nonNull(orderDto.getQuantity()) && orderDto.getQuantity() > 0;
	}

	private boolean isRealizationDate(OrderDto orderDto) {
		return Objects.nonNull(orderDto.getOrderDate()) && orderDto.getOrderDate().isAfter(LocalDate.now());
	}
}
