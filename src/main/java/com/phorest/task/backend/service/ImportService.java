package com.phorest.task.backend.service;


import com.phorest.task.backend.dto.AppointmentDto;
import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.dto.PurchaseDto;
import com.phorest.task.backend.dto.ServiceDto;
import com.phorest.task.backend.mapper.*;
import com.phorest.task.backend.model.Appointment;
import com.phorest.task.backend.model.Client;
import com.phorest.task.backend.model.LoyaltyPointsEntry;
import com.phorest.task.backend.model.Purchase;
import com.phorest.task.backend.repository.*;
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

    private final PurchaseMapper purchaseMapper;

    private final ServiceMapper serviceMapper;

    private final ClientRepository clientRepository;

    private final AppointmentRepository appointmentRepository;

    private final PurchaseRepository purchaseRepository;

    private final ServiceRepository serviceRepository;

    private final LoyaltyPointsEntryRepository loyaltyPointsEntryRepository;

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
            c.addAppointment(appointment);
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

    public void importPurchases(byte[] payload) throws IOException {
        List<PurchaseDto> dtos = csvToDtoMapper.csvToDtoList(payload, PurchaseDto.class);
        Map<UUID, Appointment> appointmentsMap = getAppointmentsRelatedToPurchases(dtos);
        List<Purchase> purchasesToImport = new LinkedList<>();
        List<LoyaltyPointsEntry> newLoyaltyPointEntries = new LinkedList<>();
        for (PurchaseDto dto : dtos) {
            Appointment a = appointmentsMap.get(dto.getAppointmentId());
            Purchase p = purchaseMapper.dtoToPurchase(dto, a);
            LoyaltyPointsEntry entry = a.addPurchase(p);
            newLoyaltyPointEntries.add(entry);
            purchasesToImport.add(p);
        }
        List<Appointment> appointmentsToSave = new ArrayList<>(appointmentsMap.values());
        List<Client> clientsToSave = appointmentsToSave.stream().map(Appointment::getClient).collect(Collectors.toList());
        appointmentRepository.saveAll(appointmentsToSave);
        purchaseRepository.saveAll(purchasesToImport);
        clientRepository.saveAll(clientsToSave);
        loyaltyPointsEntryRepository.saveAll(newLoyaltyPointEntries);
    }

    private Map<UUID, Appointment> getAppointmentsRelatedToPurchases(List<PurchaseDto> dtos) {
        Set<UUID> appointmentIds = new HashSet<>();
        for (PurchaseDto dto : dtos) {
            appointmentIds.add(dto.getAppointmentId());
        }
        List<Appointment> appointments = appointmentRepository.getAllByIdWithClient(appointmentIds);
        return appointments.stream().collect(Collectors.toMap(Appointment::getId, c -> c, (a, b) -> b));
    }

    public void importServices(byte[] payload) throws IOException {
        List<ServiceDto> dtos = csvToDtoMapper.csvToDtoList(payload, ServiceDto.class);
        Map<UUID, Appointment> appointmentsMap = getAppointmentsRelatedToServices(dtos);
        List<com.phorest.task.backend.model.Service> servicesToImport = new LinkedList<>();
        List<LoyaltyPointsEntry> newLoyaltyPointEntries = new LinkedList<>();
        for (ServiceDto dto : dtos) {
            Appointment a = appointmentsMap.get(dto.getAppointmentId());
            com.phorest.task.backend.model.Service s = serviceMapper.dtoToService(dto, a);
            LoyaltyPointsEntry entry = a.addService(s);
            newLoyaltyPointEntries.add(entry);
            servicesToImport.add(s);
        }
        List<Appointment> appointmentsToSave = new ArrayList<>(appointmentsMap.values());
        List<Client> clientsToSave = appointmentsToSave.stream().map(Appointment::getClient).collect(Collectors.toList());
        appointmentRepository.saveAll(appointmentsToSave);
        serviceRepository.saveAll(servicesToImport);
        clientRepository.saveAll(clientsToSave);
        loyaltyPointsEntryRepository.saveAll(newLoyaltyPointEntries);
    }

    private Map<UUID, Appointment> getAppointmentsRelatedToServices(List<ServiceDto> dtos) {
        Set<UUID> appointmentIds = new HashSet<>();
        for (ServiceDto dto : dtos) {
            appointmentIds.add(dto.getAppointmentId());
        }
        List<Appointment> appointments = appointmentRepository.getAllByIdWithClient(appointmentIds);
        return appointments.stream().collect(Collectors.toMap(Appointment::getId, c -> c, (a, b) -> b));
    }

}
