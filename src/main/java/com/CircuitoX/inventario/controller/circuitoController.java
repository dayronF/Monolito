package com.CircuitoX.inventario.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CircuitoX.inventario.dto.circuitoMessage;
import com.CircuitoX.inventario.dto.circuitoRequest;
import com.CircuitoX.inventario.service.circuitoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/celulares")
@RequiredArgsConstructor
public class circuitoController {

    private final circuitoService circuitoService;

    @PostMapping("/crear")
    public ResponseEntity<circuitoMessage> createCelphone(@RequestBody circuitoRequest circuitoRequest) {
        try {
            return ResponseEntity.ok(circuitoService.createCelphone(circuitoRequest));
        } catch (Exception e) {
            circuitoMessage error = new circuitoMessage();
            error.setMessage("Error al crear el celular: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/buscar/{model}")
    public ResponseEntity<circuitoMessage> searchCelphone(@PathVariable String model) {
        try {
            return ResponseEntity.ok(circuitoService.searchCelphone(model));
        } catch (Exception e) {
            circuitoMessage error = new circuitoMessage();
            error.setMessage("Error al buscar el celular: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<circuitoMessage> updateCelphone(@RequestBody circuitoRequest circuitoRequest) {
        try {
            return ResponseEntity.ok(circuitoService.UpdatePhone(circuitoRequest));
        } catch (Exception e) {
            circuitoMessage error = new circuitoMessage();
            error.setMessage("Error al actualizar el celular: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/eliminar/{model}")
    public ResponseEntity<circuitoMessage> deleteCelphone(@PathVariable String model) {
        try {
            return ResponseEntity.ok(circuitoService.deletePhone(model));
        } catch (Exception e) {
            circuitoMessage error = new circuitoMessage();
            error.setMessage("Error al eliminar el celular: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/restar-stock")
    public ResponseEntity<circuitoMessage> restarStock(@RequestParam String model, @RequestParam int stock) {
        try {
            return ResponseEntity.ok(circuitoService.RestarStock(model, stock));
        } catch (Exception e) {
            circuitoMessage error = new circuitoMessage();
            error.setMessage("Error al restar el stock: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/sumar-stock")
    public ResponseEntity<circuitoMessage> sumarStock(@RequestParam String model, @RequestParam int stock) {
        try {
            return ResponseEntity.ok(circuitoService.SumarStock(model, stock));
        } catch (Exception e) {
            circuitoMessage error = new circuitoMessage();
            error.setMessage("Error al sumar el stock: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<circuitoMessage> listCelphones() {
        try {
            return ResponseEntity.ok(circuitoService.ListCelphones());
        } catch (Exception e) {
            circuitoMessage error = new circuitoMessage();
            error.setMessage("Error al listar los celulares: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}