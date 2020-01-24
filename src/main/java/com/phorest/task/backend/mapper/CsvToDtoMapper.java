package com.phorest.task.backend.mapper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.util.List;

public class CsvToDtoMapper {

    public <T> List<T> csvToDtoList(byte[] payload, Class targetClass) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        MappingIterator<T> readValues = mapper.readerFor(targetClass).with(bootstrapSchema).readValues(payload);
        return readValues.readAll();
    }

}
