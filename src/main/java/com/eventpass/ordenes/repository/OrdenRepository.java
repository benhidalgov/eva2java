package com.eventpass.ordenes.repository;

import com.eventpass.ordenes.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByUsuarioId(Long usuarioId);

    List<Orden> findByEventoId(Long eventoId);

}
