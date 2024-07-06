package com.example.taxcalculator.service;

import com.example.taxcalculator.dto.OperationDto;
import com.example.taxcalculator.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class TaxServiceTest {

    private TaxService taxService;

    @BeforeEach
    void setUp() {
        OperationRepository repository = mock(OperationRepository.class);
        taxService = new TaxService(repository);
    }

    @Test
    void testCase1() {
        List<OperationDto> operations = Arrays.asList(
                new OperationDto("buy", new BigDecimal("10.00"), 100),
                new OperationDto("sell", new BigDecimal("15.00"), 50),
                new OperationDto("sell", new BigDecimal("15.00"), 50)
        );
        List<BigDecimal> expectedTaxes = Arrays.asList(
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        );
        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);

        List<BigDecimal> formattedActualTaxes = actualTaxes.stream()
                .map(tax -> tax.setScale(2, RoundingMode.HALF_UP))
                .collect(Collectors.toList());

        assertEquals(expectedTaxes, formattedActualTaxes);
    }

    @Test
    void testCase2() {
        List<OperationDto> operations = Arrays.asList(
                new OperationDto("buy", new BigDecimal("10.00"), 10000),
                new OperationDto("sell", new BigDecimal("20.00"), 5000),
                new OperationDto("sell", new BigDecimal("5.00"), 5000)
        );
        List<BigDecimal> expectedTaxes = Arrays.asList(
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                new BigDecimal("10000.00"),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        );
        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);

        List<BigDecimal> formattedActualTaxes = actualTaxes.stream()
                .map(tax -> tax.setScale(2, RoundingMode.HALF_UP))
                .collect(Collectors.toList());

        assertEquals(expectedTaxes, formattedActualTaxes);
    }

    @Test
    void testCase3() {
        List<OperationDto> operations = Arrays.asList(
                new OperationDto("buy", new BigDecimal("10.00"), 10000),
                new OperationDto("sell", new BigDecimal("5.00"), 5000),
                new OperationDto("sell", new BigDecimal("20.00"), 3000)
        );
        List<BigDecimal> expectedTaxes = Arrays.asList(
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                new BigDecimal("1000.00")
        );
        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);

        List<BigDecimal> formattedActualTaxes = actualTaxes.stream()
                .map(tax -> tax.setScale(2, RoundingMode.HALF_UP))
                .collect(Collectors.toList());

        assertEquals(expectedTaxes, formattedActualTaxes);
    }

    @Test
    void testCase4() {
        List<OperationDto> operations = Arrays.asList(
                new OperationDto("buy", new BigDecimal("10.00"), 10000),
                new OperationDto("buy", new BigDecimal("25.00"), 5000),
                new OperationDto("sell", new BigDecimal("15.00"), 10000)
        );
        List<BigDecimal> expectedTaxes = Arrays.asList(
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        );
        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);
        assertEquals(expectedTaxes, actualTaxes);
    }

    @Test
    void testCase5() {
        List<OperationDto> operations = Arrays.asList(
                new OperationDto("buy", new BigDecimal("10.00"), 10000),
                new OperationDto("buy", new BigDecimal("25.00"), 5000),
                new OperationDto("sell", new BigDecimal("15.00"), 10000),
                new OperationDto("sell", new BigDecimal("25.00"), 5000)
        );
        List<BigDecimal> expectedTaxes = Arrays.asList(
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                new BigDecimal("10000.00")
        );
        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);
        assertEquals(expectedTaxes, actualTaxes);
    }

//    @Test
//    void testCase6() {
//        List<OperationDto> operations = Arrays.asList(
//                new OperationDto("buy", new BigDecimal("10.00"), 10000),
//                new OperationDto("sell", new BigDecimal("2.00"), 5000),
//                new OperationDto("sell", new BigDecimal("20.00"), 2000),
//                new OperationDto("sell", new BigDecimal("20.00"), 2000),
//                new OperationDto("sell", new BigDecimal("25.00"), 1000)
//        );
//        List<BigDecimal> expectedTaxes = Arrays.asList(
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                new BigDecimal("3000.00")
//        );
//        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);
//
//        List<BigDecimal> formattedActualTaxes = actualTaxes.stream()
//                .map(tax -> tax.setScale(2, RoundingMode.HALF_UP))
//                .collect(Collectors.toList());
//
//        assertEquals(expectedTaxes, formattedActualTaxes);
//    }
//
//    @Test
//    void testCase7() {
//        List<OperationDto> operations = Arrays.asList(
//                new OperationDto("buy", new BigDecimal("10.00"), 10000),
//                new OperationDto("sell", new BigDecimal("2.00"), 5000),
//                new OperationDto("sell", new BigDecimal("20.00"), 2000),
//                new OperationDto("sell", new BigDecimal("20.00"), 2000),
//                new OperationDto("sell", new BigDecimal("25.00"), 1000),
//                new OperationDto("buy", new BigDecimal("20.00"), 10000),
//                new OperationDto("sell", new BigDecimal("15.00"), 5000)
//        );
//        List<BigDecimal> expectedTaxes = Arrays.asList(
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                new BigDecimal("3000.00"),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
//        );
//        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);
//        assertEquals(expectedTaxes, actualTaxes);
//    }
//
//    @Test
//    void testCase8() {
//        List<OperationDto> operations = Arrays.asList(
//                new OperationDto("buy", BigDecimal.valueOf(10.00), 10000),
//                new OperationDto("sell", BigDecimal.valueOf(50.00), 10000),
//                new OperationDto("buy", BigDecimal.valueOf(20.00), 10000),
//                new OperationDto("sell", BigDecimal.valueOf(50.00), 10000)
//        );
//        List<BigDecimal> expectedTaxes = Arrays.asList(
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.valueOf(80000.00).setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.valueOf(60000.00).setScale(2, RoundingMode.HALF_UP)
//        );
//
//        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);
//
//        assertEquals(expectedTaxes, actualTaxes);
//    }
}
