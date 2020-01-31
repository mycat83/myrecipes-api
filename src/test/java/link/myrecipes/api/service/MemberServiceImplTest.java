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
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Mock
    private ModelMapper modelMapper;

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

        // Given
        given(this.memberRepository.findByUsername(userSecurity.getUsername())).willReturn(Optional.ofNullable(this.userEntity));
        given(this.modelMapper.map(any(UserEntity.class), eq(UserSecurity.class))).willReturn(this.userSecurity);

        // When
        final UserSecurity readUserSecurity = this.memberService.login(this.userSecurity.getUsername());

        // Then
        assertThat(readUserSecurity, instanceOf(UserSecurity.class));
        assertThat(readUserSecurity.getUsername(), is(this.userSecurity.getUsername()));
        assertThat(readUserSecurity.getPassword(), is(this.userSecurity.getPassword()));
        assertThat(readUserSecurity.isAccountNonExpired(), is(this.userSecurity.isAccountNonExpired()));
        assertThat(readUserSecurity.isAccountNonLocked(), is(this.userSecurity.isAccountNonLocked()));
        assertThat(readUserSecurity.isCredentialsNonExpired(), is(this.userSecurity.isCredentialsNonExpired()));
        assertThat(readUserSecurity.isEnabled(), is(this.userSecurity.isEnabled()));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void When_존재하지_않는_유저명으로_회원_로그인_조회_Then_예외_발생() {

        // When
        this.memberService.login(this.userSecurity.getUsername());
    }

    @Test
    public void When_존재하는_회원_조회_Then_정상_반환() {

        // Given
        User user = makeUser(this.userEntity);

        given(this.memberRepository.findById(1)).willReturn(Optional.ofNullable(this.userEntity));
        given(this.modelMapper.map(any(UserEntity.class), eq(User.class))).willReturn(user);

        // When
        final User readUser = this.memberService.readMember(1);

        // Then
        assertThat(readUser, instanceOf(User.class));
        assertThat(readUser.getUsername(), is(this.userEntity.getUsername()));
        assertThat(readUser.getPassword(), is(this.userEntity.getPassword()));
        assertThat(readUser.getName(), is(this.userEntity.getName()));
        assertThat(readUser.getPhone(), is(this.userEntity.getPhone()));
        assertThat(readUser.getEmail(), is(this.userEntity.getEmail()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_회원_조회_Then_예외_발생() {

        // When
        this.memberService.readMember(1);
    }

    @Test
    public void When_회원_저장_Then_정상_반환() {

        // Given
        User user = makeUser(this.userEntity);

        given(this.memberRepository.save(any(UserEntity.class))).willReturn(this.userEntity);
        given(this.modelMapper.map(any(UserRequest.class), eq(UserEntity.class))).willReturn(this.userEntity);
        given(this.modelMapper.map(any(UserEntity.class), eq(User.class))).willReturn(user);

        // When
        final User savedUser = this.memberService.createMember(this.userRequest);

        // Then
        assertThat(savedUser, instanceOf(User.class));
        assertThat(savedUser.getUsername(), is(this.userRequest.getUsername()));
        assertThat(savedUser.getPassword(), is(this.userRequest.getPassword()));
        assertThat(savedUser.getName(), is(this.userRequest.getName()));
        assertThat(savedUser.getPhone(), is(this.userRequest.getPhone()));
        assertThat(savedUser.getEmail(), is(this.userRequest.getEmail()));
    }

    @Test
    public void When_존재하는_회원_수정_Then_정상_반환() {

        // Given
        User user = makeUser(this.userEntity);

        given(this.memberRepository.findById(1)).willReturn(Optional.ofNullable(this.userEntity));
        given(this.memberRepository.save(any(UserEntity.class))).willReturn(this.userEntity);
        given(this.modelMapper.map(any(UserRequest.class), eq(UserEntity.class))).willReturn(this.userEntity);
        given(this.modelMapper.map(any(UserEntity.class), eq(User.class))).willReturn(user);

        // When
        final User updatedUser = this.memberService.updateMember(1, this.userRequest, 10002);

        // Then
        assertThat(updatedUser, instanceOf(User.class));
        assertThat(updatedUser.getUsername(), is(this.userRequest.getUsername()));
        assertThat(updatedUser.getPassword(), is(this.userRequest.getPassword()));
        assertThat(updatedUser.getName(), is(this.userRequest.getName()));
        assertThat(updatedUser.getPhone(), is(this.userRequest.getPhone()));
        assertThat(updatedUser.getEmail(), is(this.userRequest.getEmail()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_존재하지_않는_회원_수정_Then_정상_반환() {

        // When
        this.memberService.updateMember(1, this.userRequest, 10002);
    }

    private User makeUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .phone(userEntity.getPhone())
                .email(userEntity.getEmail())
                .build();
    }
}