package com.projects.Ticketing.System.service;

import com.projects.Ticketing.System.DTO.InventoryEventResponse;
import com.projects.Ticketing.System.DTO.VenueInventoryResponse;
import com.projects.Ticketing.System.entity.Event;
import com.projects.Ticketing.System.entity.Venue;
import com.projects.Ticketing.System.repository.EventRepository;
import com.projects.Ticketing.System.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public List<InventoryEventResponse> getAllInventoryEvents() {
        final List<Event> events = eventRepository.findAll();

        // Map each Event in the List to InventoryEventResponse
        return events.stream()
                .map(event -> InventoryEventResponse.builder()
                        .eventId(event.getId())
                        .event(event.getName())
                        .capacity(event.getRemainingCapacity())
                        .venue(VenueInventoryResponse.builder()
                                .totalCapacity(event.getVenue().getTotalCapacity())
                                .venueName(event.getVenue().getName())
                                .venueId(event.getVenue().getId())
                                .build())
                        .ticketPrice(event.getTicketPrice())
                        .build())
                .collect(Collectors.toList());
    }

    public InventoryEventResponse getEventInventory(final Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);

        return InventoryEventResponse.builder()
                .eventId(eventId)
                .event(event.getName())
                .capacity(event.getRemainingCapacity())
                .venue(VenueInventoryResponse.builder()
                        .totalCapacity(event.getVenue().getTotalCapacity())
                        .venueName(event.getVenue().getName())
                        .venueId(event.getVenue().getId())
                        .build())
                .ticketPrice(event.getTicketPrice())
                .build();
    }

    public VenueInventoryResponse getInventoryForVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId).orElse(null);

        return VenueInventoryResponse.builder()
                .venueId(venueId)
                .venueName(venue.getName())
                .totalCapacity(venue.getTotalCapacity())
                .build();
    }

    public void updateEventCapacity(Long eventId, Long ticketsBooked) {
        Event event = eventRepository.findById(eventId).orElse(null);

        event.setRemainingCapacity(event.getRemainingCapacity() - ticketsBooked);
        eventRepository.save(event);
    }
}
