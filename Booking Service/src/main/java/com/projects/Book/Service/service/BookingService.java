package com.projects.Book.Service.service;

import com.projects.Book.Service.BookingEvent;
import com.projects.Book.Service.DTO.BookingRequest;
import com.projects.Book.Service.DTO.BookingResponse;
import com.projects.Book.Service.DTO.InventoryEventResponse;
import com.projects.Book.Service.client.InventoryServiceClient;
import com.projects.Book.Service.entity.Customer;
import com.projects.Book.Service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public BookingResponse createBooking(final BookingRequest bookingRequest) {
        // 1. Check if User Exists
        final Customer customer = customerRepository.findById(bookingRequest.getUserId()).orElse(null);
        if(customer == null)
            throw new RuntimeException("Customer not found");

        // 2. Get the Event Using the Inventory Service
        final InventoryEventResponse eventResponse = inventoryServiceClient.getInventory(bookingRequest.getEventId());
        if(eventResponse.getCapacity() < bookingRequest.getTicketCount())
            throw new RuntimeException("Capacity exceeded");

        // 3. Create a Booking & Send it to Order Service on a Kafka Topic
        final BookingEvent bookingEvent = this.createBookingEvent(bookingRequest, customer, eventResponse);
        kafkaTemplate.send("booking", bookingEvent);

        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }


    private BookingEvent createBookingEvent(final BookingRequest bookingRequest,
                                            final Customer customer,
                                            final InventoryEventResponse eventResponse) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(bookingRequest.getEventId())
                .ticketCount(bookingRequest.getTicketCount())
                .totalPrice(eventResponse.getTicketPrice()
                        .multiply(BigDecimal.valueOf(bookingRequest.getTicketCount())))
                .build();
    }
}
