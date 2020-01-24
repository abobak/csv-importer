package com.phorest.task.backend.dto;

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

    private UUID appointmentId;

    private String name;

    private Double price;

    private Integer loyaltyPoints;
}
