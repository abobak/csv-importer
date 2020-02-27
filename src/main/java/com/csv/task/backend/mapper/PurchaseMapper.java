package com.csv.task.backend.mapper;

import com.csv.task.backend.dto.PurchaseDto;
import com.csv.task.backend.model.Appointment;
import com.csv.task.backend.model.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "appointment", source = "a")
    Purchase dtoToPurchase(PurchaseDto dto, Appointment a);

}
