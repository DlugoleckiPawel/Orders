package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
	private Long id;
	private Integer quantity;
	private LocalDate orderDate;
	private ProductDto productDto;
	private CustomerDto customerDto;
}
