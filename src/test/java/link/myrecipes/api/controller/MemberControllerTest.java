package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.UserEntity;
import link.myrecipes.api.domain.UserRoleEntity;
import link.myrecipes.api.repository.MemberRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        final ResultActions actions = this.mockMvc.perform(get("/login/{username}", userEntity.getUsername())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(userEntity)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").value(userEntity.getUsername()))
                .andExpect(jsonPath("password").value(userEntity.getPassword()))
                .andExpect(jsonPath("accountNonExpired").value(userEntity.getAccountNonExpired()))
                .andExpect(jsonPath("accountNonLocked").value(userEntity.getAccountNonLocked()))
                .andExpect(jsonPath("credentialsNonExpired").value(userEntity.getCredentialsNonExpired()))
                .andExpect(jsonPath("enabled").value(userEntity.getEnabled()))
                .andExpect(jsonPath("userRoleSecurityList[0].role").value("USER"));
    }

    @Test
    public void When_회원_조회_Then_정상_리턴() throws Exception {

        // Given
        UserEntity userEntity = saveUser();

        // When
        final ResultActions actions = this.mockMvc.perform(get("/members/{id}", userEntity.getId()));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").value(userEntity.getUsername()))
                .andExpect(jsonPath("password").value(userEntity.getPassword()))
                .andExpect(jsonPath("name").value(userEntity.getName()))
                .andExpect(jsonPath("phone").value(userEntity.getPhone()))
                .andExpect(jsonPath("email").value(userEntity.getEmail()));
    }

    @Test
    public void When_회원_저장_Then_정상_리턴() throws Exception {

        // Given
        UserEntity userEntity = UserEntity.builder()
                .username("user12")
                .password("123456")
                .name("유저12")
                .phone("01012345678")
                .email("user12@domain.com")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(userEntity)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(status().isCreated())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").value(userEntity.getUsername()))
                .andExpect(jsonPath("password").value(userEntity.getPassword()))
                .andExpect(jsonPath("name").value(userEntity.getName()))
                .andExpect(jsonPath("phone").value(userEntity.getPhone()))
                .andExpect(jsonPath("email").value(userEntity.getEmail()));
    }

    @Test
    public void When_회원정보_수정_Then_정상_리턴() throws Exception {

        // Given
        UserEntity updateUserEntity = UserEntity.builder()
                .username("user34")
                .password("234567")
                .name("유저34")
                .phone("01023456789")
                .email("user34@domain.com")
                .build();

        UserEntity userEntity = saveUser();
        userEntity.update(updateUserEntity, 1002);

        // When
        final ResultActions actions = this.mockMvc.perform(put("/members/{id}", userEntity.getId())
                .param("userId", "10001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(userEntity)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(status().isCreated()) ??
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("username").value(userEntity.getUsername()))
                .andExpect(jsonPath("password").value(userEntity.getPassword()))
                .andExpect(jsonPath("name").value(userEntity.getName()))
                .andExpect(jsonPath("phone").value(userEntity.getPhone()))
                .andExpect(jsonPath("email").value(userEntity.getEmail()));
    }

    private UserEntity saveUser() {

        UserEntity userEntity = UserEntity.builder()
                .username("user12")
                .password("123456")
                .name("유저12")
                .phone("01012345678")
                .email("user12@domain.com")
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