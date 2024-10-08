package com.backend.controller;

import com.backend.dto.ClientDTO;
import com.backend.dto.OrderDTO;
import com.backend.mapper.OrderMapper;
import com.backend.model.Order;
import com.backend.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Test getting all orders")
    public void testGetAllOrders() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setType(Order.TypeOrder.BUYING);
        orderDTO.setOrderDate(java.sql.Date.valueOf(LocalDate.now()));
        List<OrderDTO> orders = Collections.singletonList(orderDTO);

        when(orderService.getAllBuyingOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Test getting client orders")
    public void testGetClientOrders() throws Exception {
        String username = "testUser";
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setUsername(username);
        List<OrderDTO> orders = Collections.singletonList(new OrderDTO());

        when(orderService.getClientOrders(username)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/client")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Test filtering orders")
    public void testFilterOrders() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        List<OrderDTO> orders = Collections.singletonList(orderDTO);

        when(orderService.getOrdersBySearch(any(OrderDTO.class))).thenReturn(orders);

        mockMvc.perform(get("/api/orders/filter")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Test adding an order")
    public void testAddOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();

        when(orderMapper.toEntity(any(OrderDTO.class))).thenReturn(order);
        when(orderService.addOrder(order)).thenReturn(true);

        mockMvc.perform(post("/api/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order created"));
    }

    @Test
    @DisplayName("Test adding an order failure")
    public void testAddOrderFailure() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();

        when(orderMapper.toEntity(any(OrderDTO.class))).thenReturn(order);
        when(orderService.addOrder(order)).thenReturn(false);

        mockMvc.perform(post("/api/orders")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to create order"));
    }

    @Test
    @DisplayName("Test deleting an order")
    public void testDeleteOrder() throws Exception {
        int orderId = 1;
        Order order = new Order();

        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted"));

        verify(orderService, times(1)).deleteOrder(order);
    }

    @Test
    @DisplayName("Test deleting an order not found")
    public void testDeleteOrderNotFound() throws Exception {
        int orderId = 1;

        when(orderService.getOrderById(orderId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }

    @Test
    @DisplayName("Test updating an order")
    public void testUpdateOrder() throws Exception {
        int orderId = 1;
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();

        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(put("/api/orders/{id}", orderId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order updated"));

        verify(orderService, times(1)).updateOrder(order);
    }

    @Test
    @DisplayName("Test updating an order not found")
    public void testUpdateOrderNotFound() throws Exception {
        int orderId = 1;
        OrderDTO orderDTO = new OrderDTO();

        when(orderService.getOrderById(orderId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/orders/{id}", orderId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }
}
