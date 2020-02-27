package com.csv.task.backend.mapper;


import com.csv.task.backend.dto.AppointmentDto;
import com.csv.task.backend.dto.ClientDto;
import com.csv.task.backend.dto.PurchaseDto;
import com.csv.task.backend.model.Gender;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvToDtoMapperTest {
    private final CsvToDtoMapper mapper = new CsvToDtoMapper();

    @Test
    void shouldMapBytePayloadToClientDto() throws IOException {
        String minimumCsvPayload =
                "id,first_name,last_name,email,phone,gender,banned\n" +
                "e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false";

        UUID expectedId = UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764");
        String expectedFirstName = "Dori";
        String expectedLastName = "Dietrich";
        String expectedEmail = "patrica@keeling.net";
        String expectedPhone = "(272) 301-6356";
        Gender expectedGender = Gender.Male;
        boolean expectedBanned = false;
        List<ClientDto> dtos = mapper.csvToDtoList(minimumCsvPayload.getBytes(), ClientDto.class);
        ClientDto dto = dtos.get(0);
        assertEquals(expectedBanned, dto.isBanned());
        assertEquals(expectedEmail, dto.getEmail());
        assertEquals(expectedFirstName, dto.getFirstName());
        assertEquals(expectedGender, dto.getGender());
        assertEquals(expectedId, dto.getId());
        assertEquals(expectedLastName, dto.getLastName());
        assertEquals(expectedPhone, dto.getPhone());
    }

    @Test
    void shouldProduceTwoClientsFromTwoEntries() throws IOException {
        String csvPayload =
                "id,first_name,last_name,email,phone,gender,banned\n" +
                "e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false\n" +
                "104fdf33-c8a2-4f1c-b371-3e9c2facdfa0,Gordon,Hammes,glen@cummerata.co,403-844-1643,Male,false";
        int expectedNumberOfClients = 2;
        List<ClientDto> dtos = mapper.csvToDtoList(csvPayload.getBytes(), ClientDto.class);
        assertEquals(expectedNumberOfClients, dtos.size());
    }

    @Test
    void shouldMapBytePayloadToAppointmentDto() throws IOException {
        String csvPayload =
                "id,client_id,start_time,end_time\n" +
                "7416ebc3-12ce-4000-87fb-82973722ebf4,263f67fa-ce8f-447b-98cf-317656542216,2016-02-07 17:15:00 +0000,2016-02-07 20:15:00 +0000";
        UUID expectedId = UUID.fromString("7416ebc3-12ce-4000-87fb-82973722ebf4");
        UUID expectedClientId = UUID.fromString("263f67fa-ce8f-447b-98cf-317656542216");
        String expectedStartTime = "2016-02-07 17:15:00 +0000";
        String expectedEndTime = "2016-02-07 20:15:00 +0000";
        List<AppointmentDto> dtos = mapper.csvToDtoList(csvPayload.getBytes(), AppointmentDto.class);
        AppointmentDto dto = dtos.get(0);
        assertEquals(expectedClientId, dto.getClientId());
        assertEquals(expectedEndTime, dto.getEndTime());
        assertEquals(expectedId, dto.getId());
        assertEquals(expectedStartTime, dto.getStartTime());
    }

    @Test
    void shouldMapBytePayloadToPurchaseDto() throws IOException {
        String csvPayload =
                "id,appointment_id,name,price,loyalty_points\n" +
                "d2d3b92d-f9b5-48c5-bf31-88c28e3b73ac,7416ebc3-12ce-4000-87fb-82973722ebf4,Shampoo,19.5,20";
        UUID expectedId = UUID.fromString("d2d3b92d-f9b5-48c5-bf31-88c28e3b73ac");
        UUID expectedAppointmentId = UUID.fromString("7416ebc3-12ce-4000-87fb-82973722ebf4");
        String expectedName = "Shampoo";
        Double expectedPrice = 19.5;
        Integer expectedLoyaltyPoints = 20;
        List<PurchaseDto> dtos = mapper.csvToDtoList(csvPayload.getBytes(), PurchaseDto.class);
        PurchaseDto dto = dtos.get(0);
        assertEquals(expectedAppointmentId, dto.getAppointmentId());
        assertEquals(expectedId, dto.getId());
        assertEquals(expectedLoyaltyPoints, dto.getLoyaltyPoints());
        assertEquals(expectedName, dto.getName());
        assertEquals(expectedPrice, dto.getPrice());
    }

    @Test
    void shouldMapBytePayloadToServiceDto() throws IOException {
        String csvPayload =
                "id,appointment_id,name,price,loyalty_points\n" +
                        "f1fc7009-0c44-4f89-ac98-5de9ce58095c,7416ebc3-12ce-4000-87fb-82973722ebf4,Full Head Colour,85.0,80";
        UUID expectedId = UUID.fromString("f1fc7009-0c44-4f89-ac98-5de9ce58095c");
        UUID expectedAppointmentId = UUID.fromString("7416ebc3-12ce-4000-87fb-82973722ebf4");
        String expectedName = "Full Head Colour";
        Double expectedPrice = 85.0;
        Integer expectedLoyaltyPoints = 80;
        List<PurchaseDto> dtos = mapper.csvToDtoList(csvPayload.getBytes(), PurchaseDto.class);
        PurchaseDto dto = dtos.get(0);
        assertEquals(expectedAppointmentId, dto.getAppointmentId());
        assertEquals(expectedId, dto.getId());
        assertEquals(expectedLoyaltyPoints, dto.getLoyaltyPoints());
        assertEquals(expectedName, dto.getName());
        assertEquals(expectedPrice, dto.getPrice());
    }

}
