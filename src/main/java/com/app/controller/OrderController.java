package com.app.controller;

import com.app.dto.OrderDto;
import com.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
	private final OrderService orderService;

	@PostMapping
	public OrderDto addOrder(@RequestBody OrderDto orderDto) {
		return orderService.addOrder(orderDto);
	}

	@GetMapping
	public List<OrderDto> getOrders() {
		return orderService.getOrders();
	}
}
