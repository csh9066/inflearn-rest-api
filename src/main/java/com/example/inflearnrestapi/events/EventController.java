package com.example.inflearnrestapi.events;

import com.example.inflearnrestapi.common.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventModel createEvent(@RequestBody @Validated EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        event.validate();
        eventRepository.save(event);


        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class)
                .slash(event.getId());

        EventModel eventModel = new EventModel(event);

        eventModel
                .add(linkTo(EventController.class).withRel("query-events"))
                .add(selfLinkBuilder.withRel("update-event"))
                .add(Link.of("/docs/index.html#resources-events-create").withRel("profile"))
                .add(selfLinkBuilder.withSelfRel());

        return eventModel;
    }

    @GetMapping
    public PagedModel<EntityModel<Event>> getEventPages(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> events = eventRepository.findAll(pageable);
        return assembler.toModel(events);
    }

    @GetMapping("/{id}")
    public EventModel getEvent(@PathVariable Integer id) {
        Event event = findEvent(id);

        return new EventModel(event);
    }

    @PutMapping("/{id}")
    public EventModel updateEvent(@PathVariable Integer id, @RequestBody @Validated EventDto eventDto) {
        Event event = findEvent(id);

        modelMapper.map(eventDto, event);

        eventRepository.save(event);

        return new EventModel(event);
    }

    private Event findEvent(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id가 " + id + "인 event는 존재하지 않습니다."));
        return event;
    }
}
