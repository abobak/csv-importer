package com.phorest.task.backend.mapper;


import com.phorest.task.backend.dto.AppointmentDto;
import com.phorest.task.backend.model.Appointment;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppointmentMapperTest {

    AppointmentMapper mapper = new AppointmentMapperImpl();

    @Test
    void shouldCorrectlyMapDtoToEntity() {
        AppointmentDto dto = new AppointmentDto(
                UUID.fromString("2878e8b1-23bf-4225-abaa-db4f06bdee61"),
                UUID.fromString("78afc583-c1ea-472b-b24d-4316e74d071d"),
                "2016-03-21 10:00:00 +0100",
                "2016-03-21 10:20:00 +0100"
        );
        Appointment entity = mapper.dtoToAppointment(dto, null);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getStartTime(), entity.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")));
        assertEquals(dto.getEndTime(), entity.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")));
    }

}
