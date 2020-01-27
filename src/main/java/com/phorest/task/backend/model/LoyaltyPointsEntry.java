package com.phorest.task.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyPointsEntry {

    @Id
    private UUID entryId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDate dateAdded;

    private Integer loyaltyPoints;
}
