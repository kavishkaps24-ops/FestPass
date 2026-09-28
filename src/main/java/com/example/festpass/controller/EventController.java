package com.example.festpass.controller;

import com.example.festpass.model.Event;
import com.example.festpass.repository.EventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    @Autowired
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Create a new fest event with name, capacity and ticket price.
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> listEvents() {
        return eventRepository.findAll();
    }

    /**
     * Get a single event by ID.
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Event getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Event not found with id: " + id));
    }
}