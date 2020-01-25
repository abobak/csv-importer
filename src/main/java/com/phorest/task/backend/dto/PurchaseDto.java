package com.phorest.task.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {

    private UUID id;

    @JsonProperty("appointment_id")
    private UUID appointmentId;

    private String name;

    private Double price;

    @JsonProperty("loyalty_points")
    private Integer loyaltyPoints;
}
