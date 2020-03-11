package link.myrecipes.api.service;

import link.myrecipes.api.domain.UserEntity;
import link.myrecipes.api.domain.UserRoleEntity;
import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.dto.security.UserSecurity;
import link.myrecipes.api.exception.DuplicateDataException;
import link.myrecipes.api.exception.NotExistDataException;
import link.myrecipes.api.exception.UsernameNotFoundException;
import link.myrecipes.api.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public MemberServiceImpl(MemberRepository memberRepository, ModelMapper modelMapper) {

        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserSecurity login(String username) {

        Optional<UserEntity> userEntityOptional = this.memberRepository.findByUsername(username);

        if (userEntityOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        UserSecurity userSecurity = this.modelMapper.map(userEntityOptional.get(), UserSecurity.class);
        for (UserRoleEntity userRoleEntity : userEntityOptional.get().getUserRoleEntityList()) {
            userSecurity.addUserRoleSecurity(userRoleEntity.toSecurityDTO());
        }
        return userSecurity;
    }

    @Override
    public User readMember(int id) {

        Optional<UserEntity> userEntityOptional = this.memberRepository.findById(id);

        if (userEntityOptional.isEmpty()) {
            throw new NotExistDataException(UserEntity.class, id);
        }

        return this.modelMapper.map(userEntityOptional.get(), User.class);
    }

    @Override
    @Transactional
    public User createMember(UserRequest userRequest) {

        if (this.memberRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new DuplicateDataException(userRequest.getClass(), userRequest.getUsername());
        }

        UserEntity userEntity = this.modelMapper.map(userRequest, UserEntity.class);
        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .role("USER")
                .build();

        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);
        userEntity.addUserRole(userRoleEntity);
        userRoleEntity.setUserEntity(userEntity);

        UserEntity savedUserEntity = this.memberRepository.save(userEntity);
        savedUserEntity.setRegisterUserId(savedUserEntity.getId());
        savedUserEntity.setModifyUserId(savedUserEntity.getId());

        return this.modelMapper.map(this.memberRepository.save(savedUserEntity), User.class);
    }

    @Override
    @Transactional
    public User updateMember(int id, UserRequest userRequest, int userId) {

        Optional<UserEntity> userEntityOptional = this.memberRepository.findById(id);

        if (userEntityOptional.isEmpty()) {
            throw new NotExistDataException(UserEntity.class, id);
        }

        UserEntity readUserEntity = userEntityOptional.get();
        UserEntity userEntity = this.modelMapper.map(userRequest, UserEntity.class);
        readUserEntity.update(userEntity, userId);
        return this.modelMapper.map(this.memberRepository.save(readUserEntity), User.class);
    }
}
