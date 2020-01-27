package com.phorest.task.backend.service;

import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.dto.ClientWithLoyaltyPointsDto;
import com.phorest.task.backend.mapper.ClientMapper;
import com.phorest.task.backend.model.Client;
import com.phorest.task.backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public ClientDto getClientById(UUID id) {
        Client c = clientRepository.getOne(id);
        return clientMapper.clientToDto(c);
    }

    public ClientDto createNewClient(ClientDto dto) {
        if (!isNull(dto.getId())) {
            throw new UnsupportedOperationException("Can't create new client with predefined ID");
        }
        Client c = clientMapper.dtoToClient(dto);
        c = clientRepository.save(c);
        return clientMapper.clientToDto(c);
    }

    public ClientDto updateClient(ClientDto dto) {
        if (isNull(dto.getId())) {
            throw new UnsupportedOperationException("Can't update client with no ID");
        }
        Client c = clientRepository.getOne(dto.getId());
        updateFromDto(c, dto);
        c = clientRepository.save(c);
        return clientMapper.clientToDto(c);
    }

    public void deleteClient(UUID id) {
        clientRepository.deleteById(id);
    }

    private void updateFromDto(Client c, ClientDto dto) {
        c.setBanned(dto.isBanned());
        c.setFirstName(dto.getFirstName());
        c.setGender(dto.getGender());
        c.setLastName(dto.getLastName());
        c.setPhone(dto.getPhone());
    }

    public List<ClientDto> getClients() {
        return clientMapper.clientListToDtoList(clientRepository.findAll());
    }

    public List<ClientWithLoyaltyPointsDto> getBestClients(Integer howMany, LocalDate since) {
        return clientRepository.getBestClients(since, PageRequest.of(0, howMany));
    }
}
