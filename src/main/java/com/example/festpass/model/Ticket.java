package com.example.festpass.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String uuid = UUID.randomUUID().toString();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private boolean used = false;

    public Ticket() {}

    /**
     * Convenience constructor for purchasing — auto-generates UUID.
     */
    public Ticket(Event event) {
        this.event = event;
        this.uuid = UUID.randomUUID().toString();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
}
