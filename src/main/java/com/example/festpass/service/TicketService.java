package com.example.festpass.service;

import com.example.festpass.model.Event;
import com.example.festpass.model.Ticket;
import com.example.festpass.repository.EventRepository;
import com.example.festpass.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Ticket purchaseTicket(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        // Per-event capacity check
        long sold = ticketRepository.countByEventId(eventId);
        if (event.getCapacity() != null && sold >= event.getCapacity()) {
            throw new IllegalStateException("Event is sold out");
        }
        Ticket ticket = new Ticket(event);
        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> findByUuid(String uuid) {
        return ticketRepository.findByUuid(uuid);
    }

    @Transactional
    public Ticket validateTicket(String uuid) {
        Ticket ticket = ticketRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        if (ticket.isUsed()) {
            throw new IllegalStateException("Ticket already used");
        }
        ticket.setUsed(true);
        return ticketRepository.save(ticket);
    }
}
