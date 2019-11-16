package link.myrecipes.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.myrecipes.api.dto.User;
import link.myrecipes.api.dto.request.UserRequest;
import link.myrecipes.api.dto.security.UserSecurity;
import link.myrecipes.api.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"member"})
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login/{username}")
    @ApiOperation("회원 로그인 조회")
    public ResponseEntity<UserSecurity> login(@PathVariable String username) {
        UserSecurity userSecurity = this.memberService.login(username);
        return new ResponseEntity<>(userSecurity, HttpStatus.OK);
    }

    @GetMapping("/members/{id}")
    @ApiOperation("회원 조회")
    public ResponseEntity<User> readMember(@PathVariable int id) {
        User user = this.memberService.readMember(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/members")
    @ApiOperation("회원 저장")
    public ResponseEntity<User> createMember(@RequestBody @Valid UserRequest userRequest) {
        User savedUser = this.memberService.createMember(userRequest);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @PutMapping("/members/{id}")
    @ApiOperation("회원 수정")
    public ResponseEntity<User> updateMember(@PathVariable int id, @RequestBody @Valid UserRequest userRequest, @RequestParam int userId) {
        User savedUser = this.memberService.updateMember(id, userRequest, userId);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}
