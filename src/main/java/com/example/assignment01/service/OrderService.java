package com.example.assignment01.service;

import com.example.assignment01.model.Order;
import com.example.assignment01.model.OrderDetail;
import com.example.assignment01.model.Product;
import com.example.assignment01.model.User;
import com.example.assignment01.repository.OrderDetailRepository;
import com.example.assignment01.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;


    @Autowired
    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
    }

    public Order createOrder(User user, List<Integer> productIds, List<Integer> quantities) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setUser(user);
        order.setOrderDetails(new ArrayList<>());

        double totalMoney = 0;

        for (int i = 0; i < productIds.size(); i++) {
            Product product = this.productService.findById(productIds.get(i));
            Integer qty = quantities.get(i);
            double price = product.getPrice();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetail.setQty(qty);
            orderDetail.setPrice(price);

            order.getOrderDetails().add(orderDetail);
            totalMoney += qty * price;
        }
        order.setTotalMoney(totalMoney);
        this.orderRepository.save(order);
        return order;
    }
}
