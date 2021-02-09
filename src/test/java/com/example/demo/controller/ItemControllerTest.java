package com.example.demo.controller;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @Autowired
    private ItemController itemController;

    @Autowired
    ItemRepository itemRepository = mock(ItemRepository.class);

    private final String ITEM1 = "Round Widget";
    private final BigDecimal ITEM1_PRICE = BigDecimal.valueOf(2.99);
    private final String ITEM1_DESCRIPTION = "A widget that is round";

    private final String ITEM2 = "Square Widget";
    private final BigDecimal ITEM2_PRICE = BigDecimal.valueOf(1.99);
    private final String ITEM2_DESCRIPTION = "A widget that is square";

    @Before
    public void setup() {
        itemController = new ItemController(itemRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName(ITEM1);
        item.setDescription(ITEM1_DESCRIPTION);
        BigDecimal price = ITEM1_PRICE;
        item.setPrice(price);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName(ITEM2);
        item2.setDescription(ITEM2_DESCRIPTION);
        price = ITEM2_PRICE;
        item2.setPrice(price);

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);

        when(itemRepository.findAll()).thenReturn(items);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByName(ITEM1)).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void TestGetItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(ITEM1_PRICE, items.get(0).getPrice());
        assertEquals(ITEM2_PRICE, items.get(1).getPrice());
    }

    @Test
    public void TestGetItemById() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Item item = response.getBody();
        assertNotNull(item);
        assertEquals(ITEM1_PRICE, item.getPrice());
    }

    @Test
    public void TestGetItemByName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName(ITEM1);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(ITEM1_PRICE, items.get(0).getPrice());
    }

    @Test
    public void TestGetItemByInvalidName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("non exist item");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
}
