package link.myrecipes.api.repository;

import link.myrecipes.api.domain.UserEntity;
import link.myrecipes.api.exception.NotExistDataException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Ignore
public class MemberRepositoryTest {
    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private UserEntity savedUserEntity;

    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void setUp() {
        this.userEntity1 = UserEntity.builder()
                .username("user1")
                .password("123456")
                .name("유저11")
                .phone("01012345678")
                .email("user1@domain.com")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .registerUserId(10001)
                .build();
        this.userEntity2 = UserEntity.builder()
                .username("user2")
                .password("234567")
                .name("유저22")
                .phone("01087654321")
                .email("user2@domain.com")
                .accountNonExpired(false)
                .accountNonLocked(false)
                .credentialsNonExpired(false)
                .enabled(false)
                .modifyUserId(10002)
                .build();

        this.savedUserEntity = this.memberRepository.save(userEntity1);
    }

    @Test
    public void When_회원_저장_Then_동일한_엔티티_반환() {
        //then
        assertThat(this.savedUserEntity.getUsername(), is(this.userEntity1.getUsername()));
        assertThat(this.savedUserEntity.getPassword(), is(this.userEntity1.getPassword()));
        assertThat(this.savedUserEntity.getName(), is(this.userEntity1.getName()));
        assertThat(this.savedUserEntity.getPhone(), is(this.userEntity1.getPhone()));
        assertThat(this.savedUserEntity.getEmail(), is(this.userEntity1.getEmail()));
        assertThat(this.savedUserEntity.getAccountNonExpired(), is(this.userEntity1.getAccountNonExpired()));
        assertThat(this.savedUserEntity.getAccountNonLocked(), is(this.userEntity1.getAccountNonLocked()));
        assertThat(this.savedUserEntity.getCredentialsNonExpired(), is(this.userEntity1.getCredentialsNonExpired()));
        assertThat(this.savedUserEntity.getEnabled(), is(this.userEntity1.getEnabled()));
        assertThat(this.savedUserEntity.getRegisterUserId(), is(this.userEntity1.getRegisterUserId()));
    }

    @Test
    public void When_회원_저장_후_수정_Then_변경돤_엔티티_반환() {
        //given
        this.savedUserEntity.update(this.userEntity2, this.userEntity2.getModifyUserId());

        //when
        UserEntity updatedUserEntity = this.memberRepository.save(this.savedUserEntity);

        //then
        assertThat(updatedUserEntity.getId(), is(this.savedUserEntity.getId()));
        assertThat(updatedUserEntity.getUsername(), is(this.userEntity2.getUsername()));
        assertThat(updatedUserEntity.getPassword(), is(this.userEntity2.getPassword()));
        assertThat(updatedUserEntity.getName(), is(this.userEntity2.getName()));
        assertThat(updatedUserEntity.getPhone(), is(this.userEntity2.getPhone()));
        assertThat(updatedUserEntity.getEmail(), is(this.userEntity2.getEmail()));
        assertThat(updatedUserEntity.getModifyUserId(), is(this.userEntity2.getModifyUserId()));
    }

    @Test(expected = NotExistDataException.class)
    public void When_회원_저장_후_삭제_조회_Then_예외_발생() {
        //given
        this.memberRepository.deleteById(this.savedUserEntity.getId());

        //when
        Optional<UserEntity> userEntityOptional = this.memberRepository.findById(this.savedUserEntity.getId());
        if (userEntityOptional.isEmpty()) {
            throw new NotExistDataException(UserEntity.class, this.savedUserEntity.getId());
        }
        final UserEntity userEntity = userEntityOptional.get();
    }
}