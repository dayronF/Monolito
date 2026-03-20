package com.CircuitoX.inventario.service;

import org.springframework.stereotype.Service;

import com.CircuitoX.inventario.dto.circuitoMessage;
import com.CircuitoX.inventario.dto.circuitoRequest;
import com.CircuitoX.inventario.entity.circuitoEntity;
import com.CircuitoX.inventario.repository.circuitoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class circuitoService {

    private final circuitoRepository circuitoRepository;

    public circuitoMessage  createCelphone(circuitoRequest circuitoRequest) {
        circuitoMessage circuitomessage = new circuitoMessage();

        circuitoEntity circuitoentity = new circuitoEntity();
        circuitoentity.setBrand(circuitoRequest.getBrand());
        circuitoentity.setModel(circuitoRequest.getModel());
        circuitoentity.setStorage(circuitoRequest.getStorage());
        circuitoentity.setRam(circuitoRequest.getRam());
        circuitoentity.setColor(circuitoRequest.getColor());
        circuitoentity.setPrice(circuitoRequest.getPrice());
        circuitoentity.setStock(circuitoRequest.getStock());
        circuitoRepository.save(circuitoentity);
        circuitomessage.setMessage("Celular creado exitosamente");

        return circuitomessage;
    }
}