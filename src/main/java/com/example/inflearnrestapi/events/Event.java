package com.example.inflearnrestapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id  @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    private Integer limitOfEnrollment;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    // optional 만약 null이면 온라인 모임
    private String location;

    private Integer basePrice;

    private Integer maxPrice;

    private boolean offline;

    private boolean free;

    private LocalDateTime beginEnrollmentDateTIme;

    private LocalDateTime closeEnrollmentDateTIme;

    private LocalDateTime beginEventDateTIme;

    private LocalDateTime endEventDateTIme;

    public void update() {
        if (basePrice == 0 && maxPrice == 0) {
            this.free = true;
        } else {
            this.free =  false;
        }

        if (location.isBlank()) {
            this.offline = true;
        } else {
            this.offline = false;
        }
    }
}
