package com.example.support.component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.support.controller.AnnouncementDeleteController;
import com.example.support.controller.AnnouncementGetController;
import com.example.support.domain.AnnouncementResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class DtoModelAssembler implements RepresentationModelAssembler<AnnouncementResponse, EntityModel<AnnouncementResponse>> {

    @Override
    public EntityModel<AnnouncementResponse> toModel(AnnouncementResponse dto) {
        return EntityModel.of(dto,
                              linkTo(methodOn(AnnouncementGetController.class).getAnnouncementById(dto.getAnnounceId())).withSelfRel(),
                              linkTo(methodOn(AnnouncementGetController.class).getAllAnnouncements()).withRel("announcements"),
                              linkTo(methodOn(AnnouncementDeleteController.class).deleteAnnouncementById(dto.getAnnounceId())).withRel("delete")
        );
    }
}
