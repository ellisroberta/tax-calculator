package com.example.taxcalculator.service;

import com.example.taxcalculator.dto.OperationDto;
import com.example.taxcalculator.repository.OperationRepository;
import com.example.taxcalculator.service.TaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxServiceTest {

    @Mock
    private OperationRepository repository;

    @InjectMocks
    private TaxService taxService;

    @Test
    @DisplayName("Case 1")
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

        assertTaxes(expectedTaxes, actualTaxes);
    }

    @Test
    @DisplayName("Case 2")
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

        assertTaxes(expectedTaxes, actualTaxes);
    }

    @Test
    @DisplayName("Case 1 + 2")
    void testCase1And2() {
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

        assertTaxes(expectedTaxes, actualTaxes);
    }

    @Test
    @DisplayName("Case 3")
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

        assertTaxes(expectedTaxes, actualTaxes);
    }

    @Test
    @DisplayName("Case 4")
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

        assertTaxes(expectedTaxes, actualTaxes);
    }

    @Test
    @DisplayName("Case 5")
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

        assertTaxes(expectedTaxes, actualTaxes);
    }

//    @Test
//    @DisplayName("Case 6")
//    void testCase6() {
//        List<OperationDto> operations = Arrays.asList(
//                new OperationDto("buy", new BigDecimal("10.00"), 10000),
//                new OperationDto("sell", new BigDecimal("2.00"), 5000),
//                new OperationDto("sell", new BigDecimal("20.00"), 2000),
//                new OperationDto("sell", new BigDecimal("20.00"), 2000),
//                new OperationDto("sell", new BigDecimal("25.00"), 1000)
//        );
//
//        when(repository.saveAll(ArgumentMatchers.anyList())).thenReturn(null);
//
//        List<BigDecimal> expectedTaxes = Arrays.asList(
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                new BigDecimal("3000.00")
//        );
//
//        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);
//
//        assertTaxes(expectedTaxes, actualTaxes);
//    }
//
//    @Test
//    @DisplayName("Case 7")
//    void testCase7() {
//        List<OperationDto> operations = Arrays.asList(
//                new OperationDto("buy", new BigDecimal("10.00"), 10000),
//                new OperationDto("sell", new BigDecimal("2.00"), 5000),
//                new OperationDto("sell", new BigDecimal("20.00"), 2000),
//                new OperationDto("sell", new BigDecimal("20.00"), 2000),
//                new OperationDto("sell", new BigDecimal("25.00"), 1000),
//                new OperationDto("buy", new BigDecimal("20.00"), 10000),
//                new OperationDto("sell", new BigDecimal("15.00"), 5000),
//                new OperationDto("sell", new BigDecimal("30.00"), 4350),
//                new OperationDto("sell", new BigDecimal("30.00"), 650)
//        );
//
//        when(repository.saveAll(ArgumentMatchers.anyList())).thenReturn(null);
//
//        List<BigDecimal> expectedTaxes = Arrays.asList(
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                new BigDecimal("3000.00"),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.valueOf(3700.00).setScale(2, RoundingMode.HALF_UP),
//                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
//        );
//
//        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);
//
//        assertTaxes(expectedTaxes, actualTaxes);
//    }

    @Test
    @DisplayName("Case 8")
    void testCase8() {
        List<OperationDto> operations = Arrays.asList(
                new OperationDto("buy", BigDecimal.valueOf(10.00), 10000),
                new OperationDto("sell", BigDecimal.valueOf(50.00), 10000),
                new OperationDto("buy", BigDecimal.valueOf(20.00), 10000),
                new OperationDto("sell", BigDecimal.valueOf(50.00), 10000)
        );
        List<BigDecimal> expectedTaxes = Arrays.asList(
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(80000.00).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(60000.00).setScale(2, RoundingMode.HALF_UP)
        );

        List<BigDecimal> actualTaxes = taxService.calculateTaxes(operations);

        assertTaxes(expectedTaxes, actualTaxes);
    }

    private void assertTaxes(List<BigDecimal> expected, List<BigDecimal> actual) {
        List<BigDecimal> formattedActualTaxes = actual.stream()
                .map(tax -> tax.setScale(2, RoundingMode.HALF_UP))
                .collect(Collectors.toList());

        assertEquals(expected, formattedActualTaxes);
    }
}