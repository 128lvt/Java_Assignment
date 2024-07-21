package com.example.assignment01.controllers;

import com.example.assignment01.models.Item;
import com.example.assignment01.models.User;
import com.example.assignment01.securities.UserPrincipal;
import com.example.assignment01.services.CartService;
import com.example.assignment01.services.OrderService;
import com.example.assignment01.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {

        if (userPrincipal != null) {
            String fullname = userPrincipal.getFullName();
            String address = userPrincipal.getAddress();
            String phoneNumber = userPrincipal.getPhoneNumber();
            User userInfo = new User();
            userInfo.setFullname(fullname);
            userInfo.setAddress(address);
            userInfo.setPhoneNumber(phoneNumber);
            model.addAttribute("userInfo", userInfo);
        } else {
            model.addAttribute("userInfo", new User());
        }
        return "checkout";
    }

    @PostMapping("/order")
    public String placeOrder(@RequestParam(name = "isCreate", required = false) Boolean isCreate,
                             Model model,
                             @ModelAttribute User user,
                             @AuthenticationPrincipal UserPrincipal userPrincipal,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {


        boolean isCreated = (isCreate != null) ? isCreate : false;

        try {
            if (isCreated && userPrincipal == null) {
                User newUser = this.userService.save(user);
                placeOrderForUser(newUser);
                clearCart(session);
                redirectAttributes.addFlashAttribute("message", "Order Successfully");
            } else if (!isCreated && userPrincipal != null) {
                User loginUser = this.userService.findById(userPrincipal.getId());
                placeOrderForUser(loginUser);
                clearCart(session);
                redirectAttributes.addFlashAttribute("message", "Order Successfully");

            } else {
                redirectAttributes.addFlashAttribute("message", "Order failed: Invalid user state");
                return "redirect:/login";
            }
        } catch (Exception e) {
            logger.error("Order placement failed", e);
            redirectAttributes.addFlashAttribute("message", "Order failed");
        }
        return "redirect:/checkout";
    }

    private void placeOrderForUser(User user) throws Exception {
        List<Item> cartsItem = this.cartService.getItems();
        List<Integer> productIds = cartsItem.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        List<Integer> quantities = cartsItem.stream()
                .map(Item::getQty)
                .collect(Collectors.toList());
        this.orderService.createOrder(user, productIds, quantities);

    }

    private void clearCart(HttpSession session) {
        session.removeAttribute("cart");
        this.cartService.clear();
    }

}
