package com.csv.task.backend.service;

import com.csv.task.backend.model.*;
import com.csv.task.backend.repository.AppointmentRepository;
import com.csv.task.backend.repository.ClientRepository;
import com.csv.task.backend.repository.PurchaseRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ImportService_importPurchasesTest {

    @Autowired
    private ImportService importService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    private UUID appointmentId;

    private String csvPayload = "id,appointment_id,name,price,loyalty_points\n" +
            "d2d3b92d-f9b5-48c5-bf31-88c28e3b73ac,7416ebc3-12ce-4000-87fb-82973722ebf4,Shampoo,19.5,20\n" +
            "5bd41515-4c91-4ea7-9b0e-266617097852,7416ebc3-12ce-4000-87fb-82973722ebf4,Moistureiser,30.0,30";

    private Integer loyaltyPointsInImportPayload = 50;

    private UUID clientId;

    @BeforeEach
    void setUp() {
        createClientAndAppointment();
    }

    private void createClientAndAppointment() {
        Client c = new Client(UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764"), "Dori", "Dietrich", "patricia@keeling.net", "(272) 301-6356", Gender.Male, false, new LinkedList<>(), new LinkedList<>());
        Appointment a = new Appointment(UUID.fromString("7416ebc3-12ce-4000-87fb-82973722ebf4"), null, ZonedDateTime.parse("2017-08-04 17:15:00 +0100", df), ZonedDateTime.parse("2017-08-04 18:15:00 +0100", df), new LinkedList<>(), new LinkedList<>());
        c.addAppointment(a);
        clientRepository.save(c);
        appointmentRepository.save(a);
        appointmentId = a.getId();
        clientId = c.getId();
    }

    @Test
    void shouldCorrectlyImportPurchasesAndStoreThemInDatabase() throws IOException {
        // when
        importService.importPurchases(csvPayload.getBytes());
        // then
        List<Purchase> expectedPurchases = purchaseRepository.findAllById(Lists.list(
                UUID.fromString("d2d3b92d-f9b5-48c5-bf31-88c28e3b73ac"),
                UUID.fromString("5bd41515-4c91-4ea7-9b0e-266617097852")));
        assertEquals(2, expectedPurchases.size());
    }

    @Test
    void whenPurchasesAreImportedTheyShouldBeAccessibleFromParentAppointment() throws IOException {
        // when
        importService.importPurchases(csvPayload.getBytes());
        // then
        Appointment ap = appointmentRepository.getAppointmentWithPurchases(appointmentId);
        assertEquals(2, ap.getPurchases().size());
    }

    @Test
    void whenPurchasesAreImportedUserShouldBeGrantedLoyaltyPoints() throws IOException {
        // when
        importService.importServices(csvPayload.getBytes());
        // then
        Client c = clientRepository.getClientWithLoyaltyPointEntries(clientId);
        Integer grantedLoyaltyPoints = c.getLoyaltyPointsEntries().stream().mapToInt(LoyaltyPointsEntry::getLoyaltyPoints).sum();
        assertEquals(loyaltyPointsInImportPayload, grantedLoyaltyPoints);
    }

}
