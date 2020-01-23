package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.UserEntity;
import link.myrecipes.api.domain.UserRoleEntity;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.repository.MemberRepository;
import org.junit.After;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MemberControllerTest extends ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @After
    public void tearDown() {
        this.memberRepository.deleteAll();
    }

    @Test
    public void When_로그인_정보_조회_Then_정상_리턴() throws Exception {

        // Given
        UserEntity userEntity = saveUser();

        // When
        final ResultActions actions = this.mockMvc.perform(get("/members/login/{username}", userEntity.getUsername())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(userEntity)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").value(userEntity.getUsername()))
                .andExpect(jsonPath("password").value(userEntity.getPassword()))
                .andExpect(jsonPath("accountNonExpired").value(userEntity.getAccountNonExpired()))
                .andExpect(jsonPath("accountNonLocked").value(userEntity.getAccountNonLocked()))
                .andExpect(jsonPath("credentialsNonExpired").value(userEntity.getCredentialsNonExpired()))
                .andExpect(jsonPath("enabled").value(userEntity.getEnabled()))
                .andExpect(jsonPath("userRoleSecurityList[0].role").value("USER"))
                .andDo(document("members-login",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        pathParameters(
                                parameterWithName("username").description("회원 유저명(로그인 계정)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 아이디"),
                                fieldWithPath("username").description("회원 유저명(로그인 계정)"),
                                fieldWithPath("password").description("회원 비밀번호"),
                                fieldWithPath("accountNonExpired").description("계정이 만료되지 않음 여부"),
                                fieldWithPath("accountNonLocked").description("계정이 잠겨 있지 않음 여부"),
                                fieldWithPath("credentialsNonExpired").description("자격 증명이 만료되지 않음 여부"),
                                fieldWithPath("enabled").description("회원의 활성화 여부"),
                                fieldWithPath("userRoleSecurityList[0].role").description("회원 역할"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_회원_조회_Then_정상_리턴() throws Exception {

        // Given
        UserEntity userEntity = saveUser();

        // When
        final ResultActions actions = this.mockMvc.perform(get("/members/{id}", userEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").value(userEntity.getUsername()))
                .andExpect(jsonPath("password").value(userEntity.getPassword()))
                .andExpect(jsonPath("name").value(userEntity.getName()))
                .andExpect(jsonPath("phone").value(userEntity.getPhone()))
                .andExpect(jsonPath("email").value(userEntity.getEmail()))
                .andDo(document("members-read",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("members-create").description("회원 저장 API"),
                                linkWithRel("members-update").description("회원 수정 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        pathParameters(
                                parameterWithName("id").description("회원 아이디")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 아이디"),
                                fieldWithPath("username").description("회원 유저명(로그인 계정)"),
                                fieldWithPath("password").description("회원 비밀번호"),
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("phone").description("회원 전화번호"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.members-create.href").description("회원 저장 API"),
                                fieldWithPath("_links.members-update.href").description("회원 수정 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_회원_저장_Then_정상_리턴() throws Exception {

        // Given
        UserRequest userRequest = UserRequest.builder()
                .username("user01")
                .password("123456")
                .name("유저01")
                .phone("01012345678")
                .email("user01@domain.com")
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(userRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").value(userRequest.getUsername()))
                .andExpect(jsonPath("password").value(userRequest.getPassword()))
                .andExpect(jsonPath("name").value(userRequest.getName()))
                .andExpect(jsonPath("phone").value(userRequest.getPhone()))
                .andExpect(jsonPath("email").value(userRequest.getEmail()))
                .andDo(document("members-create",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("members-read").description("회원 조회 API"),
                                linkWithRel("members-update").description("회원 수정 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("username").description("회원 유저명(로그인 계정)"),
                                fieldWithPath("password").description("회원 비밀번호"),
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("phone").description("회원 전화번호"),
                                fieldWithPath("email").description("회원 이메일")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 아이디"),
                                fieldWithPath("username").description("회원 유저명(로그인 계정)"),
                                fieldWithPath("password").description("회원 비밀번호"),
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("phone").description("회원 전화번호"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.members-read.href").description("회원 조회 API"),
                                fieldWithPath("_links.members-update.href").description("회원 수정 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    @Test
    public void When_회원정보_수정_Then_정상_리턴() throws Exception {

        // Given
        UserEntity updateUserEntity = UserEntity.builder()
                .username("user02")
                .password("234567")
                .name("유저02")
                .phone("01023456789")
                .email("user02@domain.com")
                .build();

        UserEntity userEntity = saveUser();
        userEntity.update(updateUserEntity, 1002);
        UserRequest userRequest = this.objectMapper.convertValue(userEntity, UserRequest.class);

        // When
        final ResultActions actions = this.mockMvc.perform(put("/members/{id}", userEntity.getId())
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(userRequest)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").value(userEntity.getUsername()))
                .andExpect(jsonPath("password").value(userEntity.getPassword()))
                .andExpect(jsonPath("name").value(userEntity.getName()))
                .andExpect(jsonPath("phone").value(userEntity.getPhone()))
                .andExpect(jsonPath("email").value(userEntity.getEmail()))
                .andDo(document("members-update",
                        links(
                                linkWithRel("self").description("현재 API"),
                                linkWithRel("members-create").description("회원 저장 API"),
                                linkWithRel("members-read").description("회원 조회 API"),
                                linkWithRel("profile").description("프로파일 링크")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("username").description("회원 유저명(로그인 계정)"),
                                fieldWithPath("password").description("회원 비밀번호"),
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("phone").description("회원 전화번호"),
                                fieldWithPath("email").description("회원 이메일")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 아이디"),
                                fieldWithPath("username").description("회원 유저명(로그인 계정)"),
                                fieldWithPath("password").description("회원 비밀번호"),
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("phone").description("회원 전화번호"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("_links.self.href").description("현재 API"),
                                fieldWithPath("_links.members-create.href").description("회원 저장 API"),
                                fieldWithPath("_links.members-read.href").description("회원 조회 API"),
                                fieldWithPath("_links.profile.href").description("프로파일 링크")
                        )
                ));
    }

    private UserEntity saveUser() {

        UserEntity userEntity = UserEntity.builder()
                .username("user01")
                .password("123456")
                .name("사용자01")
                .phone("01012345678")
                .email("user01@domain.com")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();
        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .role("USER")
                .build();

        userEntity.addUserRole(userRoleEntity);
        userRoleEntity.setUserEntity(userEntity);
        this.memberRepository.save(userEntity);
        return userEntity;
    }
}