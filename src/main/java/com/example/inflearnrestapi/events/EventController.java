package com.example.inflearnrestapi.events;

import com.example.inflearnrestapi.common.ErrorsModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Validated EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        eventRepository.save(event);


        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class)
                .slash(event.getId());

        EventModel eventModel = new EventModel(event);

        eventModel
                .add(linkTo(EventController.class).withRel("query-events"))
                .add(selfLinkBuilder.withRel("update-event"))
                .add(Link.of("/docs/index.html#resources-events-create").withRel("profile"))
                .add(selfLinkBuilder.withSelfRel());

        return ResponseEntity.created(selfLinkBuilder.toUri()).body(eventModel);
    }
}
