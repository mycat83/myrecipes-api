package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.common.ErrorsResource;
import link.myrecipes.api.common.LinkType;
import link.myrecipes.api.common.MemberValidator;
import link.myrecipes.api.common.RestResource;
import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.dto.security.UserSecurity;
import link.myrecipes.api.service.MemberService;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(tags = {"member"})
@RestController
@RequestMapping("/members")
public class MemberController {

    private static final String MEMBERS = "members";
    private final MemberService memberService;
    private final MemberValidator memberValidator;

    public MemberController(MemberService memberService, MemberValidator memberValidator) {
        this.memberService = memberService;
        this.memberValidator = memberValidator;
    }

    @GetMapping("/login/{username}")
    @ApiOperation("회원 로그인 조회")
    public ResponseEntity<ResourceSupport> login(@PathVariable String username) {

        UserSecurity userSecurity = this.memberService.login(username);

        RestResource<UserSecurity> restResource = new RestResource<>(userSecurity);
        restResource.add(linkTo(methodOn(getClass()).login(username)).withSelfRel());
        restResource.addProfileLink("/docs/index.html#resources-members-login");

        return ResponseEntity.ok(restResource);
    }

    @GetMapping("/{id}")
    @ApiOperation("회원 조회")
    public ResponseEntity<ResourceSupport> readMember(@PathVariable int id) {

        User user = this.memberService.readMember(id);

        RestResource<User> restResource = new RestResource<>(user,
                String.valueOf(user.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.UPDATE},
                MEMBERS);
        restResource.addProfileLink("/docs/index.html#resources-members-read");

        return ResponseEntity.ok(restResource);
    }

    @PostMapping
    @ApiOperation("회원 저장")
    public ResponseEntity<ResourceSupport> createMember(@RequestBody @Valid UserRequest userRequest, Errors errors) {

        ResponseEntity<ResourceSupport> errorsResource = checkErrors(userRequest, errors);
        if (errorsResource != null) return errorsResource;

        User savedUser = this.memberService.createMember(userRequest);

        RestResource<User> restResource = new RestResource<>(savedUser,
                String.valueOf(savedUser.getId()),
                getClass(),
                new LinkType[] {LinkType.READ, LinkType.UPDATE},
                MEMBERS);
        restResource.addProfileLink("/docs/index.html#resources-members-create");

        return ResponseEntity.created(restResource.selfLink().getTemplate().expand()).body(restResource);
    }

    @PutMapping("/{id}")
    @ApiOperation("회원 수정")
    public ResponseEntity<ResourceSupport> updateMember(@PathVariable int id,
                                                        @RequestBody @Valid UserRequest userRequest,
                                                        Errors errors,
                                                        @RequestParam int userId) {

        ResponseEntity<ResourceSupport> errorsResource = checkErrors(userRequest, errors);
        if (errorsResource != null) return errorsResource;

        User savedUser = this.memberService.updateMember(id, userRequest, userId);

        RestResource<User> restResource = new RestResource<>(savedUser,
                String.valueOf(savedUser.getId()),
                getClass(),
                new LinkType[] {LinkType.CREATE, LinkType.READ},
                MEMBERS);
        restResource.addProfileLink("/docs/index.html#resources-members-update");

        return ResponseEntity.ok(restResource);
    }

    private ResponseEntity<ResourceSupport> checkErrors(@RequestBody @Valid UserRequest userRequest, Errors errors) {
        if (errors.hasErrors()) {
            ErrorsResource errorsResource = new ErrorsResource(errors);
            return ResponseEntity.badRequest().body(errorsResource);
        }

        this.memberValidator.validate(userRequest, errors);
        if (errors.hasErrors()) {
            ErrorsResource errorsResource = new ErrorsResource(errors);
            return ResponseEntity.badRequest().body(errorsResource);
        }
        return null;
    }
}
