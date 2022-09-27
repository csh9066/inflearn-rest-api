package com.example.inflearnrestapi.common;

import com.example.inflearnrestapi.IndexController;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsModel extends EntityModel<ErrorResponse> {
    public ErrorsModel(ErrorResponse content) {
        super(content);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
