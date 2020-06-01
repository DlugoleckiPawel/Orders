package com.app.converter;

import com.app.dto.CustomerDto;

import java.util.List;

public class CustomerConverter extends JsonConverter<List<CustomerDto>> {
	public CustomerConverter(String jsonFilename) {
		super(jsonFilename);
	}
}
