package com.app.repository;

import com.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("select o.orderDate from Order o group by o.id order by count(o)")
	List<LocalDate> groupByOrderDate();
}
