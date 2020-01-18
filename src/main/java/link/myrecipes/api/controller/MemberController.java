package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.common.LinkType;
import link.myrecipes.api.common.RestResource;
import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.dto.security.UserSecurity;
import link.myrecipes.api.service.MemberService;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(tags = {"member"})
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login/{username}")
    @ApiOperation("회원 로그인 조회")
    public ResponseEntity<RestResource<UserSecurity>> login(@PathVariable String username) {

        UserSecurity userSecurity = this.memberService.login(username);

        RestResource<UserSecurity> restResource = new RestResource<>(userSecurity);
        restResource.add(linkTo(methodOn(getClass()).login(username)).withSelfRel());
        restResource.add(new Link("/docs/index.html#resources-login-member").withRel("profile"));

        return ResponseEntity.ok(restResource);
    }

    @GetMapping("/{id}")
    @ApiOperation("회원 조회")
    public ResponseEntity<RestResource<User>> readMember(@PathVariable int id) {

        User user = this.memberService.readMember(id);

        RestResource<User> restResource = new RestResource<>(user,
                String.valueOf(user.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.UPDATE},
                "member");
        restResource.add(new Link("/docs/index.html#resources-read-member").withRel("profile"));

        return ResponseEntity.ok(restResource);
    }

    @PostMapping
    @ApiOperation("회원 저장")
    public ResponseEntity<RestResource<User>> createMember(@RequestBody @Valid UserRequest userRequest) {

        User savedUser = this.memberService.createMember(userRequest);

        RestResource<User> restResource = new RestResource<>(savedUser,
                String.valueOf(savedUser.getId()),
                getClass(),
                new LinkType[] {LinkType.READ, LinkType.UPDATE},
                "member");
        restResource.add(new Link("/docs/index.html#resources-create-member").withRel("profile"));

        return ResponseEntity.created(restResource.selfLink().getTemplate().expand()).body(restResource);
    }

    @PutMapping("/{id}")
    @ApiOperation("회원 수정")
    public ResponseEntity<RestResource<User>> updateMember(@PathVariable int id, @RequestBody @Valid UserRequest userRequest, @RequestParam int userId) {
        User savedUser = this.memberService.updateMember(id, userRequest, userId);

        RestResource<User> restResource = new RestResource<>(savedUser,
                String.valueOf(savedUser.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.READ},
                "member");
        restResource.add(new Link("/docs/index.html#resources-update-member").withRel("profile"));

        return ResponseEntity.ok(restResource);
    }
}
