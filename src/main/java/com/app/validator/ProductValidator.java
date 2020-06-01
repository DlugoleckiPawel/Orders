package com.app.validator;

import com.app.dto.ProductDto;
import com.app.model.enums.Category;
import com.app.validator.generic.AbstractValidator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class ProductValidator extends AbstractValidator<ProductDto> {

	@Override
	public Map<String, String> validate(ProductDto productDto) {
		errors.clear();

		if (!isNameValid(productDto)) {
			errors.put("Validation error", "Product name must contains only big letter");
		}
		if (!isPriceValid(productDto)) {
			errors.put("Validation error", "Product price must be greater than 0");
		}
		if (!isCategoryValid(productDto)) {
			errors.put("Validation error", "Product category must have A|B|C value");
		}
		return errors;
	}

	private boolean isNameValid(ProductDto productDto) {
		return Objects.nonNull(productDto.getName()) && productDto.getName().matches("[A-Z ]+");
	}

	private boolean isPriceValid(ProductDto productDto) {
		return Objects.nonNull(productDto.getPrice()) && productDto.getPrice().compareTo(BigDecimal.ZERO) >= 0;
	}

	private boolean isCategoryValid(ProductDto productDto) {
		return Objects.nonNull(productDto.getCategory()) &&
				Arrays
						.stream(Category.values())
						.anyMatch(c -> c.equals(productDto.getCategory()));
	}
}
