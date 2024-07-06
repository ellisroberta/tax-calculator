package com.example.taxcalculator.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationDto {
    private String type;
    private BigDecimal unitCost;
    private int quantity;

    public OperationDto() {}

    public OperationDto(String type, BigDecimal unitCost, int quantity) {
        this.type = type;
        this.unitCost = unitCost;
        this.quantity = quantity;
    }

}
