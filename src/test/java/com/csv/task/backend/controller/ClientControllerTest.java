package com.csv.task.backend.controller;

import com.csv.task.backend.dto.ClientWithLoyaltyPointsDto;
import com.csv.task.backend.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientService clientService;

    @Test
    void requestingForZeroBestClientsShouldProduceBadRequest() throws Exception {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/clients/top/{howMany}/since/{sinceWhen}", 0, "2011-01-01")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("incorrectNumberOfClients")
    void requestingForNonIntegerOrNegativeNumberOfClientsShouldProduceBadRequest(Object input) throws Exception {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/clients/top/{howMany}/since/{sinceWhen}", input, "2011-01-01")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> incorrectNumberOfClients() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(0.5),
                Arguments.of("A"),
                Arguments.of(1.5)
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectSinceWhenDate")
    void providingSinceWhenDateInWrongFormatOrIncorrectTypeShouldProduceBadRequest(Object input) throws Exception {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/clients/top/{howMany}/since/{sinceWhen}", 3, input)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> incorrectSinceWhenDate() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of("2011-01"),
                Arguments.of("A"),
                Arguments.of("1.5"),
                Arguments.of("2000.03.01")
        );
    }

    @Test
    void providingCorrectNumberOfClientsAndSinceWhenDateShouldProduceJsonListOfBestClients() throws Exception {
        List<ClientWithLoyaltyPointsDto> expected = new LinkedList<>();
        when(clientService.getBestClients(any(), any())).thenReturn(expected);
        mvc.perform( MockMvcRequestBuilders
                .get("/api/v1/clients/top/{howMany}/since/{sinceWhen}", 3, "2001-01-01")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
