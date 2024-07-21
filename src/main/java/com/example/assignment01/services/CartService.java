package com.example.assignment01.services;

import com.example.assignment01.models.Item;

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
