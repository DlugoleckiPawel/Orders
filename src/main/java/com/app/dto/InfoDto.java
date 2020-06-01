package com.app.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InfoDto<T> {
	T data;
	String error;
}
