package com.app.controller;

import com.app.dto.CustomerDto;
import com.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
	private final CustomerService customerService;

	@PostMapping
	public CustomerDto addCustomer(@RequestBody CustomerDto customerDto) {
		return customerService.addCustomer(customerDto);
	}

	@PutMapping("/{id}")
	public CustomerDto updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
		return customerService.updateCustomer(id, customerDto);
	}

	@GetMapping
	public List<CustomerDto> getCustomers() {
		return customerService.getCustomers();
	}

	@GetMapping("/{id}")
	public CustomerDto getOneCustomer(@PathVariable Long id) {
		return customerService.getOneCustomer(id);
	}

	@DeleteMapping("/{id}")
	public CustomerDto deleteOneCustomer(@PathVariable Long id) {
		return customerService.deleteOneCustomer(id);
	}
}
