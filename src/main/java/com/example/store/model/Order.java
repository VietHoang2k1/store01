package com.example.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "address")
    private String address;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total")
    private Integer total; // Tổng tiền

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private Date date;

    @Column(name = "total_amount")
    private Integer totalAmount; // Tổng tiền bao gồm phí khác (nếu cần)
    @ManyToMany
    @JoinTable(
            name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;


    // Phương thức tính tổng số lượng sản phẩm
    public int calculateQuantity() {
        return products != null ? products.size() : 0;
    }

    // Phương thức tính tổng tiền
    public int calculateTotal() {
        return products != null ? products.stream()
                .mapToInt(product -> product.getNewPrice() != null ? product.getNewPrice() : 0)
                .sum() : 0;
    }
}
