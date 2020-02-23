package link.myrecipes.api.controller;

import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.request.UnitRequest;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UnitControllerTest extends ControllerTest {

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void When_단위_조회_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();

        // When
        final ResultActions actions = this.mockMvc.perform(get("/units/{name}", unitEntity.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("name").value(unitEntity.getName()))
                .andExpect(jsonPath("exchangeUnitName").value(unitEntity.getExchangeUnitName()))
                .andExpect(jsonPath("exchangeQuantity").value(unitEntity.getExchangeQuantity()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.units-create").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("units-read",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("units-create").description("단위 저장 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        pathParameters(
                                parameterWithName("name").description("단위 이름")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("name").description("단위 이름"),
                                fieldWithPath("exchangeUnitName").description("환산 단위 이름"),
                                fieldWithPath("exchangeQuantity").description("환산 단위 수량"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.units-create.href").description("단위 저장 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_단위_저장_Then_정상_리턴() throws Exception {

        // Given
        UnitRequest unitRequest = UnitRequest.builder()
                .name("kg")
                .exchangeUnitName("g")
                .exchangeQuantity(1000D)
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/units")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(unitRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("name").value(unitRequest.getName()))
                .andExpect(jsonPath("exchangeUnitName").value(unitRequest.getExchangeUnitName()))
                .andExpect(jsonPath("exchangeQuantity").value(unitRequest.getExchangeQuantity()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.units-read").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("units-create",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("units-read").description("단위 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("name").description("단위 이름"),
                                fieldWithPath("exchangeUnitName").description("환산 단위"),
                                fieldWithPath("exchangeQuantity").description("환산 단위의 수량")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("name").description("단위 이름"),
                                fieldWithPath("exchangeUnitName").description("환산 단위 이름"),
                                fieldWithPath("exchangeQuantity").description("환산 단위 수량"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.units-read.href").description("단위 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_잘못된_값으로_단위_저장_Then_400_에러_리턴() throws Exception {

        // Given
        UnitRequest unitRequest = new UnitRequest();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/units")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(unitRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
    }

    private UnitEntity saveUnit() {

        UnitEntity unitEntity = UnitEntity.builder()
                .name("kg")
                .exchangeUnitName("g")
                .exchangeQuantity(1000D)
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();
        this.unitRepository.save(unitEntity);
        return unitEntity;
    }
}