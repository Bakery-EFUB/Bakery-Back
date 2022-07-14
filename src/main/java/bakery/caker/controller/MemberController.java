package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.MemberResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/account/profile")
    public MemberResponseDTO sessionMemberDetails(@LoginUser SessionUserDTO sessionUser) {
        return memberService.findSessionMember(sessionUser.getMemberId());
    }
}
