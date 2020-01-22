package com.phorest.task.backend.mapper;

import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.model.Client;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client dtoToClient(ClientDto dto);

    ClientDto clientToDto(Client client);

    List<ClientDto> clientListToDtoList(List<Client> all);
}
