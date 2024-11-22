package com.example.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ProductName")
    private String productName;

    @Column(name = "imageurl")
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "oldPrice")
    private Integer oldPrice;

    @Column(name = "newPrice")
    private Integer newPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    // Chuyển từ `int` sang `Integer` để cho phép nhận giá trị null từ database
    @Column(name = "sales", nullable = true)
    private Integer sales;

    // Thêm constructor mặc định để đặt giá trị `sales` là 0 nếu cần

}
