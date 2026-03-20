package com.CircuitoX.inventario.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.CircuitoX.inventario.dto.circuitoMessage;
import com.CircuitoX.inventario.dto.circuitoRequest;
import com.CircuitoX.inventario.dto.circuitoResponse;
import com.CircuitoX.inventario.entity.circuitoEntity;
import com.CircuitoX.inventario.repository.circuitoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class circuitoService {

    private final circuitoRepository circuitoRepository;

    public circuitoMessage createCelphone(circuitoRequest circuitoRequest) {
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

    public circuitoMessage searchCelphone(String model) {

        circuitoMessage circuitoMessage = new circuitoMessage();

        Optional<circuitoEntity> circuitoEntity = circuitoRepository.findByModel(model);

        if (circuitoEntity.isEmpty()) {
            circuitoMessage.setMessage("Celular no encontrado");
            return circuitoMessage;
        }

        circuitoMessage.setMessage(
                "Celular encontrado: " + circuitoEntity.get().getBrand() + " " + circuitoEntity.get().getModel() + " "
                        + circuitoEntity.get().getStorage() + " " + circuitoEntity.get().getRam() + " "
                        + circuitoEntity.get().getColor() + " " + circuitoEntity.get().getPrice() + " "
                        + circuitoEntity.get().getStock());
        return circuitoMessage;
    }

    public circuitoMessage UpdatePhone(circuitoRequest circuitoRequest) {
        circuitoMessage circuitoMessage = new circuitoMessage();

        Optional<circuitoEntity> circuitoEntity = circuitoRepository.findByModel(circuitoRequest.getModel());

        if (circuitoEntity.isEmpty()) {
            circuitoMessage.setMessage("Celular no encontrado");
            return circuitoMessage;
        }

        circuitoEntity.get().setBrand(circuitoRequest.getBrand());
        circuitoEntity.get().setStorage(circuitoRequest.getStorage());
        circuitoEntity.get().setRam(circuitoRequest.getRam());
        circuitoEntity.get().setColor(circuitoRequest.getColor());
        circuitoEntity.get().setPrice(circuitoRequest.getPrice());
        circuitoEntity.get().setStock(circuitoRequest.getStock());

        circuitoRepository.save(circuitoEntity.get());

        circuitoMessage.setMessage("Celular actualizado exitosamente");
        return circuitoMessage;
    }

    public circuitoMessage deletePhone(String model) {
        circuitoMessage circuitoMessage = new circuitoMessage();

        Optional<circuitoEntity> circuitoEntity = circuitoRepository.findByModel(model);

        if (circuitoEntity.isEmpty()) {
            circuitoMessage.setMessage("Celular no encontrado");
            return circuitoMessage;
        }

        circuitoRepository.delete(circuitoEntity.get());

        circuitoMessage.setMessage("Celular eliminado exitosamente");
        return circuitoMessage;
    }

    public circuitoMessage  RestarStock(String model, int stock) {
        circuitoMessage circuitoMessage = new circuitoMessage();

        Optional<circuitoEntity> circuitoEntity = circuitoRepository.findByModel(model);

        if (circuitoEntity.isEmpty()) {
            circuitoMessage.setMessage("Celular no encontrado");
            return circuitoMessage;
        }

        if (circuitoEntity.get().getStock() < stock) {
            circuitoMessage.setMessage("No hay suficiente stock para restar");
            return circuitoMessage;
        }

        circuitoEntity.get().setStock(circuitoEntity.get().getStock() - stock);
        circuitoRepository.save(circuitoEntity.get());

        circuitoMessage.setMessage("Stock restado exitosamente");
        return circuitoMessage;
    }

}