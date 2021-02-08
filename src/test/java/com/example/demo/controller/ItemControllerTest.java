package com.example.demo.controller;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
        

    }
}
