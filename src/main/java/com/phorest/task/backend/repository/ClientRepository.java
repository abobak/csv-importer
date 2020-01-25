package com.phorest.task.backend.repository;

import com.phorest.task.backend.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("select client from Client client left join fetch client.appointments where client.id = :id")
    Client getClientWithAppointments(@Param("id") UUID id);
}
