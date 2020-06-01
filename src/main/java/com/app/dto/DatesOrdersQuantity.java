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
public class DatesOrdersQuantity {
	private LocalDate minDate;
	private Integer minOrdersQuantity;
	private LocalDate maxDate;
	private Integer maxOrdersQuantity;
}
