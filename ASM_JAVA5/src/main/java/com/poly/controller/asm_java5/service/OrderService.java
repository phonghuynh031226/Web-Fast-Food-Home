package com.poly.controller.asm_java5.service;

import com.poly.controller.asm_java5.entity.Order;
import com.poly.controller.asm_java5.entity.OrderItem;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.model.CartItemDTO;
import com.poly.controller.asm_java5.repository.OrderItemRepository;
import com.poly.controller.asm_java5.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    public Order createOrder(
            HttpSession session,
            String customerName,
            String phone,
            String address,
            String note,
            String paymentMethod
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return null;
        }

        Map<Integer, CartItemDTO> cartItems = cartService.getCartItems(session);
        if (cartItems.isEmpty()) {
            return null;
        }

        double totalAmount = cartService.getCartTotal(session);

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(customerName);
        order.setPhone(phone);
        order.setAddress(address);
        order.setNote(note);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (CartItemDTO cartItem : cartItems.values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMenuItem(cartItem.getItem());
            orderItem.setItemName(cartItem.getItem().getItemName());
            orderItem.setPrice(cartItem.getItem().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            orderItemRepository.save(orderItem);
        }

        cartService.clearCart(session);

        return savedOrder;
    }
}