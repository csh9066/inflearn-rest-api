package com.example.inflearnrestapi.common;

import com.example.inflearnrestapi.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsModel extends EntityModel<List<ObjectError>> {
    public ErrorsModel(List<ObjectError> content) {
        super(content);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
