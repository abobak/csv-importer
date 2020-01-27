package com.phorest.task.backend.api;

import com.phorest.task.backend.dto.ClientDto;
import com.phorest.task.backend.dto.ClientWithLoyaltyPointsDto;

import java.util.List;
import java.util.UUID;

public interface ClientApi {

    ClientDto getClient(UUID clientId);

    ClientDto updateClient(UUID clientId, ClientDto dto);

    ClientDto createClient(ClientDto dto);

    void deleteClient(UUID clientId);

    List<ClientWithLoyaltyPointsDto> getTopClientsSinceDate(Integer howMany, String sinceWhen);
}
