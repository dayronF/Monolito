package com.CircuitoX.inventario.dto;

import lombok.Data;

@Data
public class circuitoResponse {
    
    private Long id;

    private String brand;

    private String model;

    private String storage;

    private int ram;

    private String color;

    private Double price;

    private int stock;

}
