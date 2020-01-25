package com.phorest.task.backend.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Client {

    @Id
    private UUID id = UUID.randomUUID();

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Gender gender;

    private boolean banned = false;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

}
