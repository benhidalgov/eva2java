package com.eventpass.eventos.repository;

import com.eventpass.eventos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Descuenta el aforo de forma atómica: solo descuenta si aún queda
     * espacio. Devuelve 0 filas afectadas cuando no hay aforo suficiente,
     * lo que evita la condición de carrera de la venta masiva.
     */
    @Modifying
    @Query("""
            update Evento e
               set e.aforoDisponible = e.aforoDisponible - :cantidad
             where e.id = :id
               and e.aforoDisponible >= :cantidad
            """)
    int descontarAforo(@Param("id") Long id, @Param("cantidad") int cantidad);

    /** Devuelve el aforo consumido al anular una orden. */
    @Modifying
    @Query("""
            update Evento e
               set e.aforoDisponible = e.aforoDisponible + :cantidad
             where e.id = :id
            """)
    int devolverAforo(@Param("id") Long id, @Param("cantidad") int cantidad);

}
