package com.eventpass.tickets.repository;

import com.eventpass.tickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByCodigoQr(String codigoQr);

}
