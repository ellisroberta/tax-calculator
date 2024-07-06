package com.example.taxcalculator.controller;

import com.example.taxcalculator.dto.OperationDto;
import com.example.taxcalculator.service.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TaxControllerTest {

    @Mock
    private TaxService taxService;

    @InjectMocks
    private TaxController taxController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateTaxes() {
        List<OperationDto> operations = Arrays.asList(
                new OperationDto("buy", new BigDecimal("10.00"), 10000),
                new OperationDto("sell", new BigDecimal("5.00"), 5000),
                new OperationDto("sell", new BigDecimal("20.00"), 3000)
        );
        List<BigDecimal> expectedTaxes = Arrays.asList(
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                new BigDecimal("1000.00").setScale(2, RoundingMode.HALF_UP)
        );
        when(taxService.calculateTaxes(operations)).thenReturn(expectedTaxes);

        ResponseEntity<List<BigDecimal>> responseEntity = taxController.calculateTaxes(operations);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedTaxes, responseEntity.getBody());
    }
}