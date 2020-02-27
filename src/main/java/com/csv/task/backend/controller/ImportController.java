package com.csv.task.backend.controller;

import com.csv.task.backend.api.ImportApi;
import com.csv.task.backend.service.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImportController implements ImportApi {

    private final ImportService importService;

    @PostMapping(path = "/api-internal/v1/import/clients")
    public void importClients(@RequestParam("file") MultipartFile f) throws IOException {
        importService.importClients(f.getBytes());
    }

    @PostMapping(path = "/api-internal/v1/import/appointments")
    public void importAppointments(@RequestParam("file") MultipartFile f) throws IOException {
        importService.importAppointments(f.getBytes());
    }

    @PostMapping(path = "/api-internal/v1/import/services")
    public void importServices(@RequestParam("file") MultipartFile f) throws IOException {
        importService.importServices(f.getBytes());
    }

    @PostMapping(path = "/api-internal/v1/import/purchases")
    public void importPurchases(@RequestParam("file") MultipartFile f) throws IOException {
        importService.importPurchases(f.getBytes());
    }

}
