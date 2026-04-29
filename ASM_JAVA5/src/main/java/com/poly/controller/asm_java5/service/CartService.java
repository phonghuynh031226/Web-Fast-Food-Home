package com.poly.controller.asm_java5.service;

import com.poly.controller.asm_java5.entity.Cart;
import com.poly.controller.asm_java5.entity.CartId;
import com.poly.controller.asm_java5.entity.MenuItem;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.model.CartItemDTO;
import com.poly.controller.asm_java5.repository.CartRepository;
import com.poly.controller.asm_java5.repository.MenuItemRepository;
import com.poly.controller.asm_java5.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Integer TEMP_USER_ID = 1;

    private Integer normalizeUserId(Integer userId) {
        return userId != null ? userId : TEMP_USER_ID;
    }

    public void addToCart(Integer userId, Integer itemId, Integer quantity) {
        Integer currentUserId = normalizeUserId(userId);
        if (quantity == null || quantity < 1) {
            quantity = 1;
        }

        MenuItem item = menuItemRepository.findById(itemId).orElse(null);
        User user = userRepository.findById(currentUserId).orElse(null);

        if (item == null || user == null || Boolean.FALSE.equals(item.getStatus())) {
            return;
        }

        Cart cart = cartRepository.findByUser_UserIdAndMenuItem_ItemId(currentUserId, itemId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    CartId id = new CartId();
                    id.setUserId(currentUserId);
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

    public void addToCart(Integer itemId, Integer quantity) {
        addToCart(TEMP_USER_ID, itemId, quantity);
    }

    public Map<Integer, CartItemDTO> getCartItems(Integer userId) {
        Integer currentUserId = normalizeUserId(userId);
        List<Cart> cartItems = cartRepository.findByUser_UserId(currentUserId);

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

    public Map<Integer, CartItemDTO> getCartItems() {
        return getCartItems(TEMP_USER_ID);
    }

    public double getCartTotal(Integer userId) {
        return getCartItems(userId).values().stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum();
    }

    public double getCartTotal() {
        return getCartTotal(TEMP_USER_ID);
    }

    public void removeItem(Integer userId, Integer itemId) {
        cartRepository.deleteByUser_UserIdAndMenuItem_ItemId(normalizeUserId(userId), itemId);
    }

    public void removeItem(Integer itemId) {
        removeItem(TEMP_USER_ID, itemId);
    }

    public void increaseQuantity(Integer userId, Integer itemId) {
        cartRepository.findByUser_UserIdAndMenuItem_ItemId(normalizeUserId(userId), itemId)
                .ifPresent(cart -> {
                    cart.setQuantity(cart.getQuantity() + 1);
                    cartRepository.save(cart);
                });
    }

    public void increaseQuantity(Integer itemId) {
        increaseQuantity(TEMP_USER_ID, itemId);
    }

    public void decreaseQuantity(Integer userId, Integer itemId) {
        cartRepository.findByUser_UserIdAndMenuItem_ItemId(normalizeUserId(userId), itemId)
                .ifPresent(cart -> {
                    if (cart.getQuantity() > 1) {
                        cart.setQuantity(cart.getQuantity() - 1);
                        cartRepository.save(cart);
                    } else {
                        cartRepository.delete(cart);
                    }
                });
    }

    public void decreaseQuantity(Integer itemId) {
        decreaseQuantity(TEMP_USER_ID, itemId);
    }

    public void clearCart(Integer userId) {
        cartRepository.deleteByUser_UserId(normalizeUserId(userId));
    }
}
