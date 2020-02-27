package com.csv.task.backend.mapper;

import com.csv.task.backend.dto.ServiceDto;
import com.csv.task.backend.model.Appointment;
import com.csv.task.backend.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "appointment", source = "a")
    Service dtoToService(ServiceDto dto, Appointment a);

}
