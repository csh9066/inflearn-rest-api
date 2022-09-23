package com.example.inflearnrestapi.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class EventDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @Min(0)
    private Integer limitOfEnrollment;

    // optional 만약 null이면 온라인 모임
    private String location;

    @Min(0)
    private Integer basePrice;

    @Min(0)
    private Integer maxPrice;

    @NotNull
    private LocalDateTime beginEnrollmentDateTIme;

    @NotNull
    private LocalDateTime closeEnrollmentDateTIme;

    @NotNull
    private LocalDateTime beginEventDateTIme;

    @NotNull
    private LocalDateTime endEventDateTIme;
}
