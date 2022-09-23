package com.example.inflearnrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        //
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is wrong");
        }

        LocalDateTime endEventDateTIme = eventDto.getEndEventDateTIme();

        if (endEventDateTIme.isBefore(eventDto.getBeginEventDateTIme()) ||
            endEventDateTIme.isBefore(eventDto.getBeginEnrollmentDateTIme()) ||
            endEventDateTIme.isBefore(eventDto.getCloseEnrollmentDateTIme())) {
            errors.rejectValue("endEventDateTIme", "wrongValue", "endEventDateTime is wrong ");
        }
    }
}
