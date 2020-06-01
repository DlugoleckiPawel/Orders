package com.app.controller;

import com.app.dto.*;
import com.app.model.enums.Category;
import com.app.service.OrdersStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/stats")
public class OrdersStatsController {

	private final OrdersStatsService ordersStatsService;

	@GetMapping("/from/{dateFrom}/to/{dateTo}")
	public OrdersStatsAveragePriceBetweenDatesDto getAveragePriceBetweenDates(@PathVariable String dateFrom, @PathVariable String dateTo) {
		return ordersStatsService.calculateAveragePriceBetween(dateFrom, dateTo);
	}

	@GetMapping("/stat1")
	public Map<Category, ProductDto> getMostExpensiveProductForCategory() {
		return ordersStatsService.mostExpensiveProductInEachCategory();
	}

	@GetMapping("/stat2")
	public Map<CustomerDto, List<ProductDto>> getCustomersWithProducts() {
		return ordersStatsService.getCustomersWithProducts();
	}

	@GetMapping("/stat3")
	public DatesOrdersQuantity dateAndQuantityWhenThereWereMostOrders() {
		return ordersStatsService.dateAndQuantityWhenThereWereMostOrders();
	}

	@GetMapping("/stat4")
	public Map<Month, Category> thePostPopularCategoryByMonths() {
		return ordersStatsService.thePostPopularCategoryByMonths();
	}

	@GetMapping("/stat5")
	public BigDecimal sumPriceOrders() {
		return ordersStatsService.totalSumPriceOrders();
	}

	@GetMapping("/stat6/{N}")
	public Integer numberOfCustomersBoughtProductAmount(@PathVariable int N) {
		return ordersStatsService.numberOfCustomersBoughtProductAmount(N);
	}

	@GetMapping("/stat7")
	public Map<Month, Integer> numberOfProductsByMonths() {
		return ordersStatsService.numberOfProductsByMonths();
	}
}
