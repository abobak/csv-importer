package com.phorest.task.backend.service;


import com.phorest.task.backend.dto.AppointmentDto;
import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.dto.PurchaseDto;
import com.phorest.task.backend.dto.ServiceDto;
import com.phorest.task.backend.mapper.ClientMapper;
import com.phorest.task.backend.mapper.CsvToDtoMapper;
import com.phorest.task.backend.model.Client;
import com.phorest.task.backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final CsvToDtoMapper csvToDtoMapper = new CsvToDtoMapper();

    private final ClientMapper clientMapper;

    private final ClientRepository clientRepository;

    @Transactional
    public void importClients(byte[] payload) throws IOException {
        List<ClientDto> dtos = csvToDtoMapper.csvToDtoList(payload, ClientDto.class);
        List<Client> clients = clientMapper.dtoListToClientList(dtos);
        clientRepository.saveAll(clients);
    }

    public void importAppointments(byte[] payload) throws IOException {
        List<AppointmentDto> dtos = csvToDtoMapper.csvToDtoList(payload, AppointmentDto.class);
    }

    public void importServices(byte[] payload) throws IOException {
        List<ServiceDto> dtos = csvToDtoMapper.csvToDtoList(payload, ServiceDto.class);
    }

    public void importPurchases(byte[] payload) throws IOException {
        List<PurchaseDto> dtos = csvToDtoMapper.csvToDtoList(payload, PurchaseDto.class);
    }

}
