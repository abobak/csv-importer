package com.phorest.task.backend.service;


import com.phorest.task.backend.dto.AppointmentDto;
import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.dto.PurchaseDto;
import com.phorest.task.backend.dto.ServiceDto;
import com.phorest.task.backend.mapper.AppointmentMapper;
import com.phorest.task.backend.mapper.ClientMapper;
import com.phorest.task.backend.mapper.CsvToDtoMapper;
import com.phorest.task.backend.model.Appointment;
import com.phorest.task.backend.model.Client;
import com.phorest.task.backend.repository.AppointmentRepository;
import com.phorest.task.backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final CsvToDtoMapper csvToDtoMapper = new CsvToDtoMapper();

    private final ClientMapper clientMapper;

    private final AppointmentMapper appointmentMapper;

    private final ClientRepository clientRepository;

    private final AppointmentRepository appointmentRepository;

    @Transactional
    public void importClients(byte[] payload) throws IOException {
        List<ClientDto> dtos = csvToDtoMapper.csvToDtoList(payload, ClientDto.class);
        List<Client> clients = clientMapper.dtoListToClientList(dtos);
        clientRepository.saveAll(clients);
    }

    @Transactional
    public void importAppointments(byte[] payload) throws IOException {
        List<AppointmentDto> dtos = csvToDtoMapper.csvToDtoList(payload, AppointmentDto.class);
        Map<UUID, Client> clientsMap = getClientsRelatedToAppointments(dtos);
        List<Appointment> appointmentsToImport = new LinkedList<>();
        for (AppointmentDto dto : dtos) {
            Client c = clientsMap.get(dto.getClientId());
            Appointment appointment = appointmentMapper.dtoToAppointment(dto, c);
            appointmentsToImport.add(appointment);
            c.getAppointments().add(appointment);
        }

        clientRepository.saveAll(new ArrayList<>(clientsMap.values()));
        appointmentRepository.saveAll(appointmentsToImport);
    }

    private Map<UUID, Client> getClientsRelatedToAppointments(List<AppointmentDto> dtos) {
        Set<UUID> clientsIds = new HashSet<>();
        for (AppointmentDto dto : dtos) {
            clientsIds.add(dto.getClientId());
        }
        List<Client> clients = clientRepository.findAllById(clientsIds);
        return clients.stream().collect(Collectors.toMap(Client::getId, c -> c, (a, b) -> b));
    }

    public void importServices(byte[] payload) throws IOException {
        List<ServiceDto> dtos = csvToDtoMapper.csvToDtoList(payload, ServiceDto.class);
    }

    public void importPurchases(byte[] payload) throws IOException {
        List<PurchaseDto> dtos = csvToDtoMapper.csvToDtoList(payload, PurchaseDto.class);
    }

}
