package com.phorest.task.backend.repository;

import com.phorest.task.backend.dto.ClientWithLoyaltyPointsDto;
import com.phorest.task.backend.model.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("select client from Client client left join fetch client.appointments where client.id = :id")
    Client getClientWithAppointments(@Param("id") UUID id);

    @Query("select client from Client client left join fetch client.loyaltyPointsEntries where client.id = :id")
    Client getClientWithLoyaltyPointEntries(@Param("id") UUID id);

    @Query("select distinct new com.phorest.task.backend.dto.ClientWithLoyaltyPointsDto(" +
            "client.id, client.firstName, client.lastName, " +
            "client.email, client.phone, client.gender, coalesce(sum(lpe.loyaltyPoints), 0)) " +
            "from Client client " +
            "left join client.loyaltyPointsEntries lpe " +
            "where lpe.dateAdded >= :since and client.banned = false " +
            "group by client " +
            "order by coalesce(sum(lpe.loyaltyPoints), 0) desc")
    List<ClientWithLoyaltyPointsDto> getBestClients(@Param("since") LocalDate since, Pageable pageable);

}
