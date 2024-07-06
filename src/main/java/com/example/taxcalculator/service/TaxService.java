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

        // Verificar se o valor total da operação é menor ou igual a R$ 20.000,00
        if (totalOperationValue.compareTo(BigDecimal.valueOf(20000)) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // Cópia do estado atual para variáveis locais
        BigDecimal currentWeightedAveragePrice = weightedAveragePrice;
        int currentTotalQuantity = totalQuantity;
        BigDecimal currentAccumulatedLosses = accumulatedLosses;

        // Calcular ganho ou perda da operação
        BigDecimal gainOrLoss = dto.getUnitCost().subtract(currentWeightedAveragePrice)
                .multiply(BigDecimal.valueOf(dto.getQuantity()));

        BigDecimal tax;
        if (gainOrLoss.compareTo(BigDecimal.ZERO) < 0) {
            // Prejuízo
            currentAccumulatedLosses = currentAccumulatedLosses.add(gainOrLoss.abs());
            tax = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            // Lucro
            if (currentAccumulatedLosses.compareTo(BigDecimal.ZERO) > 0) {
                if (gainOrLoss.compareTo(currentAccumulatedLosses) <= 0) {
                    currentAccumulatedLosses = currentAccumulatedLosses.subtract(gainOrLoss);
                    tax = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                } else {
                    gainOrLoss = gainOrLoss.subtract(currentAccumulatedLosses);
                    currentAccumulatedLosses = BigDecimal.ZERO;
                    BigDecimal taxRate = BigDecimal.valueOf(0.20); // Taxa de imposto de 20%
                    tax = gainOrLoss.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
                }
            } else {
                BigDecimal taxRate = BigDecimal.valueOf(0.20); // Taxa de imposto de 20%
                tax = gainOrLoss.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
            }
        }

        // Atualizar variáveis globais apenas se houver mudanças
        if (currentWeightedAveragePrice.compareTo(weightedAveragePrice) != 0) {
            weightedAveragePrice = currentWeightedAveragePrice;
        }
        if (currentTotalQuantity != totalQuantity) {
            totalQuantity = currentTotalQuantity;
        }
        if (currentAccumulatedLosses.compareTo(accumulatedLosses) != 0) {
            accumulatedLosses = currentAccumulatedLosses;
        }

        return tax;
    }


    public void saveOperations(List<OperationDto> operations) {
        for (OperationDto dto : operations) {
            Operation operation = new Operation();
            operation.setType(dto.getType());
            operation.setUnitCost(dto.getUnitCost());
            operation.setQuantity(dto.getQuantity());
            repository.save(operation);
        }
    }
}