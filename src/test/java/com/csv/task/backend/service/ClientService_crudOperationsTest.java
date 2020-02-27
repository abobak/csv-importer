package com.csv.task.backend.service;

import com.csv.task.backend.dto.ClientDto;
import com.csv.task.backend.mapper.ClientMapper;
import com.csv.task.backend.model.Client;
import com.csv.task.backend.model.Gender;
import com.csv.task.backend.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ClientService_crudOperationsTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    private String expectedFirstName = "expected first name";
    private String expectedLastName = "expected last name";
    private String expectedEmail = "expected@email";
    private String expectedPhone = "123";
    private Gender expectedGender = Gender.Female;
    private boolean banned = false;

    private ClientDto dto;

    @BeforeEach
    void setUp() {
        dto = new ClientDto(null, expectedFirstName, expectedLastName, expectedEmail, expectedPhone, expectedGender, banned);
    }

    @Test
    void shouldPersistNewClient() {
        // when
        ClientDto c = clientService.createNewClient(dto);

        // then
        assertNotNull(c.getId());
        assertEquals(c.getFirstName(), dto.getFirstName());
        Assertions.assertEquals(c.getGender(), dto.getGender());
        assertEquals(c.getLastName(), dto.getLastName());
        assertEquals(c.getPhone(), dto.getPhone());
        assertEquals(c.isBanned(), dto.isBanned());
    }

    @Test
    void shouldUpdateClient() {
        // given
        String expectedFirstName = "changed first name";
        String expectedLastName = "changed last name";
        String expectedEmail = "changed@email";
        String expectedPhone = "123456";
        Gender expectedGender = Gender.Male;
        Client c = clientRepository.save(clientMapper.dtoToClient(dto));

        // when
        dto.setId(c.getId());
        dto.setBanned(true);
        dto.setFirstName(expectedFirstName);
        dto.setLastName(expectedLastName);
        dto.setEmail(expectedEmail);
        dto.setPhone(expectedPhone);
        dto.setGender(expectedGender);
        dto = clientService.updateClient(dto);

        // then
        assertClientAndDtoAreEqual(c, dto);
    }

    @Test
    void shouldGetExistingClient() {
        // when
        Client c = clientRepository.save(clientMapper.dtoToClient(dto));

        // then
        ClientDto clientFromDatabase = clientService.getClientById(c.getId());
        assertClientAndDtoAreEqual(c, clientFromDatabase);
    }

    @Test
    void shouldRemoveClientFromSystem() {
        // given
        Client c = clientRepository.save(clientMapper.dtoToClient(dto));
        UUID id = c.getId();

        // when
        clientService.deleteClient(id);

        // then
        assertThrows(JpaObjectRetrievalFailureException.class, () -> clientService.getClientById(id));
    }

    private void assertClientAndDtoAreEqual(Client c, ClientDto dto) {
        assertEquals(c.getFirstName(), dto.getFirstName());
        Assertions.assertEquals(c.getGender(), dto.getGender());
        assertEquals(c.getLastName(), dto.getLastName());
        assertEquals(c.getPhone(), dto.getPhone());
        assertEquals(c.isBanned(), dto.isBanned());
    }

}
