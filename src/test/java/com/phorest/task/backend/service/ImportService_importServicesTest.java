package com.phorest.task.backend.service;

import com.phorest.task.backend.model.Appointment;
import com.phorest.task.backend.model.Client;
import com.phorest.task.backend.model.LoyaltyPointsEntry;
import com.phorest.task.backend.model.Service;
import com.phorest.task.backend.repository.AppointmentRepository;
import com.phorest.task.backend.repository.ClientRepository;
import com.phorest.task.backend.repository.ServiceRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.phorest.task.backend.model.Gender.Male;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ImportService_importServicesTest {

    @Autowired
    private ImportService importService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    private UUID appointmentId;

    private Integer loyaltyPointsInImportPayload = 100;

    private String csvPayload = "id,appointment_id,name,price,loyalty_points\n" +
            "f1fc7009-0c44-4f89-ac98-5de9ce58095c,7416ebc3-12ce-4000-87fb-82973722ebf4,Full Head Colour,85.0,80\n" +
            "4c6b7ed3-910a-43c0-a1b6-27e835907e30,7416ebc3-12ce-4000-87fb-82973722ebf4,Scalp Massage,21.0,20";

    private UUID clientId;

    @BeforeEach
    void setUp() {
        createClientAndAppointment();
    }

    private void createClientAndAppointment() {
        Client c = new Client(UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764"), "Dori", "Dietrich", "patricia@keeling.net", "(272) 301-6356", Male, false, new LinkedList<>(), new LinkedList<>());
        Appointment a = new Appointment(UUID.fromString("7416ebc3-12ce-4000-87fb-82973722ebf4"), null, ZonedDateTime.parse("2017-08-04 17:15:00 +0100", df), ZonedDateTime.parse("2017-08-04 18:15:00 +0100", df), new LinkedList<>(), new LinkedList<>());
        c.addAppointment(a);
        clientRepository.save(c);
        appointmentRepository.save(a);
        appointmentId = a.getId();
        clientId = c.getId();
    }

    @Test
    void shouldCorrectlyImportServicesAndStoreThemInDatabase() throws IOException {
        // when
        importService.importServices(csvPayload.getBytes());
        // then
        List<Service> expectedServices = serviceRepository.findAllById(Lists.list(
                UUID.fromString("f1fc7009-0c44-4f89-ac98-5de9ce58095c"),
                UUID.fromString("4c6b7ed3-910a-43c0-a1b6-27e835907e30")));
        assertEquals(2, expectedServices.size());
    }

    @Test
    void whenServicesAreImportedTheyShouldBeAccessibleFromParentAppointment() throws IOException {
        // when
        importService.importServices(csvPayload.getBytes());
        // then
        Appointment ap = appointmentRepository.getAppointmentWithServices(appointmentId);
        assertEquals(2, ap.getServices().size());
    }

    @Test
    void whenServicesAreImportedUserShouldBeGrantedLoyaltyPoints() throws IOException {
        // when
        importService.importServices(csvPayload.getBytes());
        // then
        Client c = clientRepository.getClientWithLoyaltyPointEntries(clientId);
        Integer grantedLoyaltyPoints = c.getLoyaltyPointsEntries().stream().mapToInt(LoyaltyPointsEntry::getLoyaltyPoints).sum();
        assertEquals(loyaltyPointsInImportPayload, grantedLoyaltyPoints);
    }

}
