package com.example.assignment01.service;

import com.example.assignment01.model.Item;
import com.example.assignment01.model.Product;
import com.example.assignment01.system.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SessionScope
@Service
public class CartServiceImpl implements CartService{

    private final ProductService productService;

    @Autowired
    public CartServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    Map<Integer, Item> map = new HashMap<>();

    @Override
    public Item add(Integer id) {
        Item item = map.get(id);
        if (item == null) {
            item = new Item();
            List<Product> list = this.productService.findAll();
            Product product = list.stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new ObjectNotFoundException("product", id));
            item.setId(product.getId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQty(1);
            item.setProduct(product);
            map.put(id, item);
        } else {
            item.setQty(item.getQty() + 1);
        }

        return item;
    }

    @Override
    public void remove(Integer id) {
        map.remove(id);
    }

    @Override
    public Item update(Integer id, Integer qty) {
        Item item = map.get(id);
        item.setQty(qty);
        return item;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public List<Item> getItems() {
        List items = new ArrayList<>(map.values());
        return items;
    }

    @Override
    public Integer getCount() {
        return map.values().stream()
                .mapToInt(item -> item.getQty())
                .sum();

    }

    @Override
    public Double getAmount() {
        return map.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQty())
                .sum();
    }


}
