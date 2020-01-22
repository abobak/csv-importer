package com.phorest.task.backend.dto;

import com.phorest.task.backend.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Gender gender;

    private boolean banned = false;

}
