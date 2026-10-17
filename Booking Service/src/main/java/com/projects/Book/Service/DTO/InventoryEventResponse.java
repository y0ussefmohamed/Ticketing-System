package com.projects.Book.Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEventResponse {
    private Long eventId;
    private String event;
    private Long capacity;
    private VenueInventoryResponse venue;
    private BigDecimal ticketPrice;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class VenueInventoryResponse {
        private Long venueId;
        private String venueName;
        private Long totalCapacity;
    }
}
