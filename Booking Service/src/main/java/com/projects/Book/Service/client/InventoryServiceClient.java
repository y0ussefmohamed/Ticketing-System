package com.projects.Book.Service.client;

import com.projects.Book.Service.DTO.InventoryEventResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InventoryServiceClient {

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    public InventoryEventResponse getInventory(final Long eventId) {
        final RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(
                inventoryServiceUrl + "/events/" + eventId,
                InventoryEventResponse.class
        );
    }
}
