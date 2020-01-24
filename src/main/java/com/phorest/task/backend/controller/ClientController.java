package com.phorest.task.backend.controller;

import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ClientController {

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

    @DeleteMapping(path = "/api/v1/client/{clientId}")
    public void deleteClient(@PathVariable UUID clientId) {
        clientService.deleteClient(clientId);
    }

}
