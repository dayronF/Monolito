package com.CircuitoX.inventario.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CircuitoX.inventario.entity.circuitoEntity;

public interface circuitoRepository extends JpaRepository<circuitoEntity, Long> {

    Optional<circuitoEntity> findByModel(String model);

}
