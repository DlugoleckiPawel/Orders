package com.app.mappers;

import com.app.dto.*;
import com.app.model.Customer;
import com.app.model.Order;
import com.app.model.Product;
import com.app.model.User;

import java.util.HashSet;

public interface ModelMapper {

	static ProductDto fromProductToProductDto(Product product) {
		return product == null ? null : ProductDto.builder()
				.id(product.getId())
				.name(product.getName())
				.price(product.getPrice())
				.category(product.getCategory())
				.build();
	}

	static Product fromProductDtoToProduct(ProductDto productDto) {
		return productDto == null ? null : Product.builder()
				.id(productDto.getId())
				.name(productDto.getName())
				.price(productDto.getPrice())
				.category(productDto.getCategory())
				.orders(new HashSet<>())
				.build();
	}

	static CustomerDto fromCustomerToCustomerDto(Customer customer) {
		return customer == null ? null : CustomerDto.builder()
				.id(customer.getId())
				.name(customer.getName())
				.surname(customer.getSurname())
				.age(customer.getAge())
				.email(customer.getEmail())
				.build();
	}

	static Customer fromCustomerDtoToCustomer(CustomerDto customerDto) {
		return customerDto == null ? null : Customer.builder()
				.id(customerDto.getId())
				.name(customerDto.getName())
				.surname(customerDto.getSurname())
				.age(customerDto.getAge())
				.email(customerDto.getEmail())
				.orders(new HashSet<>())
				.build();
	}

	static OrderDto fromOrderDtoToOrder(Order order) {
		return order == null ? null : OrderDto.builder()
				.id(order.getId())
				.quantity(order.getQuantity())
				.orderDate(order.getOrderDate())
				.productDto(order.getProduct() == null ? null : fromProductToProductDto(order.getProduct()))
				.customerDto(order.getCustomer() == null ? null : fromCustomerToCustomerDto(order.getCustomer()))
				.build();
	}

	static Order fromOrderDtoToOrder(OrderDto orderDto) {
		return orderDto == null ? null : Order.builder()
				.id(orderDto.getId())
				.quantity(orderDto.getQuantity())
				.orderDate(orderDto.getOrderDate())
				.product(orderDto.getProductDto() == null ? null : fromProductDtoToProduct(orderDto.getProductDto()))
				.customer(orderDto.getCustomerDto() == null ? null : fromCustomerDtoToCustomer(orderDto.getCustomerDto()))
				.build();
	}

	static User fromRegisterUserDtoToUser(RegisterUserDto registerUserDto) {
		return User
				.builder()
				.username(registerUserDto.getUsername())
				.password(registerUserDto.getPassword())
				.roles(registerUserDto.getRoles())
				.build();
	}
}
