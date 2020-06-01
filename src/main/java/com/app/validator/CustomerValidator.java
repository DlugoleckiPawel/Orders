package com.app.validator;

import com.app.dto.CustomerDto;
import com.app.validator.generic.AbstractValidator;

import java.util.Map;
import java.util.Objects;

public class CustomerValidator extends AbstractValidator<CustomerDto> {

	@Override
	public Map<String, String> validate(CustomerDto customerDto) {
		errors.clear();

		if (!isNameValid(customerDto)) {
			errors.put("Validation error", "Customer name must contains only big letter");
		}
		if (!isSurnameValid(customerDto)) {
			errors.put("Validation error", "Customer surname must contains only big letter");
		}
		if (!isAgeValid(customerDto)) {
			errors.put("Validation error", "Customer age must more than 18");
		}
		if (!isEmailValid(customerDto)) {
			errors.put("Validation error", "Customer email must have......... ");
		}
		return errors;
	}

	private boolean isNameValid(CustomerDto customerDto) {
		return Objects.nonNull(customerDto.getName()) && customerDto.getName().matches("[A-Z ]+");
	}

	private boolean isSurnameValid(CustomerDto customerDto) {
		return Objects.nonNull(customerDto.getSurname()) && customerDto.getSurname().matches("[A-Z ]+");
	}

	private boolean isAgeValid(CustomerDto customerDto) {
		return Objects.nonNull(customerDto.getAge()) && customerDto.getAge() >= 18;
	}

	private boolean isEmailValid(CustomerDto customerDto) {
		return Objects.nonNull(customerDto.getEmail());
	}
}
