package com.example.store.repository;

import com.example.store.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.date >= :startOfDay AND o.date < :endOfDay")
    Double calculateRevenueByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.date >= :startOfMonth AND o.date < :endOfMonth")
    Double calculateRevenueByMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.date >= :startOfYear AND o.date < :endOfYear")
    Double calculateRevenueByYear(@Param("startOfYear") LocalDateTime startOfYear, @Param("endOfYear") LocalDateTime endOfYear);

    Long countByStatus(String status);
}
