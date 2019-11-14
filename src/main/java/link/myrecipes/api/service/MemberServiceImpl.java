package link.myrecipes.api.service;

import link.myrecipes.api.domain.UserEntity;
import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.security.UserSecurity;
import link.myrecipes.api.exception.UsernameNotFoundException;
import link.myrecipes.api.repository.MemberRepository;
import org.springframework.stereotype.Service;

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

        return userEntityOptional.get().toSecurityDTO();
    }

    @Override
    public User readMember(int id) {
        return null;
    }

    @Override
    public User createMember(User user, int userId) {
        return null;
    }

    @Override
    public User updateMember(int id, User user, int userId) {
        return null;
    }
}
