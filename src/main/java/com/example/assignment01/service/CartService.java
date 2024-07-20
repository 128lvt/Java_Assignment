package com.example.assignment01.service;

import com.example.assignment01.model.Item;

import java.util.List;

public interface CartService {
    Item add(Integer id);

    void remove(Integer id);

    Item update(Integer id, Integer qty);

    void clear();

    List<Item> getItems();

    Integer getCount();

    Double getAmount();

}
