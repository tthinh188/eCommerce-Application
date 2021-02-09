package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartControllerTest {
    @Autowired
    private CartController cartController;

    @Autowired
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Autowired
    private CartRepository cartRepository = mock(CartRepository.class);

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
    public void setup() {
        cartController = new CartController(userRepository, cartRepository, itemRepository);

        Cart cart = new Cart();

        User user = new User();
        user.setId(0);
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName(ITEM1);
        item.setDescription(ITEM1_DESCRIPTION);
        BigDecimal price = ITEM1_PRICE;
        item.setPrice(price);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

    }

    @Test
    public void TestAddToCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername(TEST_USERNAME);
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(ITEM1, cart.getItems().get(0).getName());
        assertEquals(ITEM1_PRICE, cart.getTotal());
    }

    @Test
    public void TestAddToCartInvalidUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("non exist user");
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void TestRemoveFromCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername(TEST_USERNAME);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void TestRemoveFromCartInvalidUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("non exist user");
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
