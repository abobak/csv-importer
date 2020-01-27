package com.phorest.task.backend.mapper;

import com.phorest.task.backend.dto.AppointmentDto;
import com.phorest.task.backend.model.Appointment;
import com.phorest.task.backend.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startTime", source = "startTime", dateFormat = "yyyy-MM-dd HH:mm:ss Z")
    @Mapping(target = "endTime", source = "endTime", dateFormat = "yyyy-MM-dd HH:mm:ss Z")
    AppointmentDto appointmentToDo(Appointment appointment);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "startTime", source = "dto.startTime", dateFormat = "yyyy-MM-dd HH:mm:ss Z")
    @Mapping(target = "endTime", source = "dto.endTime", dateFormat = "yyyy-MM-dd HH:mm:ss Z")
    @Mapping(target = "client", source = "c")
    Appointment dtoToAppointment(AppointmentDto dto, Client c);

}
