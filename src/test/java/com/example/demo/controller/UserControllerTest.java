package com.example.demo.controller;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private static final String TEST_USERNAME = "myusername";
    private static final String TEST_PASSWORD = "secretpassword";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final String CONFIRM_PASSWORD = "NoMatchPassword";
    private static final String INVALID_PASSWORD = "123";

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository = mock(UserRepository.class);

    @Autowired
    private CartRepository cartRepository = mock(CartRepository.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController(userRepository, cartRepository, passwordEncoder);
        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.findByUsername("someone")).thenReturn(null);
    }

    @Test
    public void testCreateUser() {
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(HASHED_PASSWORD);
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(TEST_USERNAME);
        r.setPassword(TEST_PASSWORD);
        r.setConfirmPassword(TEST_PASSWORD);
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(TEST_USERNAME, u.getUsername());
        assertEquals(HASHED_PASSWORD, u.getPassword());
    }

    @Test
    public void testCreateUserFailConfirmPassword() {
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(HASHED_PASSWORD);
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(TEST_USERNAME);
        r.setPassword(TEST_PASSWORD);
        r.setConfirmPassword(CONFIRM_PASSWORD);
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
   }

    @Test
    public void testCreateUserInvalidPassword() {
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(HASHED_PASSWORD);
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(TEST_USERNAME);
        r.setPassword(INVALID_PASSWORD);
        r.setConfirmPassword(INVALID_PASSWORD);
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindUserById() {
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());;
    }

    @Test
    public void testFindInvalidUserId() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testFindUserName() {
        final ResponseEntity<User> response = userController.findByUserName("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(TEST_USERNAME, user.getUsername());
    }


}