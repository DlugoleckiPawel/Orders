package com.app.service;

import com.app.dto.CustomerDto;
import com.app.exceptions.CustomerException;
import com.app.mappers.ModelMapper;
import com.app.model.Customer;
import com.app.repository.CustomerRepository;
import com.app.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {
	private final CustomerRepository customerRepository;

	public CustomerDto addCustomer(CustomerDto customerDto) {

		CustomerValidator customerValidator = new CustomerValidator();
		Map<String, String> errors = customerValidator.validate(customerDto);
		if (customerValidator.hasErrors()) {
			String errorMessage = errors
					.entrySet()
					.stream()
					.map(e -> e.getKey() + ": " + e.getValue())
					.collect(Collectors.joining(", "));
			throw new CustomerException(errorMessage);
		}

		Customer customer = ModelMapper.fromCustomerDtoToCustomer(customerDto);
		return ModelMapper.fromCustomerToCustomerDto(customerRepository.save(customer));
	}

	public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
		if (id == null) {
			throw new CustomerException("Customer id is null");
		}
		if (customerDto == null) {
			throw new CustomerException("Customer is null");
		}

		Customer customer = customerRepository
				.findById(id)
				.orElseThrow();

		customer.setName(customer.getName() == null ?
				customer.getName() : customerDto.getSurname());
		customer.setSurname(customer.getSurname() == null ?
				customer.getSurname() : customerDto.getSurname());
		customer.setAge(customer.getAge() == null ?
				customer.getAge() : customerDto.getAge());
		customer.setEmail(customer.getEmail() == null ?
				customer.getEmail() : customerDto.getEmail());

		Customer updatedCustomer = customerRepository.save(customer);
		return ModelMapper.fromCustomerToCustomerDto(updatedCustomer);
	}

	public List<CustomerDto> getCustomers() {
		return customerRepository.findAll()
				.stream()
				.map(ModelMapper::fromCustomerToCustomerDto)
				.collect(Collectors.toList());
	}

	public CustomerDto getOneCustomer(Long id) {
		if (id == null) {
			throw new CustomerException("Customer id is null");
		}
		return customerRepository.findById(id)
				.map(ModelMapper::fromCustomerToCustomerDto)
				.orElseThrow();
	}

	public CustomerDto deleteOneCustomer(Long id) {
		if (id == null) {
			throw new CustomerException( "Customer id is null");
		}
		Customer customer = customerRepository.findById(id)
				.orElseThrow();
		customerRepository.deleteById(id);
		return ModelMapper.fromCustomerToCustomerDto(customer);
	}
}
