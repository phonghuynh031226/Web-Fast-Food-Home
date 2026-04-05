package com.poly.controller.asm_java5.service;

import com.poly.controller.asm_java5.entity.Cart;
import com.poly.controller.asm_java5.entity.CartId;
import com.poly.controller.asm_java5.entity.MenuItem;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.model.CartItemDTO;
import com.poly.controller.asm_java5.repository.CartRepository;
import com.poly.controller.asm_java5.repository.MenuItemRepository;
import com.poly.controller.asm_java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Integer TEMP_USER_ID = 1;

    public void addToCart(Integer itemId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            quantity = 1;
        }

        MenuItem item = menuItemRepository.findById(itemId).orElse(null);
        User user = userRepository.findById(TEMP_USER_ID).orElse(null);

        if (item == null || user == null) {
            return;
        }

        Cart cart = cartRepository.findByUser_UserIdAndMenuItem_ItemId(TEMP_USER_ID, itemId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    CartId id = new CartId();
                    id.setUserId(TEMP_USER_ID);
                    id.setItemId(itemId);

                    newCart.setId(id);
                    newCart.setUser(user);
                    newCart.setMenuItem(item);
                    newCart.setQuantity(0);
                    return newCart;
                });

        cart.setQuantity(cart.getQuantity() + quantity);
        cartRepository.save(cart);
    }

    public Map<Integer, CartItemDTO> getCartItems() {
        List<Cart> cartItems = cartRepository.findByUser_UserId(TEMP_USER_ID);

        Map<Integer, CartItemDTO> result = new LinkedHashMap<>();
        for (Cart cart : cartItems) {
            double totalPrice = cart.getMenuItem().getPrice() * cart.getQuantity();
            result.put(
                    cart.getMenuItem().getItemId(),
                    new CartItemDTO(cart.getMenuItem(), cart.getQuantity(), totalPrice)
            );
        }
        return result;
    }

    public double getCartTotal() {
        return getCartItems().values().stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum();
    }

    public void removeItem(Integer itemId) {
        cartRepository.deleteByUser_UserIdAndMenuItem_ItemId(TEMP_USER_ID, itemId);
    }

    public void increaseQuantity(Integer itemId) {
        cartRepository.findByUser_UserIdAndMenuItem_ItemId(TEMP_USER_ID, itemId)
                .ifPresent(cart -> {
                    cart.setQuantity(cart.getQuantity() + 1);
                    cartRepository.save(cart);
                });
    }

    public void decreaseQuantity(Integer itemId) {
        cartRepository.findByUser_UserIdAndMenuItem_ItemId(TEMP_USER_ID, itemId)
                .ifPresent(cart -> {
                    if (cart.getQuantity() > 1) {
                        cart.setQuantity(cart.getQuantity() - 1);
                        cartRepository.save(cart);
                    } else {
                        cartRepository.delete(cart);
                    }
                });
    }
}