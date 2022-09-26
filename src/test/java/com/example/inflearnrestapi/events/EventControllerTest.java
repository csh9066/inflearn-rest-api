package com.example.inflearnrestapi.events;

import com.example.inflearnrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest extends BaseControllerTest {

    @Test
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
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("offline").value(false))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("생성된 이벤트 링크"),
                                linkWithRel("query-events").description("이벤트 리스트 조회 링크"),
                                linkWithRel("update-event").description("이벤트 변경 링크")
                        ),
                        requestFields(
                                fieldWithPath("name").description("이벤트 이름"),
                                fieldWithPath("description" ).description("이벤트 설명"),
                                fieldWithPath("limitOfEnrollment").description("이벤트 등록 인원 제한"),
                                fieldWithPath("location").description("이벤트 장소"),
                                fieldWithPath("basePrice").description("이벤트 기본 가격"),
                                fieldWithPath("maxPrice").description("이벤트 최대 가격"),
                                fieldWithPath("beginEnrollmentDateTIme").description("이벤트 등록 시작일"),
                                fieldWithPath("closeEnrollmentDateTIme").description("이벤트 등록 마감일"),
                                fieldWithPath("beginEventDateTIme").description("이벤트 시작일"),
                                fieldWithPath("endEventDateTIme").description("이벤트 종료일")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("이벤트 아이디"),
                                fieldWithPath("name").description("이벤트 이름"),
                                fieldWithPath("description" ).description("이벤트 설명"),
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
                        )
                ));
    }

    @Test
    @DisplayName("이벤트 생성 - 입력 값이 받을 수 없는 값이 들어오면 400 상태 코드를 응답한다.")
    void createEvent2() throws Exception {
        // given
        Event source = Event.builder()
                .id(1)
                .free(false)
                .offline(false)
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
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(source))
        );

        // then
        result.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("이벤트 생성 - 입력 값이 잘못된 경우 400 상태 코드를 응답한다.")
    @Test
    void createEvent3() throws Exception {
        // given
        EventDto source = EventDto.builder()
                .name("스프링")
                .description("스프링을 이용해서 REST API를 학습1")
                .beginEnrollmentDateTIme(LocalDateTime.of(2022, 9, 25, 14, 00))
                .closeEnrollmentDateTIme(LocalDateTime.of(2022, 9, 27, 14, 00))
                .beginEventDateTIme(LocalDateTime.of(2022, 9, 29, 16, 00))
                .endEventDateTIme(LocalDateTime.of(2022, 9, 28,18, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("nice meet A")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(source))
        );

        // then
        result.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists());
    }

}