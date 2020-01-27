package com.phorest.task.backend.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(value = "import", tags = { "import" })
public interface ImportApi {

    @ApiOperation(value = "Import clients from external system")
    void importClients(MultipartFile f) throws IOException;

    @ApiOperation(value = "Import appointments from external system")
    void importAppointments(MultipartFile f) throws IOException;

    @ApiOperation(value = "Import services from external system")
    void importServices(MultipartFile f) throws IOException;

    @ApiOperation(value = "Import purchases from external system")
    void importPurchases(MultipartFile f) throws IOException;

}
