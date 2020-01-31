package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.*;
import link.myrecipes.api.dto.request.RecipeMaterialRequest;
import link.myrecipes.api.dto.request.RecipeRequest;
import link.myrecipes.api.dto.request.RecipeStepRequest;
import link.myrecipes.api.dto.request.RecipeTagRequest;
import link.myrecipes.api.repository.*;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest extends ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMaterialRepository recipeMaterialRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Autowired
    private RecipeTagRepository recipeTagRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private PopularRecipeRepository popularRecipeRepository;

    @After
    public void tearDown() {
        this.recipeMaterialRepository.deleteAll();
        this.recipeStepRepository.deleteAll();
        this.recipeTagRepository.deleteAll();
        this.recipeRepository.deleteAll();
        this.materialRepository.deleteAll();
        this.unitRepository.deleteAll();
    }

    @Test
    public void When_레시피_조회_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/recipes/{id}", recipeEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value(recipeEntity.getTitle()))
                .andExpect(jsonPath("image").value(recipeEntity.getImage()))
                .andExpect(jsonPath("estimatedTime").value(recipeEntity.getEstimatedTime()))
                .andExpect(jsonPath("difficulty").value(recipeEntity.getDifficulty()))
                .andExpect(jsonPath("people").value(recipeEntity.getPeople()))
                .andExpect(jsonPath("registerUserId").value(recipeEntity.getRegisterUserId()))
                .andExpect(jsonPath("registerDate").exists())
                .andExpect(jsonPath("recipeMaterialList[0].materialId")
                        .value(recipeEntity.getRecipeMaterialEntityList().get(0).getMaterialEntity().getId()))
                .andExpect(jsonPath("recipeMaterialList[0].materialName")
                        .value(recipeEntity.getRecipeMaterialEntityList().get(0).getMaterialEntity().getName()))
                .andExpect(jsonPath("recipeMaterialList[0].materialUnitName")
                        .value(recipeEntity.getRecipeMaterialEntityList().get(0).getMaterialEntity().getUnitEntity().getName()))
                .andExpect(jsonPath("recipeMaterialList[0].quantity")
                        .value(recipeEntity.getRecipeMaterialEntityList().get(0).getQuantity()))
                .andExpect(jsonPath("recipeStepList[0].step")
                        .value(recipeEntity.getRecipeStepEntityList().get(0).getStep()))
                .andExpect(jsonPath("recipeStepList[0].content")
                        .value(recipeEntity.getRecipeStepEntityList().get(0).getContent()))
                .andExpect(jsonPath("recipeStepList[0].image")
                        .value(recipeEntity.getRecipeStepEntityList().get(0).getImage()))
                .andExpect(jsonPath("recipeTagList[0].tag")
                        .value(recipeEntity.getRecipeTagEntityList().get(0).getTag()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.recipes-create").exists())
                .andExpect(jsonPath("_links.recipes-update").exists())
                .andExpect(jsonPath("_links.recipes-delete").exists())
                .andExpect(jsonPath("_links.recipes-query").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("recipes-read",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("recipes-create").description("레시피 저장 API"),
                                linkWithRel("recipes-update").description("레시피 수정 API"),
                                linkWithRel("recipes-delete").description("레시피 삭제 API"),
                                linkWithRel("recipes-query").description("레시피 리스트 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        pathParameters(
                                parameterWithName("id").description("레시피 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("레시피 아이디"),
                                fieldWithPath("title").description("레시피 제목"),
                                fieldWithPath("image").description("레시피 대표 이미지"),
                                fieldWithPath("estimatedTime").description("레시피 예상 소요시간"),
                                fieldWithPath("difficulty").description("레시피 난이도"),
                                fieldWithPath("people").description("레시피 인분"),
                                fieldWithPath("registerUserId").description("레시피 등록자 아이디"),
                                fieldWithPath("registerDate").description("레시피 등록일"),
                                fieldWithPath("recipeMaterialList[0].materialId").description("레시피 재료 아이디"),
                                fieldWithPath("recipeMaterialList[0].materialName").description("레시피 재료 이름"),
                                fieldWithPath("recipeMaterialList[0].materialUnitName").description("레시피 재료 단위"),
                                fieldWithPath("recipeMaterialList[0].quantity").description("레시피 재료 수량"),
                                fieldWithPath("recipeStepList[0].step").description("레시피 단계"),
                                fieldWithPath("recipeStepList[0].content").description("레시피 단계 내용"),
                                fieldWithPath("recipeStepList[0].image").description("레시피 단계 이미지"),
                                fieldWithPath("recipeTagList[0].tag").description("레시피 태그"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.recipes-create.href").description("레시피 저장 API"),
                                fieldWithPath("_links.recipes-update.href").description("레시피 수정 API"),
                                fieldWithPath("_links.recipes-delete.href").description("레시피 삭제 API"),
                                fieldWithPath("_links.recipes-query.href").description("레시피 리스트 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_레시피_리스트_조회_Then_페이지_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        IntStream.range(1, 30).forEach(i -> saveRecipe(materialEntity, i));
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 30);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/recipes")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "title,DESC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.recipeList[0].id").exists())
                .andExpect(jsonPath("_embedded.recipeList[0].title").value(recipeEntity.getTitle()))
                .andExpect(jsonPath("_embedded.recipeList[0].image").value(recipeEntity.getImage()))
                .andExpect(jsonPath("_embedded.recipeList[0].estimatedTime").value(recipeEntity.getEstimatedTime()))
                .andExpect(jsonPath("_embedded.recipeList[0].difficulty").value(recipeEntity.getDifficulty()))
                .andExpect(jsonPath("_embedded.recipeList[0].recipeTagList[0].tag")
                        .value(recipeEntity.getRecipeTagEntityList().get(0).getTag()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.recipes-create").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.first").exists())
                .andExpect(jsonPath("_links.next").exists())
                .andExpect(jsonPath("_links.last").exists())
                .andDo(document("recipes-query",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("recipes-create").description("레시피 저장 API"),
                                linkWithRel("profile").description("프로파일 링크"),
                                linkWithRel("first").description("첫 페이지 링크"),
                                linkWithRel("next").description("다음 페이지 링크"),
                                linkWithRel("last").description("마지막 페이지 링크")
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
                                fieldWithPath("_embedded.recipeList[0].id").description("레시피 아이디"),
                                fieldWithPath("_embedded.recipeList[0].title").description("레시피 제목"),
                                fieldWithPath("_embedded.recipeList[0].image").description("레시피 대표 이미지"),
                                fieldWithPath("_embedded.recipeList[0].estimatedTime").description("레시피 예상 소요시간"),
                                fieldWithPath("_embedded.recipeList[0].difficulty").description("레시피 난이도"),
                                fieldWithPath("_embedded.recipeList[0].recipeTagList[0].tag").description("레시피 태그"),
                                fieldWithPath("_embedded.recipeList[0]._links.self.href").description("레시피 조회 API"),
                                fieldWithPath("page.number").description("현재 페이지 번호"),
                                fieldWithPath("page.size").description("페이지 사이즈"),
                                fieldWithPath("page.totalElements").description("전체 항목 수"),
                                fieldWithPath("page.totalPages").description("전체 페이지 수"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.recipes-create.href").description("레시피 저장 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크"),
                                fieldWithPath("_links.first.href").description("첫 페이지 링크"),
                                fieldWithPath("_links.next.href").description("다음 페이지 링크"),
                                fieldWithPath("_links.last.href").description("마지막 페이지 링크")
                        )
                ));
    }

    @Test
    public void When_레시피_저장_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);

        RecipeRequest recipeRequest = RecipeRequest.builder()
                .title("레시피")
                .image("recipe.jpg")
                .estimatedTime(30)
                .difficulty(1)
                .people(1)
                .build();

        RecipeMaterialRequest recipeMaterialRequest = RecipeMaterialRequest.builder()
                .materialId(materialEntity.getId())
                .quantity(10D)
                .build();

        RecipeStepRequest recipeStepRequest = RecipeStepRequest.builder()
                .step(1)
                .content("1단계")
                .image("step1.jpg")
                .build();

        RecipeTagRequest recipeTagRequest = RecipeTagRequest.builder()
                .tag("태그")
                .build();

        recipeRequest.addRecipeMaterial(recipeMaterialRequest);
        recipeRequest.addRecipeStep(recipeStepRequest);
        recipeRequest.addRecipeTag(recipeTagRequest);

        // When
        final ResultActions actions = this.mockMvc.perform(post("/recipes")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(recipeRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value(recipeRequest.getTitle()))
                .andExpect(jsonPath("image").value(recipeRequest.getImage()))
                .andExpect(jsonPath("estimatedTime").value(recipeRequest.getEstimatedTime()))
                .andExpect(jsonPath("difficulty").value(recipeRequest.getDifficulty()))
                .andExpect(jsonPath("recipeTagList[0].tag").value(recipeTagRequest.getTag()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.recipes-read").exists())
                .andExpect(jsonPath("_links.recipes-update").exists())
                .andExpect(jsonPath("_links.recipes-delete").exists())
                .andExpect(jsonPath("_links.recipes-query").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("recipes-create",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("recipes-read").description("레시피 조회 API"),
                                linkWithRel("recipes-update").description("레시피 수정 API"),
                                linkWithRel("recipes-delete").description("레시피 삭제 API"),
                                linkWithRel("recipes-query").description("레시피 리스트 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("title").description("레시피 제목"),
                                fieldWithPath("image").description("레시피 대표 이미지"),
                                fieldWithPath("estimatedTime").description("레시피 예상 소요시간"),
                                fieldWithPath("difficulty").description("레시피 난이도"),
                                fieldWithPath("people").description("레시피 인분"),
                                fieldWithPath("recipeMaterialRequestList[0].materialId").description("레시피 재료 아이디"),
                                fieldWithPath("recipeMaterialRequestList[0].quantity").description("레시피 재료 수량"),
                                fieldWithPath("recipeStepRequestList[0].step").description("레시피 단계"),
                                fieldWithPath("recipeStepRequestList[0].content").description("레시피 단계 내용"),
                                fieldWithPath("recipeStepRequestList[0].image").description("레시피 단계 이미지"),
                                fieldWithPath("recipeTagRequestList[0].tag").description("레시피 태그")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("레시피 아이디"),
                                fieldWithPath("title").description("레시피 제목"),
                                fieldWithPath("image").description("레시피 대표 이미지"),
                                fieldWithPath("estimatedTime").description("레시피 예상 소요시간"),
                                fieldWithPath("difficulty").description("레시피 난이도"),
                                fieldWithPath("recipeTagList[0].tag").description("레시피 태그"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.recipes-read.href").description("레시피 조회 API"),
                                fieldWithPath("_links.recipes-update.href").description("레시피 수정 API"),
                                fieldWithPath("_links.recipes-delete.href").description("레시피 삭제 API"),
                                fieldWithPath("_links.recipes-query.href").description("레시피 리스트 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_잘못된_값으로_레시피_저장_Then_400_에러_리턴() throws Exception {

        // Given
        RecipeRequest recipeRequest = new RecipeRequest();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/recipes")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(recipeRequest)));

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
    public void When_존재하지않는_재료로_레시피_저장_Then_400_에러_리턴() throws Exception {

        // Given
        RecipeRequest recipeRequest = RecipeRequest.builder()
                .title("레시피")
                .image("recipe.jpg")
                .estimatedTime(30)
                .difficulty(1)
                .people(1)
                .build();

        RecipeMaterialRequest recipeMaterialRequest = RecipeMaterialRequest.builder()
                .materialId(100)
                .quantity(10D)
                .build();

        RecipeStepRequest recipeStepRequest = RecipeStepRequest.builder()
                .step(1)
                .content("1단계")
                .image("step1.jpg")
                .build();

        RecipeTagRequest recipeTagRequest = RecipeTagRequest.builder()
                .tag("태그")
                .build();

        recipeRequest.addRecipeMaterial(recipeMaterialRequest);
        recipeRequest.addRecipeStep(recipeStepRequest);
        recipeRequest.addRecipeTag(recipeTagRequest);

        // When
        final ResultActions actions = this.mockMvc.perform(post("/recipes")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(recipeRequest)));

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
    public void When_레시피_수정_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        RecipeRequest recipeRequest = RecipeRequest.builder()
                .title("레시피02")
                .image("recipe02.jpg")
                .estimatedTime(60)
                .difficulty(2)
                .people(2)
                .build();

        RecipeMaterialRequest recipeMaterialRequest = RecipeMaterialRequest.builder()
                .materialId(materialEntity.getId())
                .quantity(20D)
                .build();

        RecipeStepRequest recipeStepRequest = RecipeStepRequest.builder()
                .step(2)
                .content("2단계")
                .image("step2.jpg")
                .build();

        RecipeTagRequest recipeTagRequest = RecipeTagRequest.builder()
                .tag("태그2")
                .build();

        recipeRequest.addRecipeMaterial(recipeMaterialRequest);
        recipeRequest.addRecipeStep(recipeStepRequest);
        recipeRequest.addRecipeTag(recipeTagRequest);

        // When
        final ResultActions actions = this.mockMvc.perform(put("/recipes/{id}", recipeEntity.getId())
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(recipeRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value(recipeRequest.getTitle()))
                .andExpect(jsonPath("image").value(recipeRequest.getImage()))
                .andExpect(jsonPath("estimatedTime").value(recipeRequest.getEstimatedTime()))
                .andExpect(jsonPath("difficulty").value(recipeRequest.getDifficulty()))
                .andExpect(jsonPath("recipeTagList[0].tag").value(recipeTagRequest.getTag()))
                .andDo(document("recipes-update",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("recipes-create").description("레시피 저장 API"),
                                linkWithRel("recipes-read").description("레시피 조회 API"),
                                linkWithRel("recipes-delete").description("레시피 삭제 API"),
                                linkWithRel("recipes-query").description("레시피 리스트 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("title").description("레시피 제목"),
                                fieldWithPath("image").description("레시피 대표 이미지"),
                                fieldWithPath("estimatedTime").description("레시피 예상 소요시간"),
                                fieldWithPath("difficulty").description("레시피 난이도"),
                                fieldWithPath("people").description("레시피 인분"),
                                fieldWithPath("recipeMaterialRequestList[0].materialId").description("레시피 재료 아이디"),
                                fieldWithPath("recipeMaterialRequestList[0].quantity").description("레시피 재료 수량"),
                                fieldWithPath("recipeStepRequestList[0].step").description("레시피 단계"),
                                fieldWithPath("recipeStepRequestList[0].content").description("레시피 단계 내용"),
                                fieldWithPath("recipeStepRequestList[0].image").description("레시피 단계 이미지"),
                                fieldWithPath("recipeTagRequestList[0].tag").description("레시피 태그")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("레시피 아이디"),
                                fieldWithPath("title").description("레시피 제목"),
                                fieldWithPath("image").description("레시피 대표 이미지"),
                                fieldWithPath("estimatedTime").description("레시피 예상 소요시간"),
                                fieldWithPath("difficulty").description("레시피 난이도"),
                                fieldWithPath("recipeTagList[0].tag").description("레시피 태그"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.recipes-create.href").description("레시피 저장 API"),
                                fieldWithPath("_links.recipes-read.href").description("레시피 조회 API"),
                                fieldWithPath("_links.recipes-delete.href").description("레시피 삭제 API"),
                                fieldWithPath("_links.recipes-query.href").description("레시피 리스트 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_잘못된_값으로_레시피_수정_Then_400_에러_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        RecipeRequest recipeRequest = new RecipeRequest();

        // When
        final ResultActions actions = this.mockMvc.perform(put("/recipes/{id}", recipeEntity.getId())
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(recipeRequest)));

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
    public void When_존재하지않는_재료로_레시피_수정_Then_400_에러_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        RecipeRequest recipeRequest = RecipeRequest.builder()
                .title("레시피02")
                .image("recipe02.jpg")
                .estimatedTime(60)
                .difficulty(2)
                .people(2)
                .build();

        RecipeMaterialRequest recipeMaterialRequest = RecipeMaterialRequest.builder()
                .materialId(100)
                .quantity(20D)
                .build();

        RecipeStepRequest recipeStepRequest = RecipeStepRequest.builder()
                .step(2)
                .content("2단계")
                .image("step2.jpg")
                .build();

        RecipeTagRequest recipeTagRequest = RecipeTagRequest.builder()
                .tag("태그2")
                .build();

        recipeRequest.addRecipeMaterial(recipeMaterialRequest);
        recipeRequest.addRecipeStep(recipeStepRequest);
        recipeRequest.addRecipeTag(recipeTagRequest);

        // When
        final ResultActions actions = this.mockMvc.perform(put("/recipes/{id}", recipeEntity.getId())
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(recipeRequest)));

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
    public void When_레시피_삭제_Then_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        // When
        final ResultActions actions = this.mockMvc.perform(delete("/recipes/{id}", recipeEntity.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("recipes-delete",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("recipes-query").description("레시피 리스트 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        pathParameters(
                                parameterWithName("id").description("레시피 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.recipes-query.href").description("레시피 리스트 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_레시피_건_수_조회_Then_정상_리턴() throws Exception {

        // Given
        int count = 10;
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        IntStream.range(0, count).forEach(i -> saveRecipe(materialEntity, i));

        // When
        final ResultActions actions = this.mockMvc.perform(get("/recipes/count")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("count").value(count))
                .andDo(document("recipes-count",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("recipes-query").description("레시피 리스트 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("count").description("레시피 건 수"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.recipes-query.href").description("레시피 리스트 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_레시피_조회_Then_조회수_증가() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitEntity);
        RecipeEntity recipeEntity = saveRecipe(materialEntity, 1);

        // When
        final ResultActions actions = this.mockMvc.perform(put("/recipes/{id}/readCount", recipeEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("recipes-readCount",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("recipes-query").description("레시피 리스트 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        pathParameters(
                                parameterWithName("id").description("레시피 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        )
                ));

        Optional<RecipeEntity> recipeEntityOptional = this.recipeRepository.findById(recipeEntity.getId());
        if (recipeEntityOptional.isEmpty()) {
            fail("Finding recipe failed.");
        }
        assertThat(recipeEntityOptional.get().getPeople(), is(1));
    }

    @Test
    public void When_인기_레시피_조회_Then_정상_조회() throws Exception {

        // Given
        PopularRecipeDocument popularRecipeDocument = PopularRecipeDocument.builder()
                .id(1)
                .title("레시피")
                .image("recipe.jpg")
                .estimatedTime(30)
                .difficulty(1)
                .people(1)
                .readCount(1)
                .recipeTagList(Collections.singletonList("태그"))
                .build();
        this.popularRecipeRepository.save(popularRecipeDocument);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/recipes/popular")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("_embedded.recipeList[0].id").value(popularRecipeDocument.getId()))
                .andExpect(jsonPath("_embedded.recipeList[0].title").value(popularRecipeDocument.getTitle()))
                .andExpect(jsonPath("_embedded.recipeList[0].image").value(popularRecipeDocument.getImage()))
                .andExpect(jsonPath("_embedded.recipeList[0].estimatedTime").value(popularRecipeDocument.getEstimatedTime()))
                .andExpect(jsonPath("_embedded.recipeList[0].difficulty").value(popularRecipeDocument.getDifficulty()))
                .andExpect(jsonPath("_embedded.recipeList[0].recipeTagList[0].tag").value(popularRecipeDocument.getRecipeTagList().get(0)))
                .andDo(document("recipes-popular",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("recipes-create").description("레시피 저장 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.recipeList[0].id").description("레시피 아이디"),
                                fieldWithPath("_embedded.recipeList[0].title").description("레시피 제목"),
                                fieldWithPath("_embedded.recipeList[0].image").description("레시피 대표 이미지"),
                                fieldWithPath("_embedded.recipeList[0].estimatedTime").description("레시피 예상 소요시간"),
                                fieldWithPath("_embedded.recipeList[0].difficulty").description("레시피 난이도"),
                                fieldWithPath("_embedded.recipeList[0].recipeTagList[0].tag").description("레시피 태그"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.recipes-create.href").description("레시피 저장 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
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

    private RecipeEntity saveRecipe(MaterialEntity materialEntity, int index) {

        RecipeEntity recipeEntity = RecipeEntity.builder()
                .title("레시피" + String.format("%02d", index))
                .image("recipe"  + String.format("%02d", index) + ".jpg")
                .estimatedTime(30)
                .difficulty(1)
                .people(1)
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();

        RecipeMaterialEntity recipeMaterialEntity = RecipeMaterialEntity.builder()
                .quantity(10D)
                .recipeEntity(recipeEntity)
                .materialEntity(materialEntity)
                .build();

        RecipeStepEntity recipeStepEntity = RecipeStepEntity.builder()
                .step(1)
                .content("1단계")
                .image("step1.jpg")
                .recipeEntity(recipeEntity)
                .build();

        RecipeTagEntity recipeTagEntity = RecipeTagEntity.builder()
                .tag("태그")
                .recipeEntity(recipeEntity)
                .build();

        recipeEntity.addRecipeMaterial(recipeMaterialEntity);
        recipeEntity.addRecipeStep(recipeStepEntity);
        recipeEntity.addRecipeTag(recipeTagEntity);
        this.recipeRepository.save(recipeEntity);
        return recipeEntity;
    }
}