package link.myrecipes.api.service;

import link.myrecipes.api.domain.UserEntity;
import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.dto.security.UserSecurity;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.exception.UsernameNotFoundException;
import link.myrecipes.api.repository.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceImplTest {
    private UserSecurity userSecurity;
    private UserEntity userEntity;
    private UserRequest userRequest;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Before
    public void setUp() {
        this.userSecurity = UserSecurity.builder()
                .id(1)
                .username("user12")
                .password("123456")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        this.userEntity = UserEntity.builder()
                .username("user12")
                .password("123456")
                .name("유저12")
                .phone("01012345678")
                .email("user12@domain.com")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        this.userRequest = UserRequest.builder()
                .username("user12")
                .password("123456")
                .name("유저12")
                .phone("01012345678")
                .email("user12@domain.com")
                .build();
    }

    @Test
    public void When_존재하는_유저명으로_회원_로그인_조회_Then_정상_반환() {
        //given
        given(this.memberRepository.findByUsername(userSecurity.getUsername())).willReturn(Optional.ofNullable(this.userEntity));

        //when
        final UserSecurity selectedUserSecurity = this.memberService.login(this.userSecurity.getUsername());

        //then
        assertThat(selectedUserSecurity, instanceOf(UserSecurity.class));
        assertThat(selectedUserSecurity.getUsername(), is(this.userSecurity.getUsername()));
        assertThat(selectedUserSecurity.getPassword(), is(this.userSecurity.getPassword()));
        assertThat(selectedUserSecurity.isAccountNonExpired(), is(this.userSecurity.isAccountNonExpired()));
        assertThat(selectedUserSecurity.isAccountNonLocked(), is(this.userSecurity.isAccountNonLocked()));
        assertThat(selectedUserSecurity.isCredentialsNonExpired(), is(this.userSecurity.isCredentialsNonExpired()));
        assertThat(selectedUserSecurity.isEnabled(), is(this.userSecurity.isEnabled()));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void When_존재하지_않는_유저명으로_회원_로그인_조회_Then_예외_발생() {
        //when
        this.memberService.login(this.userSecurity.getUsername());
    }

    @Test
    public void When_존재하는_회원_조회_Then_정상_반환() {
        //given
        given(this.memberRepository.findById(1)).willReturn(Optional.ofNullable(this.userEntity));

        //when
        final User user = this.memberService.readMember(1);

        //then
        assertThat(user, instanceOf(User.class));
        assertThat(user.getUsername(), is(this.userEntity.getUsername()));
        assertThat(user.getPassword(), is(this.userEntity.getPassword()));
        assertThat(user.getName(), is(this.userEntity.getName()));
        assertThat(user.getPhone(), is(this.userEntity.getPhone()));
        assertThat(user.getEmail(), is(this.userEntity.getEmail()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_회원_조회_Then_예외_발생() {
        //when
        this.memberService.readMember(1);
    }

    @Test
    public void When_회원_저장_Then_정상_반환() {
        //given
        given(this.memberRepository.save(any(UserEntity.class))).willReturn(this.userEntity);

        //when
        final User user = this.memberService.createMember(this.userRequest);

        //then
        assertThat(user, instanceOf(User.class));
        assertThat(user.getUsername(), is(this.userRequest.getUsername()));
        assertThat(user.getPassword(), is(this.userRequest.getPassword()));
        assertThat(user.getName(), is(this.userRequest.getName()));
        assertThat(user.getPhone(), is(this.userRequest.getPhone()));
        assertThat(user.getEmail(), is(this.userRequest.getEmail()));
    }

    @Test
    public void When_존재하는_회원_수정_Then_정상_반환() {
        //given
        given(this.memberRepository.findById(1)).willReturn(Optional.ofNullable(this.userEntity));
        given(this.memberRepository.save(any(UserEntity.class))).willReturn(this.userEntity);

        //when
        final User user = this.memberService.updateMember(1, this.userRequest, 10002);

        //then
        assertThat(user, instanceOf(User.class));
        assertThat(user.getUsername(), is(this.userRequest.getUsername()));
        assertThat(user.getPassword(), is(this.userRequest.getPassword()));
        assertThat(user.getName(), is(this.userRequest.getName()));
        assertThat(user.getPhone(), is(this.userRequest.getPhone()));
        assertThat(user.getEmail(), is(this.userRequest.getEmail()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_회원_수정_Then_정상_반환() {
        //when
        this.memberService.updateMember(1, this.userRequest, 10002);
    }
}