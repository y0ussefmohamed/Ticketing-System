package com.projects.Ticketing.System.DTO;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEventResponse {
    private Long eventId;
    private String event;
    private Long capacity;
    private VenueInventoryResponse venue;
    private BigDecimal ticketPrice;
}
