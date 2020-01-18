package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MaterialControllerTest extends ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UnitRepository unitRepository;

    @After
    public void tearDown() {
        this.materialRepository.deleteAll();
    }

    @Test
    public void When_재료_조회_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitentity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitentity);

        // When
        final ResultActions actions = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/materials/{id}", materialEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(materialEntity.getName()))
                .andExpect(jsonPath("unitName").value(materialEntity.getUnitEntity().getName()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.materials-create").exists())
                .andExpect(jsonPath("_links.materials-query").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("read-material",
                        links(
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("materials-create").description("Link to create material"),
                                linkWithRel("materials-query").description("Link to read material list"),
                                linkWithRel("profile").description("Link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("Identity of material")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identity of material"),
                                fieldWithPath("name").description("Name of material"),
                                fieldWithPath("unitName").description("Name of unit"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.materials-create.href").description("Link to create material"),
                                fieldWithPath("_links.materials-query.href").description("Link to read material list"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        )
                ));
    }

    @Test
    @Ignore     // TODO: list 요소에 self 링크 넣기
    public void When_재료_리스트_조회_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitentity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitentity);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("_embedded.materialList[0].id").exists())
                .andExpect(jsonPath("_embedded.materialList[0].name").value(materialEntity.getName()))
                .andExpect(jsonPath("_embedded.materialList[0].unitName").value(materialEntity.getUnitEntity().getName()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.materials-create").exists())
                .andExpect(jsonPath("_links.materials-read").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_embedded.materialList[0]._links.self").exists());
    }

    @Test
    public void When_재료_저장_Then_정상_리턴() throws Exception {

        // Given
        saveUnit();
        Material material = Material.builder()
                .name("재료")
                .unitName("kg")
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/materials")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(material)));

        // Then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(material.getName()))
                .andExpect(jsonPath("unitName").value(material.getUnitName()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.materials-read").exists())
                .andExpect(jsonPath("_links.materials-query").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("read-material",
                        links(
                                linkWithRel("self").description("Link to self"),
                                linkWithRel("materials-read").description("Link to read material"),
                                linkWithRel("materials-query").description("Link to read material list"),
                                linkWithRel("profile").description("Link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        requestFields(
                                fieldWithPath("id").description("Identity of material"),
                                fieldWithPath("name").description("Name of material"),
                                fieldWithPath("unitName").description("Name of unit")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identity of material"),
                                fieldWithPath("name").description("Name of material"),
                                fieldWithPath("unitName").description("Name of unit"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.materials-read.href").description("Link to read material"),
                                fieldWithPath("_links.materials-query.href").description("Link to read material list"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        )
                ));
    }

    private UnitEntity saveUnit() {

        UnitEntity unitEntity = UnitEntity.builder()
                .name("kg")
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();
        this.unitRepository.save(unitEntity);
        return unitEntity;
    }

    private MaterialEntity saveMaterial(UnitEntity unitEntity) {

        MaterialEntity materialEntity = MaterialEntity.builder()
                .name("재료")
                .registerUserId(1001)
                .modifyUserId(1001)
                .unitEntity(unitEntity)
                .build();
        this.materialRepository.save(materialEntity);
        return materialEntity;
    }
}