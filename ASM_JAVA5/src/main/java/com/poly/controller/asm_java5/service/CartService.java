package com.poly.controller.asm_java5.service;

import com.poly.controller.asm_java5.entity.Cart;
import com.poly.controller.asm_java5.entity.CartId;
import com.poly.controller.asm_java5.entity.MenuItem;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.model.CartItemDTO;
import com.poly.controller.asm_java5.repository.CartRepository;
import com.poly.controller.asm_java5.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private static final String SESSION_CART_KEY = "sessionCart";

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> getSessionCart(HttpSession session) {
        Map<Integer, Integer> sessionCart =
                (Map<Integer, Integer>) session.getAttribute(SESSION_CART_KEY);

        if (sessionCart == null) {
            sessionCart = new HashMap<>();
            session.setAttribute(SESSION_CART_KEY, sessionCart);
        }

        return sessionCart;
    }

    public void addToCart(Integer itemId, Integer quantity, HttpSession session) {
        if (quantity == null || quantity < 1) {
            quantity = 1;
        }

        MenuItem item = menuItemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            Map<Integer, Integer> sessionCart = getSessionCart(session);
            sessionCart.put(itemId, sessionCart.getOrDefault(itemId, 0) + quantity);
            session.setAttribute(SESSION_CART_KEY, sessionCart);
            return;
        }

        Cart cart = cartRepository.findByUser_UserIdAndMenuItem_ItemId(user.getUserId(), itemId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    CartId id = new CartId();
                    id.setUserId(user.getUserId());
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

    public Map<Integer, CartItemDTO> getCartItems(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<Integer, CartItemDTO> result = new LinkedHashMap<>();

        if (user == null) {
            Map<Integer, Integer> sessionCart = getSessionCart(session);

            for (Map.Entry<Integer, Integer> entry : sessionCart.entrySet()) {
                Integer itemId = entry.getKey();
                Integer quantity = entry.getValue();

                MenuItem item = menuItemRepository.findById(itemId).orElse(null);
                if (item == null) {
                    continue;
                }

                double totalPrice = item.getPrice() * quantity;
                result.put(itemId, new CartItemDTO(item, quantity, totalPrice));
            }

            return result;
        }

        List<Cart> cartItems = cartRepository.findByUser_UserId(user.getUserId());
        for (Cart cart : cartItems) {
            double totalPrice = cart.getMenuItem().getPrice() * cart.getQuantity();
            result.put(
                    cart.getMenuItem().getItemId(),
                    new CartItemDTO(cart.getMenuItem(), cart.getQuantity(), totalPrice)
            );
        }

        return result;
    }

    public double getCartTotal(HttpSession session) {
        return getCartItems(session).values().stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum();
    }

    public void removeItem(Integer itemId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            Map<Integer, Integer> sessionCart = getSessionCart(session);
            sessionCart.remove(itemId);
            session.setAttribute(SESSION_CART_KEY, sessionCart);
            return;
        }

        cartRepository.deleteByUser_UserIdAndMenuItem_ItemId(user.getUserId(), itemId);
    }

    public void increaseQuantity(Integer itemId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            Map<Integer, Integer> sessionCart = getSessionCart(session);
            if (sessionCart.containsKey(itemId)) {
                sessionCart.put(itemId, sessionCart.get(itemId) + 1);
                session.setAttribute(SESSION_CART_KEY, sessionCart);
            }
            return;
        }

        cartRepository.findByUser_UserIdAndMenuItem_ItemId(user.getUserId(), itemId)
                .ifPresent(cart -> {
                    cart.setQuantity(cart.getQuantity() + 1);
                    cartRepository.save(cart);
                });
    }

    public void decreaseQuantity(Integer itemId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            Map<Integer, Integer> sessionCart = getSessionCart(session);
            if (sessionCart.containsKey(itemId)) {
                int currentQuantity = sessionCart.get(itemId);
                if (currentQuantity > 1) {
                    sessionCart.put(itemId, currentQuantity - 1);
                } else {
                    sessionCart.remove(itemId);
                }
                session.setAttribute(SESSION_CART_KEY, sessionCart);
            }
            return;
        }

        cartRepository.findByUser_UserIdAndMenuItem_ItemId(user.getUserId(), itemId)
                .ifPresent(cart -> {
                    if (cart.getQuantity() > 1) {
                        cart.setQuantity(cart.getQuantity() - 1);
                        cartRepository.save(cart);
                    } else {
                        cartRepository.delete(cart);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void mergeSessionCartToDatabase(HttpSession session, User user) {
        Map<Integer, Integer> sessionCart =
                (Map<Integer, Integer>) session.getAttribute(SESSION_CART_KEY);

        if (sessionCart == null || sessionCart.isEmpty() || user == null) {
            return;
        }

        for (Map.Entry<Integer, Integer> entry : sessionCart.entrySet()) {
            Integer itemId = entry.getKey();
            Integer quantity = entry.getValue();

            MenuItem item = menuItemRepository.findById(itemId).orElse(null);
            if (item == null) {
                continue;
            }

            Cart cart = cartRepository.findByUser_UserIdAndMenuItem_ItemId(user.getUserId(), itemId)
                    .orElseGet(() -> {
                        Cart newCart = new Cart();
                        CartId id = new CartId();
                        id.setUserId(user.getUserId());
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

        session.removeAttribute(SESSION_CART_KEY);
    }

    public boolean isCartEmpty(HttpSession session) {
        return getCartItems(session).isEmpty();
    }

    public void clearCart(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.removeAttribute("sessionCart");
            return;
        }

        cartRepository.deleteByUser_UserId(user.getUserId());
    }
}