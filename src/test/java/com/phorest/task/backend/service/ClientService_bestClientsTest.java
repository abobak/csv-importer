package com.phorest.task.backend.service;

import com.phorest.task.backend.dto.ClientWithLoyaltyPointsDto;
import com.phorest.task.backend.model.Appointment;
import com.phorest.task.backend.model.Client;
import com.phorest.task.backend.model.Purchase;
import com.phorest.task.backend.model.Service;
import com.phorest.task.backend.repository.AppointmentRepository;
import com.phorest.task.backend.repository.ClientRepository;
import com.phorest.task.backend.repository.PurchaseRepository;
import com.phorest.task.backend.repository.ServiceRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.phorest.task.backend.model.Gender.Female;
import static com.phorest.task.backend.model.Gender.Male;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ClientService_bestClientsTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientService clientService;

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

    private DateTimeFormatter justDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Client c1, c2;

    @BeforeEach
    void setUp() {
        createTwoClientsAndThreeAppointmentsWithPurchasesAndServices();
    }

    private void createTwoClientsAndThreeAppointmentsWithPurchasesAndServices() {
        c1 = new Client(UUID.fromString("e0b8ebfc-6e57-4661-9546-328c644a3764"), "Dori", "Dietrich", "patricia@keeling.net", "(272) 301-6356", Male, false, new LinkedList<>(), new LinkedList<>());
        c2 = new Client(UUID.fromString("263f67fa-ce8f-447b-98cf-317656542216"), "Krystle", "Harvey", "romeorunolfon@corwin.co", "1-580-807-6075", Female, false, new LinkedList<>(), new LinkedList<>());

        Appointment a1 = new Appointment(UUID.randomUUID(), null, ZonedDateTime.parse("2017-08-04 17:15:00 +0100", df), ZonedDateTime.parse("2017-08-04 18:15:00 +0100", df), new LinkedList<>(), new LinkedList<>());
        Appointment a2 = new Appointment(UUID.randomUUID(), null, ZonedDateTime.parse("2017-09-04 17:15:00 +0100", df), ZonedDateTime.parse("2017-09-04 18:15:00 +0100", df), new LinkedList<>(), new LinkedList<>());
        Appointment a3 = new Appointment(UUID.randomUUID(), null, ZonedDateTime.parse("2017-07-04 17:15:00 +0100", df), ZonedDateTime.parse("2017-07-04 18:15:00 +0100", df), new LinkedList<>(), new LinkedList<>());

        c1.addAppointment(a1);
        c1.addAppointment(a2);
        c2.addAppointment(a3);

        Purchase a1p1 = new Purchase(UUID.randomUUID(), null, "Purchase 1 app 1", 10.0, 1);
        Purchase a1p2 = new Purchase(UUID.randomUUID(), null, "Purchase 2 app 1", 10.0, 1);
        Purchase a1p3 = new Purchase(UUID.randomUUID(), null, "Purchase 3 app 1", 10.0, 1);
        Service a1s1 = new Service(UUID.randomUUID(), null, "Service 1 app 1", 20.0, 100);

        a1.addPurchase(a1p1);
        a1.addPurchase(a1p2);
        a1.addPurchase(a1p3);
        a1.addService(a1s1);

        Service a2s1 = new Service(UUID.randomUUID(), null, "Service 1 app 2", 300.0, 100);
        a2.addService(a2s1);

        Purchase a3p1 = new Purchase(UUID.randomUUID(), null, "Purchase 1 app 3", 20.0, 1);
        Purchase a3p2 = new Purchase(UUID.randomUUID(), null, "Purchase 2 app 3", 20.0, 1);
        a3.addPurchase(a3p1);
        a3.addPurchase(a3p2);

        clientRepository.saveAll(Lists.list(c1, c2));
        appointmentRepository.saveAll(Lists.list(a1, a2, a3));
        purchaseRepository.saveAll(Lists.list(a1p1, a1p2, a1p3, a3p1, a3p2));
        serviceRepository.saveAll(Lists.list(a1s1, a2s1));
    }

    @Test
    void shouldReturnBothClientsIfLimitIsGreaterThanOneAndBothHaveEarnedLoyaltyPointsBeforeDate() {
        LocalDate since = LocalDate.parse("2017-03-01", justDate);
        List<ClientWithLoyaltyPointsDto> clients = clientService.getBestClients(3, since);
        assertEquals(2, clients.size());
    }

    @Test
    void shouldReturnOneClientWhenTheOtherHadNoLoyaltyPointEntriesAfterDate() {
        LocalDate since = LocalDate.parse("2017-09-04", justDate);
        List<ClientWithLoyaltyPointsDto> clients = clientService.getBestClients(20, since);
        assertEquals(1, clients.size());
        assertEquals(c1.getId(), clients.get(0).getId());
    }

    @Test
    void shouldReturnCorrectLoyaltyPointsNumberForEntriesAfterDate() {
        LocalDate since = LocalDate.parse("2017-08-01", justDate);
        List<ClientWithLoyaltyPointsDto> clients = clientService.getBestClients(20, since);
        Long pointsAccumulatedInApppointment1and2ByClient1 = 203L;
        assertEquals(pointsAccumulatedInApppointment1and2ByClient1, clients.get(0).getLoyaltyPoints());
    }

    @Test
    void shouldReturnEmptyListIfThereIsNoAppointmentWithLoyaltyPointsAfterDate() {
        LocalDate sinceFarFuture = LocalDate.parse("3011-08-01", justDate);
        assertTrue(clientService.getBestClients(20, sinceFarFuture).isEmpty());
    }

}
