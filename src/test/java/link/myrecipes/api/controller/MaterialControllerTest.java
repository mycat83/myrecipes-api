package link.myrecipes.api.controller;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.request.MaterialRequest;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.After;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MaterialControllerTest extends ControllerTest {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UnitRepository unitRepository;

    @After
    public void tearDown() {
        this.materialRepository.deleteAll();
        this.unitRepository.deleteAll();
    }

    @Test
    public void When_재료_조회_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitentity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitentity);

        // When
        final ResultActions actions = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/materials/{id}",
                materialEntity.getId())
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
                .andDo(document("materials-read",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("materials-create").description("재료 저장 API"),
                                linkWithRel("materials-query").description("재료 리스트 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        pathParameters(
                                parameterWithName("id").description("Identity of material")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("재료 아이디"),
                                fieldWithPath("name").description("재료 이름"),
                                fieldWithPath("unitName").description("재료의 단위 이름"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.materials-create.href").description("현재 API"),
                                fieldWithPath("_links.materials-query.href").description("재료 리스트 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_재료_리스트_조회_Then_페이지_리턴() throws Exception {

        // Given
        UnitEntity unitentity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitentity);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/materials")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "name,DESC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.materialList[0].id").exists())
                .andExpect(jsonPath("_embedded.materialList[0].name").value(materialEntity.getName()))
                .andExpect(jsonPath("_embedded.materialList[0].unitName").value(materialEntity.getUnitEntity().getName()))
                .andExpect(jsonPath("_embedded.materialList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.materials-create").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("materials-query",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("materials-create").description("재료 저장 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        requestParameters(
                                parameterWithName("page").description("요청 페이지"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.materialList[0].id").description("재료 아이디"),
                                fieldWithPath("_embedded.materialList[0].name").description("재료 이름"),
                                fieldWithPath("_embedded.materialList[0].unitName").description("재료의 단위 이름"),
                                fieldWithPath("_embedded.materialList[0]._links.self.href").description("재료 조회 API"),
                                fieldWithPath("page.number").description("현재 페이지 번호"),
                                fieldWithPath("page.size").description("페이지 사이즈"),
                                fieldWithPath("page.totalElements").description("전체 항목 수"),
                                fieldWithPath("page.totalPages").description("전체 페이지 수"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.materials-create.href").description("재료 저장 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_재료_저장_Then_정상_리턴() throws Exception {

        // Given
        saveUnit();
        MaterialRequest materialRequest = MaterialRequest.builder()
                .name("재료")
                .unitName("kg")
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/materials")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(materialRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(materialRequest.getName()))
                .andExpect(jsonPath("unitName").value(materialRequest.getUnitName()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.materials-read").exists())
                .andExpect(jsonPath("_links.materials-query").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("materials-create",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("materials-read").description("재료 조회 API"),
                                linkWithRel("materials-query").description("재료 리스트 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("name").description("재료 이름"),
                                fieldWithPath("unitName").description("재료의 단위 이름")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("재료 아이디"),
                                fieldWithPath("name").description("재료 이름"),
                                fieldWithPath("unitName").description("재료의 단위 이름"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.materials-read.href").description("재료 조회 API"),
                                fieldWithPath("_links.materials-query.href").description("재료 리스트 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_잘못된_값으로_재료_저장_Then_400_에러_리턴() throws Exception {

        // Given
        MaterialRequest materialRequest = new MaterialRequest();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/materials")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(materialRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    public void When_존재하지않는_단위로_재료_저장_Then_400_에러_리턴() throws Exception {

        // Given
        MaterialRequest materialRequest = MaterialRequest.builder()
                .name("재료")
                .unitName("g")
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/materials")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(materialRequest)));

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