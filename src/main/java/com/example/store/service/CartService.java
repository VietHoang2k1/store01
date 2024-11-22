package com.example.store.service;

import com.example.store.model.CartItem;

import java.util.List;

public interface CartService {
    CartItem addItemToCart(CartItem cartItem);

    void removeItemFromCart(Long id);

    List<CartItem> getAllCartItems();
}