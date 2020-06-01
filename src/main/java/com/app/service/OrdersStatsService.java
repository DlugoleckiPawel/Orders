package com.app.service;

import com.app.converter.CustomerConverter;
import com.app.dto.*;
import com.app.exceptions.AppException;
import com.app.mappers.ModelMapper;
import com.app.model.Order;
import com.app.model.enums.Category;
import com.app.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.impl.collector.Collectors2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdersStatsService {
	private final OrderRepository orderRepository;

	public OrdersStatsAveragePriceBetweenDatesDto calculateAveragePriceBetween(String beginDateStr, String endDateStr) {

		if (Objects.isNull(beginDateStr)) {
			throw new AppException("begin date object is null");
		}

		if (Objects.isNull(endDateStr)) {
			throw new AppException("end date object is null");
		}

		LocalDate beginDate = LocalDate.parse(beginDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		System.out.println("Begin date: " + beginDate);
		System.out.println("End date: " + endDate);

		if (beginDate.isAfter(endDate)) {
			throw new AppException("begin date can not be later than end date");
		}

		var averagePrice = orderRepository
				.findAll()
				.stream()
				.filter(order -> order.betweenDates(beginDate, endDate))
				.collect(Collectors2.summarizingBigDecimal(Order::totalPrice))
				.getAverage();
		System.out.println("AVG PRICE: " + averagePrice);

		return OrdersStatsAveragePriceBetweenDatesDto
				.builder()
				.price(averagePrice)
				.build();


	}

	public Map<Category, ProductDto> mostExpensiveProductInEachCategory() {
		return orderRepository
				.findAll()
				.stream()
				.collect(Collectors.groupingBy(
						order -> order.getProduct().getCategory(),
						Collectors.collectingAndThen(
								Collectors.maxBy(Comparator.comparing(order -> order.getProduct().getPrice())),
								orderOp -> ModelMapper.fromProductToProductDto(orderOp
										.orElseThrow()
										.getProduct())
						)
				));
	}

	public Map<CustomerDto, List<ProductDto>> getCustomersWithProducts() {
		return orderRepository
				.findAll()
				.stream()
				.collect(Collectors.groupingBy(
						order -> ModelMapper.fromCustomerToCustomerDto(order.getCustomer()),
						Collectors.mapping(order -> ModelMapper.fromProductToProductDto(order.getProduct()), Collectors.toList())
				));
	}

	// Wyznacz informację o kliencie, który zapłacił najwięcej za
	// złożone zamówienia.
	// sql wysylasz zapytania ktore zwraca dane customera po tym jak pogrupujesz po sumie cen za zamowienie
	public CustomerWithPrice getCustomerWhoPaidTheMostForAllPurchases() {
		// mapa
		var entryMap = orderRepository
				.findAll()
				.stream()
				.collect(
						Collectors.groupingBy(
								order -> ModelMapper.fromCustomerToCustomerDto(order.getCustomer()),
								Collectors.mapping(
										Order::totalPrice,
										Collectors2.summarizingBigDecimal(o -> o))))
				.entrySet()
				.stream()
				.max(Comparator.comparing(e -> e.getValue().getSum()))
				.orElseThrow(() -> new AppException("error"));

		return CustomerWithPrice
				.builder()
				.customerDto(entryMap.getKey())
				.price(entryMap.getValue().getSum())
				.build();

		// https://stackoverflow.com/questions/36328063/how-to-return-a-custom-object-from-a-spring-data-jpa-group-by-query
	/*	return orderRepository
				.findWithMaxTotalOrdersPrice()
				.stream()
				.findFirst()
				//.map(ModelMapper::fromCustomerToCustomerDto)
				.orElseThrow();*/

	}

	// Wyznaczenie daty, dla której złożono najwięcej zamówień oraz daty
	// dla której złożono najmniej zamówień
	public DatesOrdersQuantity dateAndQuantityWhenThereWereMostOrders() {
		/*var entryDate = orderRepository
				.findAll()
				.stream()
				.collect(Collectors.groupingBy(Order::getOrderDate, Collectors.counting()))
				.entrySet()
				.stream()
				.max(Map.Entry.comparingByValue())
				.orElseThrow(() -> new AppException("error"));

		return DatesOrdersQuantity
				.builder()
				.date(entryDate.getKey())
				.quantity(entryDate.getValue())
				.build();*/

		var dates = orderRepository.groupByOrderDate();
		return DatesOrdersQuantity
				.builder()
				.maxDate(dates.get(dates.size() - 1))
				.minDate(dates.get(0))
				.build();
	}

	public Map<Month, Category> thePostPopularCategoryByMonths() {
		return orderRepository
				.findAll()
				.stream()
				.collect(Collectors.groupingBy(
						x -> x.getOrderDate().getMonth(),
						Collectors.collectingAndThen(
								Collectors.flatMapping(order -> Collections.nCopies(order.getQuantity(), order.getProduct().getCategory()).stream(), Collectors.toList()),
								categories -> categories
										.stream()
										.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
										.entrySet()
										.stream()
										.max(Comparator.comparing(Map.Entry::getValue))
										.orElseThrow()
										.getKey()
						)
				));
	}

	public BigDecimal totalSumPriceOrders() {
		return orderRepository
				.findAll()
				.stream()
				.map(Order::totalPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public Integer numberOfCustomersBoughtProductAmount(final int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than 0");
		}
		List<CustomerDto> customerList = orderRepository
				.findAll()
				.stream()
				.filter(x -> x.getQuantity() >= quantity)
				.map(order -> ModelMapper.fromCustomerToCustomerDto(order.getCustomer()))
				.collect(Collectors.toList());

		CustomerConverter customerConverter = new CustomerConverter("src/main/resources/customers.json");
		customerConverter.toJson(customerList);
		return customerList.size();
	}

	public Map<Month, Integer> numberOfProductsByMonths() {
		return orderRepository
				.findAll()
				.stream()
				.collect(Collectors.groupingBy(
						order -> order.getOrderDate().getMonth(),
						Collectors.collectingAndThen(
								Collectors.mapping(Order::getQuantity, Collectors.toList()),
								quantities -> quantities.stream().reduce(0, Integer::sum)
						)
				));
	}
}



