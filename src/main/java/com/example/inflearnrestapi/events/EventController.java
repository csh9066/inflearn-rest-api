package com.example.inflearnrestapi.events;

import com.example.inflearnrestapi.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events")
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Event createEvent(@RequestBody @Validated EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        event.validate();
        eventRepository.save(event);
        return event;
    }

    @GetMapping
    public Page<Event> getEventPages(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Integer id) {
        return findEvent(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Integer id, @RequestBody @Validated EventDto eventDto) {
        Event event = findEvent(id);

        modelMapper.map(eventDto, event);

        return eventRepository.save(event);
    }

    private Event findEvent(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id가 " + id + "인 event는 존재하지 않습니다."));
        return event;
    }
}
