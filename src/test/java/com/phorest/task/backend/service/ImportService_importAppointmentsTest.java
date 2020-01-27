package com.phorest.task.backend.service;

import com.phorest.task.backend.model.Appointment;
import com.phorest.task.backend.model.Client;
import com.phorest.task.backend.repository.AppointmentRepository;
import com.phorest.task.backend.repository.ClientRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.phorest.task.backend.model.Gender.Female;
import static com.phorest.task.backend.model.Gender.Male;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ImportService_importAppointmentsTest {

    @Autowired
    private ImportService importService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    String csvPayload = "id,client_id,start_time,end_time\n" +
            "67ce894a-9625-4ab7-8b91-17d83fb3fd10,e0b8ebfc-6e57-4661-9546-328c644a3764,2017-05-09 15:30:00 +0100,2017-05-09 18:30:00 +0100\n" +
            "a659bdd1-cd79-473a-aff4-a20c5760748d,e0b8ebfc-6e57-4661-9546-328c644a3764,2017-08-04 17:15:00 +0100,2017-08-04 18:15:00 +0100\n" +
            "5efa5a5d-d609-44bf-9190-a05c58b17196,263f67fa-ce8f-447b-98cf-317656542216,2017-09-05 15:00:00 +0100,2017-09-05 15:30:00 +0100";

    @BeforeEach
    void setUp() {
        createTwoClients();
    }

    @Test
    void shouldImportAppointmentsFromCsvData() throws IOException {
        // when
        importService.importAppointments(csvPayload.getBytes());
        // then
        List<Appointment> expectedAppointments = appointmentRepository.findAllById(Lists.list(
                UUID.fromString("67ce894a-9625-4ab7-8b91-17d83fb3fd10"),
                UUID.fromString("a659bdd1-cd79-473a-aff4-a20c5760748d"),
                UUID.fromString("5efa5a5d-d609-44bf-9190-a05c58b17196")
        ));
        assertEquals(3, expectedAppointments.size());
    }

    @Test
    void clientShouldHaveAccessToAppointmentsAddedDuringImport() throws IOException {
        // given
        UUID userWithTwoAppointmentsId = UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764");
        // when
        importService.importAppointments(csvPayload.getBytes());
        // then
        Client c = clientRepository.getClientWithAppointments(userWithTwoAppointmentsId);
        assertEquals(2, c.getAppointments().size());
    }


    private void createTwoClients() {
        Client c1 = new Client(UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764"), "Dori", "Dietrich", "patricia@keeling.net", "(272) 301-6356", Male, false, new LinkedList<>(), new LinkedList<>());
        Client c2 = new Client(UUID.fromString("263f67fa-ce8f-447b-98cf-317656542216"), "Krystle", "Harvey", "romeorunolfon@corwin.co", "1-580-807-6075", Female, false, new LinkedList<>(), new LinkedList<>());
        clientRepository.saveAll(Lists.newArrayList(c1, c2));
    }
}
