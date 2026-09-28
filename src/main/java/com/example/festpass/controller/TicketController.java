package com.example.festpass.controller;

import com.example.festpass.model.Ticket;
import com.example.festpass.service.TicketService;
import com.example.festpass.util.QRGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Purchase a ticket for a given event.
     * Returns the ticket UUID and a Base64-encoded QR code image.
     */
    @PostMapping(
            path = "/events/{eventId}/tickets",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> purchaseTicket(
            @PathVariable("eventId") Long eventId) {

        Ticket ticket = ticketService.purchaseTicket(eventId);

        String qrBase64 = QRGenerator.generateBase64QRCode(
                ticket.getUuid(), 250, 250
        );

        Map<String, String> resp = new HashMap<>();

        resp.put("uuid", ticket.getUuid());
        resp.put("qrCode", "data:image/png;base64," + qrBase64);

        return resp;
    }

    /**
     * Validate a ticket at entry using its UUID.
     */
    @PostMapping(
            path = "/tickets/validate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> validateTicket(
            @RequestParam("uuid") String uuid) {

        ticketService.validateTicket(uuid);

        Map<String, String> resp = new HashMap<>();

        resp.put("status", "valid");
        resp.put("message", "Ticket validated successfully");

        return resp;
    }
}