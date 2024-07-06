package com.example.taxcalculator.controller;

import com.example.taxcalculator.dto.OperationDto;
import com.example.taxcalculator.service.TaxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<List<BigDecimal>> calculateTaxes(@RequestBody List<OperationDto> operations) {
        List<BigDecimal> taxes = taxService.calculateTaxes(operations);
        return ResponseEntity.ok(taxes);
    }
}