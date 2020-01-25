package com.phorest.task.backend.mapper;

import com.phorest.task.backend.dto.AppointmentDto;
import com.phorest.task.backend.dto.PurchaseDto;
import com.phorest.task.backend.model.Appointment;
import com.phorest.task.backend.model.Client;
import com.phorest.task.backend.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(target = "id", source = "dto.id")
    Purchase dtoToPurchase(PurchaseDto dto, Appointment a);

}
