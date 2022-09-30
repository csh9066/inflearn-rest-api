package com.example.inflearnrestapi.events;

import com.example.inflearnrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventControllerTest extends BaseControllerTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    @WithMockUser
    @DisplayName("이벤트 생성 - 정상이면 201 status를 반환한다.")
    void createEvent() throws Exception {
        // given
        EventDto eventDto = EventDto.builder()
                .name("스프링")
                .description("스프링을 이용해서 REST API를 학습1")
                .beginEnrollmentDateTIme(LocalDateTime.of(2022, 9, 25, 14, 00))
                .closeEnrollmentDateTIme(LocalDateTime.of(2022, 9, 27, 14, 00))
                .beginEventDateTIme(LocalDateTime.of(2022, 9, 29, 16, 00))
                .endEventDateTIme(LocalDateTime.of(2022, 9, 29, 18, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("nice meet A")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto))
        );

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("offline").value(false))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(print())
                .andDo(document("events/create-event",
                        eventRequestFields(),
                        eventResponseFields()
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("이벤트 생성 - 입력 값이 잘못된 경우 400 상태 코드를 응답한다.")
    void createEvent2() throws Exception {
        // given
        EventDto source = EventDto.builder()
                .name(null)
                .description("스프링을 이용해서 REST API를 학습1")
                .beginEnrollmentDateTIme(LocalDateTime.of(2022, 9, 25, 14, 00))
                .closeEnrollmentDateTIme(LocalDateTime.of(2022, 9, 27, 14, 00))
                .beginEventDateTIme(LocalDateTime.of(2022, 9, 29, 16, 00))
                .endEventDateTIme(LocalDateTime.of(2022, 9, 28, 18, 00))
                .basePrice(-300)
                .maxPrice(-300)
                .limitOfEnrollment(100)
                .location("nice meet A")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(source))
        );

        // then
        result
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("field-error",
                        relaxedResponseFields(
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("errors[0].sourceName").description("자원 이름"),
                                fieldWithPath("errors[0].fieldName").description("필드 이름"),
                                fieldWithPath("errors[0].description").description("에러에 대한 설명")
                        )
                ));
    }

    @Test
    @DisplayName("리스트 조회 - 30개의 이벤트를 생성10개씩 두번쨰 페이지 조회하기")
    void queryEvents() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        mockMvc.perform(get("/api/events")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,ASC")
                )
                .andDo(print())
                .andDo(document("events/get-events"));
    }

    @Test
    @DisplayName("단건 조회 - 조회한 이벤트 하나를 응답한다.")
    void getEvent() throws Exception {
        Event event = generateEvent(100);

        mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("events/get-event",
                        pathParameters(
                                parameterWithName("id").description("조회할 이벤트 아이디")
                        ),
                        eventResponseFields()
                ));
    }

    @Test
    @DisplayName("이벤트 조회 - 존재하지 않은 아이디 조회하기")
    void getEventWhenEventNotExist() throws Exception {
        mockMvc.perform(get("/api/events/{id}", 21321))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("이벤트 업데이트 - 변경된 이벤트를 응답한다.")
    void updateEvent() throws Exception {
        // given
        Event event = generateEvent(1);

        EventDto updateSource = modelMapper.map(event, EventDto.class);
        updateSource.setName("kimchi");
        updateSource.setBasePrice(50);
        updateSource.setLocation("nice meet b");

        mockMvc.perform(put("/api/events/{id}", event.getId())
                        .content(objectMapper.writeValueAsString(updateSource))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(event.getId()))
                .andExpect(jsonPath("name").value("kimchi"))
                .andExpect(jsonPath("basePrice").value(50))
                .andExpect(jsonPath("location").value("nice meet b"))
                .andDo(document("events/update-event",
                        eventRequestFields(),
                        eventResponseFields()
                ));
    }

    @Test
    @WithMockUser
    @DisplayName("이벤트 업데이트 - 이벤트가 존재하지 않으면 404를 응답한다.")
    void updateEventWhenNotExists() throws Exception {
        mockMvc.perform(put("/events/{id}", 123123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aEventDto()))
                )
                .andExpect(status().isNotFound());
    }

    private static ResponseFieldsSnippet eventResponseFields() {
        return responseFields(
                fieldWithPath("id").description("이벤트 아이디"),
                fieldWithPath("name").description("이벤트 이름"),
                fieldWithPath("description").description("이벤트 설명"),
                fieldWithPath("limitOfEnrollment").description("이벤트 등록 인원 제한"),
                fieldWithPath("location").description("이벤트 장소"),
                fieldWithPath("basePrice").description("이벤트 기본 가격"),
                fieldWithPath("maxPrice").description("이벤트 최대 가격"),
                fieldWithPath("eventStatus").description("이벤트 상태"),
                fieldWithPath("free").description("이벤트 무료 여부"),
                fieldWithPath("offline").description("이벤트 오프라인 여부"),
                fieldWithPath("beginEnrollmentDateTIme").description("이벤트 등록 시작일"),
                fieldWithPath("closeEnrollmentDateTIme").description("이벤트 등록 마감일"),
                fieldWithPath("beginEventDateTIme").description("이벤트 시작일"),
                fieldWithPath("endEventDateTIme").description("이벤트 종료일")
        );
    }

    private static RequestFieldsSnippet eventRequestFields() {
        return requestFields(
                fieldWithPath("name").description("이벤트 이름"),
                fieldWithPath("description").description("이벤트 설명"),
                fieldWithPath("limitOfEnrollment").description("이벤트 등록 인원 제한"),
                fieldWithPath("location").description("이벤트 장소"),
                fieldWithPath("basePrice").description("이벤트 기본 가격"),
                fieldWithPath("maxPrice").description("이벤트 최대 가격"),
                fieldWithPath("beginEnrollmentDateTIme").description("이벤트 등록 시작일"),
                fieldWithPath("closeEnrollmentDateTIme").description("이벤트 등록 마감일"),
                fieldWithPath("beginEventDateTIme").description("이벤트 시작일"),
                fieldWithPath("endEventDateTIme").description("이벤트 종료일")
        );
    }

    private EventDto aEventDto() {
        return EventDto.builder()
                .name("스프링")
                .description("스프링을 이용해서 REST API를 학습1")
                .beginEnrollmentDateTIme(LocalDateTime.of(2022, 9, 25, 14, 00))
                .closeEnrollmentDateTIme(LocalDateTime.of(2022, 9, 27, 14, 00))
                .beginEventDateTIme(LocalDateTime.of(2022, 9, 29, 16, 00))
                .endEventDateTIme(LocalDateTime.of(2022, 9, 29, 18, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("nice meet A")
                .build();
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event" + index)
                .description("스프링을 이용해서 REST API를 학습1")
                .beginEnrollmentDateTIme(LocalDateTime.of(2022, 9, 25, 14, 00))
                .closeEnrollmentDateTIme(LocalDateTime.of(2022, 9, 27, 14, 00))
                .beginEventDateTIme(LocalDateTime.of(2022, 9, 29, 16, 00))
                .endEventDateTIme(LocalDateTime.of(2022, 9, 29, 18, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("nice meet A")
                .build();

        return eventRepository.save(event);
    }
}