package com.phorest.task.backend.service;

import com.phorest.task.backend.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ImportService_importClientsTest {

    @Autowired
    private ImportService importService;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void whenClientIsImportedItIsSavedInDatabase() throws IOException {
        String csvPayload =
                "id,first_name,last_name,email,phone,gender,banned\n" +
                        "e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false";
        UUID expectedClientId = UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764");
        importService.importClients(csvPayload.getBytes());
        assertTrue(clientRepository.findById(expectedClientId).isPresent());
    }

}
