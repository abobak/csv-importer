package com.phorest.task.backend.service;


import com.phorest.task.backend.dto.AppointmentDto;
import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.mapper.CsvToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final CsvToDtoMapper mapper = new CsvToDtoMapper();

    public void importClients(byte[] payload) throws IOException {
        List<ClientDto> dtos = mapper.csvToDtoList(payload, ClientDto.class);
    }

    public void importAppointments(byte[] payload) throws IOException {
        List<AppointmentDto> dtos = mapper.csvToDtoList(payload, AppointmentDto.class);
    }

    public void importServices(byte[] payload) throws IOException {

    }

    public void importPurchases(byte[] payload) throws IOException {

    }

}
