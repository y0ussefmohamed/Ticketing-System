package com.projects.Order.Service.service;

import com.projects.Book.Service.BookingEvent;
import com.projects.Order.Service.client.InventoryServiceClient;
import com.projects.Order.Service.entity.Order;
import com.projects.Order.Service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;

    /// When a booking is added in the kafkaTemplate it automatically comes here and activates this function
    @KafkaListener(topics = "booking", groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent) {
        Order order = this.createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);

        inventoryServiceClient.updateEventInventory(bookingEvent.getEventId(), bookingEvent.getTicketCount());
    }

    private Order createOrder(BookingEvent bookingEvent) {
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }
}
