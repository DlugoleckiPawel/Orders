package com.app.service;

import com.app.dto.CustomerDto;
import com.app.dto.OrderDto;
import com.app.dto.ProductDto;
import com.app.exceptions.OrderException;
import com.app.mappers.ModelMapper;
import com.app.model.Order;
import com.app.repository.CustomerRepository;
import com.app.repository.OrderRepository;
import com.app.repository.ProductRepository;
import com.app.validator.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;

	public OrderDto addOrder(OrderDto orderDto) {
		OrderValidator orderValidator = new OrderValidator();
		Map<String, String> errors = orderValidator.validate(orderDto);
		if (orderValidator.hasErrors()) {
			String errorMessage = errors
					.entrySet()
					.stream()
					.map(e -> e.getKey() + ": " + e.getValue())
					.collect(Collectors.joining(", "));
			throw new OrderException(errorMessage);
		}

		ProductDto productDto = ModelMapper.fromProductToProductDto(productRepository
				.findById(orderDto.getProductDto().getId())
				.orElseThrow(NullPointerException::new));

		CustomerDto customerDto = ModelMapper.fromCustomerToCustomerDto(customerRepository
				.findById(orderDto.getCustomerDto().getId())
				.orElseThrow(NullPointerException::new));

		Order order = ModelMapper.fromOrderDtoToOrder(orderDto);
		order.setProduct(ModelMapper.fromProductDtoToProduct(productDto));
		order.setCustomer(ModelMapper.fromCustomerDtoToCustomer(customerDto));

		return ModelMapper.fromOrderDtoToOrder(orderRepository.save(order));
	}

	public List<OrderDto> getOrders() {
		return orderRepository.findAll()
				.stream()
				.map(ModelMapper::fromOrderDtoToOrder)
				.collect(Collectors.toList());
	}
}
