package com.poly.controller.asm_java5.controller;

import com.poly.controller.asm_java5.entity.Menu;
import com.poly.controller.asm_java5.entity.MenuItem;
import com.poly.controller.asm_java5.entity.Order;
import com.poly.controller.asm_java5.entity.User;
import com.poly.controller.asm_java5.repository.MenuItemRepository;
import com.poly.controller.asm_java5.repository.MenuRepository;
import com.poly.controller.asm_java5.repository.OrderItemRepository;
import com.poly.controller.asm_java5.repository.OrderRepository;
import com.poly.controller.asm_java5.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/admin")
    public String adminHome() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        List<Order> allOrders = orderRepository.findAll();
        double revenue = allOrders.stream()
                .filter(order -> order.getTotalAmount() != null)
                .mapToDouble(Order::getTotalAmount)
                .sum();

        YearMonth currentMonth = YearMonth.now();
        List<String> monthLabels = new ArrayList<>();
        List<Double> monthRevenues = new ArrayList<>();
        List<Integer> monthPercents = new ArrayList<>();
        double maxMonthRevenue = 0;
        for (int i = 5; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            double monthTotal = allOrders.stream()
                    .filter(order -> order.getCreatedAt() != null)
                    .filter(order -> YearMonth.from(order.getCreatedAt()).equals(month))
                    .filter(order -> order.getTotalAmount() != null)
                    .mapToDouble(Order::getTotalAmount)
                    .sum();
            monthLabels.add("Tháng " + month.getMonthValue());
            monthRevenues.add(monthTotal);
            if (monthTotal > maxMonthRevenue) maxMonthRevenue = monthTotal;
        }

        for (Double value : monthRevenues) {
            int percent = maxMonthRevenue <= 0 ? 4 : (int) Math.round(value * 100 / maxMonthRevenue);
            monthPercents.add(value <= 0 ? 4 : Math.max(percent, 4));
        }

        model.addAttribute("totalProducts", menuItemRepository.count());
        model.addAttribute("totalMenus", menuRepository.count());
        model.addAttribute("totalOrders", orderRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalRevenue", revenue);
        model.addAttribute("monthLabels", monthLabels);
        model.addAttribute("monthRevenues", monthRevenues);
        model.addAttribute("maxMonthRevenue", maxMonthRevenue <= 0 ? 1 : maxMonthRevenue);
        model.addAttribute("monthPercents", monthPercents);
        model.addAttribute("recentOrders", orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderId")).stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/admin/product")
    public String product(@RequestParam(required = false) Integer editId, Model model) {
        MenuItem itemForm = editId == null ? new MenuItem() : menuItemRepository.findById(editId).orElse(new MenuItem());
        if (itemForm.getStatus() == null) itemForm.setStatus(true);
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("menus", menuRepository.findAll(Sort.by("menuId")));
        model.addAttribute("items", menuItemRepository.findAll(Sort.by(Sort.Direction.DESC, "itemId")));
        return "admin/product";
    }

    @PostMapping("/admin/product/save")
    public String saveProduct(@RequestParam(required = false) Integer itemId,
                              @RequestParam Integer menuId,
                              @RequestParam String itemName,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) String image,
                              @RequestParam Double price,
                              @RequestParam(required = false) Boolean status,
                              RedirectAttributes ra) {
        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            ra.addFlashAttribute("error", "Danh mục không tồn tại.");
            return "redirect:/admin/product";
        }
        MenuItem item = itemId == null ? new MenuItem() : menuItemRepository.findById(itemId).orElse(new MenuItem());
        item.setMenu(menu);
        item.setItemName(itemName);
        item.setDescription(description);
        item.setImage(image);
        item.setPrice(price);
        item.setStatus(status != null && status);
        menuItemRepository.save(item);
        ra.addFlashAttribute("success", "Đã lưu sản phẩm.");
        return "redirect:/admin/product";
    }

    @PostMapping("/admin/product/delete")
    public String deleteProduct(@RequestParam Integer itemId, RedirectAttributes ra) {
        menuItemRepository.findById(itemId).ifPresent(item -> {
            item.setStatus(false);
            menuItemRepository.save(item);
        });
        ra.addFlashAttribute("success", "Đã ẩn sản phẩm khỏi website bán hàng.");
        return "redirect:/admin/product";
    }

    @PostMapping("/admin/product/restore")
    public String restoreProduct(@RequestParam Integer itemId, RedirectAttributes ra) {
        menuItemRepository.findById(itemId).ifPresent(item -> {
            item.setStatus(true);
            menuItemRepository.save(item);
        });
        ra.addFlashAttribute("success", "Đã mở bán lại sản phẩm.");
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/order")
    public String order(Model model) {
        model.addAttribute("orders", orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderId")));
        return "admin/order";
    }

    @PostMapping("/admin/order/delete")
    @Transactional
    public String deleteOrder(@RequestParam Integer orderId, RedirectAttributes ra) {
        orderItemRepository.deleteByOrder_OrderId(orderId);
        orderRepository.deleteById(orderId);
        ra.addFlashAttribute("success", "Đã xóa đơn hàng.");
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/user")
    public String user(Model model) {
        model.addAttribute("users", userRepository.findAll(Sort.by(Sort.Direction.DESC, "userId")));
        return "admin/user";
    }

    @PostMapping("/admin/user/role")
    public String updateUserRole(@RequestParam Integer userId,
                                 @RequestParam String role,
                                 RedirectAttributes ra) {
        if (!role.equals("admin") && !role.equals("customer")) {
            ra.addFlashAttribute("error", "Vai trò không hợp lệ.");
            return "redirect:/admin/user";
        }
        userRepository.findById(userId).ifPresent(user -> {
            user.setRole(role);
            userRepository.save(user);
        });
        ra.addFlashAttribute("success", "Đã cập nhật vai trò người dùng.");
        return "redirect:/admin/user";
    }

    @PostMapping("/admin/user/delete")
    public String deleteUser(@RequestParam Integer userId, RedirectAttributes ra) {
        if (orderRepository.findByUser_UserId(userId).isEmpty()) {
            userRepository.deleteById(userId);
            ra.addFlashAttribute("success", "Đã xóa người dùng.");
        } else {
            ra.addFlashAttribute("error", "Không thể xóa người dùng đã có đơn hàng. Hãy đổi vai trò hoặc giữ lại tài khoản.");
        }
        return "redirect:/admin/user";
    }
}
