package com.example.inflearnrestapi.events;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void builder() {
        Event event = Event.builder()
                .name("REST API")
                .description("REST API development with Spring")
                .build();

        assertThat(event).isNotNull();
    }

    @DisplayName("basePrice와 maxPrice가 0이면 free는 true이다.")
    @Test
    void update1() {
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .location("good")
                .build();

        event.update();

        assertThat(event.isFree()).isTrue();
    }

    @DisplayName("location이 비어있으면 offline은 true이다.")
    @ValueSource(strings = {"", "   "})
    @ParameterizedTest
    void update2(String location) {
        Event event = Event.builder()
                .basePrice(300)
                .maxPrice(500)
                .location(location)
                .build();

        event.update();

        assertThat(event.isOffline()).isTrue();
    }
}