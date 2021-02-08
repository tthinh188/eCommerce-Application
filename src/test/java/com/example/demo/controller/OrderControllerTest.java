package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Autowired
    private UserRepository userRepository = mock(UserRepository.class);

    private final String ITEM1 = "Round Widget";
    private final BigDecimal ITEM1_PRICE = BigDecimal.valueOf(2.99);
    private final String ITEM1_DESCRIPTION = "A widget that is round";

    private final String ITEM2 = "Square Widget";
    private final BigDecimal ITEM2_PRICE = BigDecimal.valueOf(1.99);
    private final String ITEM2_DESCRIPTION = "A widget that is square";

    private static final String TEST_USERNAME = "myusername";
    private static final String TEST_PASSWORD = "secretpassword";
    private static final String OTHER_USER = "other";
    @Before
    public void setUp() {
        orderController = new OrderController(orderRepository, userRepository);

        User user = new User();
        user.setId(0);
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);

        Item item = new Item();
        item.setId(1L);
        item.setName(ITEM1);
        item.setDescription(ITEM1_DESCRIPTION);
        BigDecimal price = ITEM1_PRICE;
        item.setPrice(price);
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);

        BigDecimal total = BigDecimal.valueOf(2.99);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(user);
        when(userRepository.findByUsername(OTHER_USER)).thenReturn(null);
    }

    @Test
    public void testSubmitOrder() {
        ResponseEntity<UserOrder> response = orderController.submit(TEST_USERNAME);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(ITEM1_PRICE, order.getTotal());
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void testSubmitOrderInvalidUser() {
        ResponseEntity<UserOrder> response = orderController.submit(OTHER_USER);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrderFromUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(TEST_USERNAME);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getOrderFromInvalidUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(OTHER_USER);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
