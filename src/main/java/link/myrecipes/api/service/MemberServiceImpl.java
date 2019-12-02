package link.myrecipes.api.service;

import link.myrecipes.api.domain.UserEntity;
import link.myrecipes.api.domain.UserRoleEntity;
import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.dto.security.UserSecurity;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.exception.UsernameNotFoundException;
import link.myrecipes.api.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserSecurity login(String username) {
        Optional<UserEntity> userEntityOptional = this.memberRepository.findByUsername(username);

        if (!userEntityOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }

        UserSecurity userSecurity = userEntityOptional.get().toSecurityDTO();
        for (UserRoleEntity userRoleEntity : userEntityOptional.get().getUserRoleEntityList()) {
            userSecurity.addUserAuthoritySecurity(userRoleEntity.toSecurityDTO());
        }
        return userSecurity;
    }

    @Override
    public User readMember(int id) {
        Optional<UserEntity> userEntityOptional = this.memberRepository.findById(id);

        if (!userEntityOptional.isPresent()) {
            throw new NotExistDataException(UserEntity.class, id);
        }

        return userEntityOptional.get().toDTO();
    }

    @Override
    @Transactional
    public User createMember(UserRequest userRequest) {
        UserEntity userEntity = userRequest.toEntity();
        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .authority("USER")
                .build();

        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);
        userEntity.addUserAuthority(userRoleEntity);
        userRoleEntity.setUserEntity(userEntity);

        UserEntity savedUserEntity = this.memberRepository.save(userEntity);
        savedUserEntity.setRegisterUserId(savedUserEntity.getId());
        savedUserEntity.setModifyUserId(savedUserEntity.getId());

        return this.memberRepository.save(savedUserEntity).toDTO();
    }

    @Override
    @Transactional
    public User updateMember(int id, UserRequest userRequest, int userId) {
        Optional<UserEntity> userEntityOptional = this.memberRepository.findById(id);

        if (!userEntityOptional.isPresent()) {
            throw new NotExistDataException(UserEntity.class, id);
        }

        UserEntity selectedUserEntity = userEntityOptional.get();
        selectedUserEntity.update(userRequest.toEntity(), userId);
        return this.memberRepository.save(selectedUserEntity).toDTO();
    }
}
