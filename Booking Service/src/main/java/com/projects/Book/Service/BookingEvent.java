package com.projects.Book.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingEvent { // This is the object that will be sent to the listener in the Order Service
    private Long userId;
    private Long eventId;
    private Long ticketCount;
    private BigDecimal totalPrice; // Will be derived by getting the event price * ticketCount
}
