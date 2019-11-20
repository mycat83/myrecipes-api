package link.myrecipes.api.controller;

import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.dto.security.UserSecurity;
import link.myrecipes.api.service.MemberServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.file.Files;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    private User user;

    @Value("classpath:/json/userRequest.json")
    private Resource userRequestResource;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberServiceImpl memberService;

    @Before
    public void setUp() {
        this.user = User.builder()
                .id(1)
                .username("user12")
                .password("123456")
                .name("유저12")
                .phone("01012345678")
                .email("user12@domain.com")
                .build();
    }

    @Test
    public void When_로그인_정보_조회_When_정상_리턴() throws Exception {
        //given
        UserSecurity userSecurity = UserSecurity.builder()
                .id(1)
                .username("user12")
                .password("123456")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
        given(this.memberService.login(eq(userSecurity.getUsername()))).willReturn(userSecurity);

        //when
        final ResultActions actions = this.mockMvc.perform(get("/login/" + userSecurity.getUsername()));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + userSecurity.getId())))
                .andExpect(content().string(containsString("\"username\":\"" + userSecurity.getUsername() + "\"")))
                .andExpect(content().string(containsString("\"password\":\"" + userSecurity.getPassword() + "\"")))
                .andExpect(content().string(containsString("\"accountNonExpired\":" + userSecurity.isAccountNonExpired())))
                .andExpect(content().string(containsString("\"accountNonLocked\":" + userSecurity.isAccountNonLocked())))
                .andExpect(content().string(containsString("\"credentialsNonExpired\":" + userSecurity.isCredentialsNonExpired())))
                .andExpect(content().string(containsString("\"enabled\":" + userSecurity.isEnabled())));
    }

    @Test
    public void When_회원_조회_When_정상_리턴() throws Exception {
        //given
        given(this.memberService.readMember(eq(this.user.getId()))).willReturn(this.user);

        //when
        final ResultActions actions = this.mockMvc.perform(get("/members/" + this.user.getId()));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + this.user.getId())))
                .andExpect(content().string(containsString("\"username\":\"" + this.user.getUsername() + "\"")))
                .andExpect(content().string(containsString("\"password\":\"" + this.user.getPassword() + "\"")))
                .andExpect(content().string(containsString("\"name\":\"" + this.user.getName() + "\"")))
                .andExpect(content().string(containsString("\"phone\":\"" + this.user.getPhone() + "\"")))
                .andExpect(content().string(containsString("\"email\":\"" + this.user.getEmail() + "\"")));
    }

    @Test
    public void When_회원_저장_When_정상_리턴() throws Exception {
        //given
        String userRequestJson = new String(Files.readAllBytes(userRequestResource.getFile().toPath()));
        given(this.memberService.createMember(any(UserRequest.class))).willReturn(this.user);

        //when
        final ResultActions actions = this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(userRequestJson));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + this.user.getId())))
                .andExpect(content().string(containsString("\"username\":\"" + this.user.getUsername() + "\"")))
                .andExpect(content().string(containsString("\"password\":\"" + this.user.getPassword() + "\"")))
                .andExpect(content().string(containsString("\"name\":\"" + this.user.getName() + "\"")))
                .andExpect(content().string(containsString("\"phone\":\"" + this.user.getPhone() + "\"")))
                .andExpect(content().string(containsString("\"email\":\"" + this.user.getEmail() + "\"")));
    }

    @Test
    public void When_회원정보_수정_When_정상_리턴() throws Exception {
        //given
        String userRequestJson = new String(Files.readAllBytes(userRequestResource.getFile().toPath()));
        given(this.memberService.updateMember(eq(this.user.getId()), any(UserRequest.class), any(Integer.class))).willReturn(this.user);

        //when
        final ResultActions actions = this.mockMvc.perform(put("/members/" + this.user.getId() + "?userId=10001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(userRequestJson));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + this.user.getId())))
                .andExpect(content().string(containsString("\"username\":\"" + this.user.getUsername() + "\"")))
                .andExpect(content().string(containsString("\"password\":\"" + this.user.getPassword() + "\"")))
                .andExpect(content().string(containsString("\"name\":\"" + this.user.getName() + "\"")))
                .andExpect(content().string(containsString("\"phone\":\"" + this.user.getPhone() + "\"")))
                .andExpect(content().string(containsString("\"email\":\"" + this.user.getEmail() + "\"")));
    }
}