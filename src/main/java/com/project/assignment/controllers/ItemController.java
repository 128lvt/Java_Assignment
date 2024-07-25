package com.project.assignment.controllers;


import com.project.assignment.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    private final CartService cartService;

    @Autowired
    public ItemController(CartService cartService) {
        this.cartService = cartService;

    }

    @GetMapping("/shopping-cart")
    public String viewCart(Model model) {
        model.addAttribute("items", cartService);
        return "shopping-cart";
    }

    @GetMapping("/shopping-cart/add/{id}")
    public String addItem(@PathVariable Integer id) {
        this.cartService.add(id);
        return "redirect:/shopping-cart";
    }

    @GetMapping("shopping-cart/remove/{id}")
    public String clearCart(@PathVariable Integer id) {
        this.cartService.remove(id);
        return "redirect:/shopping-cart";
    }

    @PostMapping("/shopping-cart/update/{id}")
    public String updateCart(@PathVariable("id") Integer id,
                             @RequestParam("qty") Integer qty) {
        this.cartService.update(id, qty);
        return "redirect:/shopping-cart";
    }


}
