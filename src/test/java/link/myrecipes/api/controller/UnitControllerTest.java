package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Unit;
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
    private ObjectMapper objectMapper;

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
                .andExpect(jsonPath("_links.create-unit").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("read-unit",
                        links(
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("create-unit").description("Link to create unit"),
                                linkWithRel("profile").description("Link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        pathParameters(
                                parameterWithName("name").description("Name of unit")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        responseFields(
                                fieldWithPath("name").description("Name of unit"),
                                fieldWithPath("exchangeUnitName").description("Name of exchangeable unit"),
                                fieldWithPath("exchangeQuantity").description("Base quantity to convert to exchangeable unit"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.create-unit.href").description("Link to create unit"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        )
                ));
    }

    @Test
    public void When_단위_저장_Then_정상_리턴() throws Exception {

        // Given
        Unit unit = Unit.builder()
                .name("kg")
                .exchangeUnitName("g")
                .exchangeQuantity(1000D)
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/units")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(unit)));

        // Then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("name").value(unit.getName()))
                .andExpect(jsonPath("exchangeUnitName").value(unit.getExchangeUnitName()))
                .andExpect(jsonPath("exchangeQuantity").value(unit.getExchangeQuantity()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.read-unit").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("create-unit",
                        links(
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("read-unit").description("Link to read unit"),
                                linkWithRel("profile").description("Link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of unit"),
                                fieldWithPath("exchangeUnitName").description("Name of exchangeable unit"),
                                fieldWithPath("exchangeQuantity").description("Base quantity to convert to exchangeable unit")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        responseFields(
                                fieldWithPath("name").description("Name of unit"),
                                fieldWithPath("exchangeUnitName").description("Name of exchangeable unit"),
                                fieldWithPath("exchangeQuantity").description("Base quantity to convert to exchangeable unit"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.read-unit.href").description("Link to read unit"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        )
                ));
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