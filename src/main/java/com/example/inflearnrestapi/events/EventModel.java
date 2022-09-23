package com.example.inflearnrestapi.events;

import lombok.Getter;
import org.springframework.hateoas.EntityModel;

@Getter
public class EventModel extends EntityModel<Event> {
    protected EventModel(Event content) {
        super(content);
    }
}
