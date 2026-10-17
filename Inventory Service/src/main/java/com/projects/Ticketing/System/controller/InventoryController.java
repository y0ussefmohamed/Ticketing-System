package com.projects.Ticketing.System.controller;

import com.projects.Ticketing.System.DTO.InventoryEventResponse;
import com.projects.Ticketing.System.DTO.VenueInventoryResponse;
import com.projects.Ticketing.System.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/inventory/events")
    List<InventoryEventResponse> getAllInventoryEvents() {
        return inventoryService.getAllInventoryEvents();
    }

    @GetMapping("/inventory/events/{eventId}")
    InventoryEventResponse getAllInventoryEvents(@PathVariable Long eventId) {
        return inventoryService.getEventInventory(eventId);
    }

    @GetMapping("/inventory/venues/{venueId}")
    VenueInventoryResponse inventoryByVenueId(@PathVariable("venueId") Long venueId) {
        return inventoryService.getInventoryForVenue(venueId);
    }

    @PutMapping("/inventory/event/{eventId}/capacity/{ticketsBooked}")
    public ResponseEntity<Void> updateEventCapacity(@PathVariable("eventId") Long eventId,
                                                    @PathVariable("ticketsBooked") Long ticketsBooked) {
        inventoryService.updateEventCapacity(eventId, ticketsBooked);
        return ResponseEntity.ok().build();
    }
}