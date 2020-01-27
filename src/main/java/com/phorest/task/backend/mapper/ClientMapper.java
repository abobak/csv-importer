package com.phorest.task.backend.mapper;

import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", expression = "java(java.util.Objects.isNull(dto.getId()) ? java.util.UUID.randomUUID() : dto.getId())")
    @Mapping(target = "appointments", expression = "java(new java.util.ArrayList())")
    @Mapping(target = "loyaltyPointsEntries", expression = "java(new java.util.ArrayList())")
    Client dtoToClient(ClientDto dto);

    ClientDto clientToDto(Client client);

    List<ClientDto> clientListToDtoList(List<Client> clients);

    List<Client> dtoListToClientList(List<ClientDto> dtos);
}
