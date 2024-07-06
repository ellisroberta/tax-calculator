package com.example.taxcalculator.service;

import com.example.taxcalculator.dto.OperationDto;
import com.example.taxcalculator.model.Operation;
import com.example.taxcalculator.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaxService {

    private final OperationRepository repository;

    private BigDecimal weightedAveragePrice = BigDecimal.ZERO;
    private int totalQuantity = 0;
    private BigDecimal accumulatedLosses = BigDecimal.ZERO;

    public TaxService(OperationRepository repository) {
        this.repository = repository;
    }

    public List<BigDecimal> calculateTaxes(List<OperationDto> operations) {
        List<BigDecimal> taxes = new ArrayList<>();
        for (OperationDto dto : operations) {
            if ("buy".equalsIgnoreCase(dto.getType())) {
                processBuyOperation(dto);
                taxes.add(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            } else if ("sell".equalsIgnoreCase(dto.getType())) {
                BigDecimal tax = processSellOperation(dto);
                taxes.add(tax);
            }
        }
        return taxes;
    }

    private void processBuyOperation(OperationDto dto) {
        BigDecimal totalCost = weightedAveragePrice.multiply(BigDecimal.valueOf(totalQuantity))
                .add(dto.getUnitCost().multiply(BigDecimal.valueOf(dto.getQuantity())));
        totalQuantity += dto.getQuantity();
        weightedAveragePrice = totalCost.divide(BigDecimal.valueOf(totalQuantity), RoundingMode.HALF_UP);
    }

    private BigDecimal processSellOperation(OperationDto dto) {
        BigDecimal totalOperationValue = dto.getUnitCost().multiply(BigDecimal.valueOf(dto.getQuantity()));

        if (totalOperationValue.compareTo(BigDecimal.valueOf(20000)) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal gainOrLoss = dto.getUnitCost().subtract(weightedAveragePrice)
                .multiply(BigDecimal.valueOf(dto.getQuantity()));

        BigDecimal tax = calculateTax(gainOrLoss);

        // Atualizar estado global
        updateGlobalState(gainOrLoss, dto.getQuantity());

        return tax;
    }

    private BigDecimal calculateTax(BigDecimal gainOrLoss) {
        if (gainOrLoss.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (accumulatedLosses.compareTo(BigDecimal.ZERO) > 0) {
            if (gainOrLoss.compareTo(accumulatedLosses) <= 0) {
                accumulatedLosses = accumulatedLosses.subtract(gainOrLoss);
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            } else {
                gainOrLoss = gainOrLoss.subtract(accumulatedLosses);
                accumulatedLosses = BigDecimal.ZERO;
            }
        }

        BigDecimal taxRate = BigDecimal.valueOf(0.20);
        return gainOrLoss.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    private void updateGlobalState(BigDecimal gainOrLoss, int quantitySold) {
        if (gainOrLoss.compareTo(BigDecimal.ZERO) < 0) {
            accumulatedLosses = accumulatedLosses.add(gainOrLoss.abs());
        }
        totalQuantity -= quantitySold;
        if (totalQuantity == 0) {
            weightedAveragePrice = BigDecimal.ZERO;
        }
    }

    public void saveOperations(List<OperationDto> operations) {
        List<Operation> entities = new ArrayList<>();
        for (OperationDto dto : operations) {
            Operation operation = new Operation();
            operation.setType(dto.getType());
            operation.setUnitCost(dto.getUnitCost());
            operation.setQuantity(dto.getQuantity());
            entities.add(operation);
        }
        repository.saveAll(entities);
    }
}