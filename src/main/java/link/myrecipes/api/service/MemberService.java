package link.myrecipes.api.service;

import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.security.UserSecurity;

public interface MemberService {
    UserSecurity login(String username);

    User readMember(int id);

    User createMember(User user, int userId);

    User updateMember(int id, User user, int userId);
}
