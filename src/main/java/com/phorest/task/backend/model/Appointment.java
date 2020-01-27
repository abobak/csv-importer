package com.phorest.task.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services;

    public LoyaltyPointsEntry addPurchase(Purchase p) {
        purchases.add(p);
        p.setAppointment(this);
        LoyaltyPointsEntry entry = new LoyaltyPointsEntry(p.getId(), null, this.getStartTime().toLocalDate(), p.getLoyaltyPoints());
        return this.client.addLoyaltyPointsEntry(entry);
    }

    public LoyaltyPointsEntry addService(Service s) {
        services.add(s);
        s.setAppointment(this);
        LoyaltyPointsEntry entry = new LoyaltyPointsEntry(s.getId(), null, this.getStartTime().toLocalDate(), s.getLoyaltyPoints());
        return this.client.addLoyaltyPointsEntry(entry);
    }
}
