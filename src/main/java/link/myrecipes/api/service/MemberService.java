package link.myrecipes.api.service;

import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.dto.security.UserSecurity;

public interface MemberService {
    UserSecurity login(String username);

    User readMember(int id);

    User createMember(UserRequest userRequest);

    User updateMember(int id, UserRequest userRequest, int userId);
}
