package com.poly.controller.asm_java5.service;

import com.poly.controller.asm_java5.entity.Cart;
import com.poly.controller.asm_java5.entity.Order;
import com.poly.controller.asm_java5.entity.OrderItem;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.repository.CartRepository;
import com.poly.controller.asm_java5.repository.OrderItemRepository;
import com.poly.controller.asm_java5.repository.OrderRepository;
import com.poly.controller.asm_java5.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrderFromCart(Integer userId, String customerName, String phone, String address, String note, String paymentMethod) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        List<Cart> cartItems = cartRepository.findByUser_UserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng đang trống");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(cart -> cart.getMenuItem().getPrice() * cart.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(customerName);
        order.setPhone(phone);
        order.setAddress(address);
        order.setNote(note);
        order.setPaymentMethod(paymentMethod == null || paymentMethod.isBlank() ? "CASH" : paymentMethod);
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(LocalDateTime.now());
        order = orderRepository.save(order);

        for (Cart cart : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(cart.getMenuItem());
            orderItem.setItemName(cart.getMenuItem().getItemName());
            orderItem.setPrice(cart.getMenuItem().getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItemRepository.save(orderItem);
        }

        cartRepository.deleteByUser_UserId(userId);
        return order;
    }
}
