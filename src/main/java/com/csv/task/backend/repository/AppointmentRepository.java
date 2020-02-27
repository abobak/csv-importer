package com.csv.task.backend.repository;

import com.csv.task.backend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("select appointment from Appointment appointment " +
            "left join fetch appointment.purchases purchases " +
            "where appointment.id = :id")
    Appointment getAppointmentWithPurchases(@Param("id") UUID id);

    @Query("select appointment from Appointment appointment " +
            "left join fetch appointment.services purchases " +
            "where appointment.id = :id")
    Appointment getAppointmentWithServices(@Param("id") UUID id);

    @Query("select appointment from Appointment appointment " +
            "left join fetch appointment.client client where appointment.id in :ids")
    List<Appointment> getAllByIdWithClient(@Param("ids") Set<UUID> ids);
}
