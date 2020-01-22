package com.phorest.task.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID clientId;

    LocalDateTime startDate;

    LocalDateTime endDate;

    @OneToMany(mappedBy = "appointmentId", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Service> services = new LinkedList<>();

    @OneToMany(mappedBy = "appointmentId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

}
