package link.myrecipes.api.controller;

import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IndexControllerTest extends ControllerTest {

    @Test
    public void When_인덱스_조회_Then_API_리스트_리턴() throws Exception {

        // When
        final ResultActions actions = this.mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("_links.units-query").exists())
                .andExpect(jsonPath("_links.materials-query").exists())
                .andExpect(jsonPath("_links.recipes-query").exists())
                .andExpect(jsonPath("_links.members-query").exists())
                .andDo(document("index",
                        links(
                                linkWithRel("units-query").description("단위 리스트 조회 API"),
                                linkWithRel("materials-query").description("재료 리스트 조회 API"),
                                linkWithRel("recipes-query").description("레시피 리스트 조회 API"),
                                linkWithRel("members-query").description("회원 리스트 조회 API")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type header")
                        ),
                        responseFields(
                                fieldWithPath("_links.units-query.href").description("단위 리스트 조회 API"),
                                fieldWithPath("_links.materials-query.href").description("재료 리스트 조회 API"),
                                fieldWithPath("_links.recipes-query.href").description("레시피 리스트 조회 API"),
                                fieldWithPath("_links.members-query.href").description("회원 리스트 조회 API")
                        )
                ));
    }
}
