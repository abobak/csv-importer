package com.csv.task.backend.controller;

import com.csv.task.backend.api.ClientApi;
import com.csv.task.backend.dto.ClientDto;
import com.csv.task.backend.dto.ClientWithLoyaltyPointsDto;
import com.csv.task.backend.exception.InvalidInputException;
import com.csv.task.backend.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ClientController implements ClientApi {

    private final ClientService clientService;

    @GetMapping(path = "/api/v1/clients/{clientId}")
    public ClientDto getClient(@PathVariable UUID clientId) {
        return clientService.getClientById(clientId);
    }

    @PutMapping(path = "/api/v1/clients/{clientId}")
    public ClientDto updateClient(@PathVariable UUID clientId, @RequestBody ClientDto dto) {
        if (clientId != dto.getId()) {
            throw new UnsupportedOperationException("Changing user identifier is not allowed");
        }
        return clientService.updateClient(dto);
    }

    @PostMapping(path = "/api/v1/clients")
    public ClientDto createClient(@RequestBody ClientDto dto) {
        return clientService.createNewClient(dto);
    }

    @GetMapping(path = "/api/v1/clients")
    public List<ClientDto> getClient() {
        return clientService.getClients();
    }

    @DeleteMapping(path = "/api/v1/clients/{clientId}")
    public void deleteClient(@PathVariable UUID clientId) {
        clientService.deleteClient(clientId);
    }

    @GetMapping(path = "/api/v1/clients/top/{howMany}/since/{sinceWhen}")
    public List<ClientWithLoyaltyPointsDto> getTopClientsSinceDate(@PathVariable Integer howMany,
                                                                   @PathVariable String sinceWhen) {
        if (howMany < 1) {
            throw new InvalidInputException("Should request for at least one client");
        }
        LocalDate since;
        try {
            since = LocalDate.parse(sinceWhen, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException ex) {
            throw new InvalidInputException("Date should be passed in yyyy-MM-dd format");
        }
        return clientService.getBestClients(howMany, since);
    }

}
