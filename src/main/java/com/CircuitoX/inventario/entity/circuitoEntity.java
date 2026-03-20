package com.CircuitoX.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "mobile_devices")

public class circuitoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;
    @Column(name = "storage")
    private String storage;
    @Column(name = "ram")
    private int ram;
    @Column(name = "color")
    private String color;
    @Column(name = "price")
    private Double price;
    @Column(name = "stock")
    private int stock;

}
