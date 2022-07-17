package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.MemberRequestDTO;
import bakery.caker.dto.MemberResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.MemberService;
import bakery.caker.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final OAuthUserService oAuthUserService;

    @PostMapping("/postman")
    public Object sessionTest(@RequestBody Map<String,Object> attribute) {
        return oAuthUserService.loadUserPostman(attribute);
    }

    @GetMapping("/account/profile")
    public MemberResponseDTO sessionMemberDetails(@LoginUser SessionUserDTO sessionUser) {
        return memberService.findSessionMember(sessionUser.getMemberId());
    }

    @GetMapping("/{memberId}")
    public MemberResponseDTO memberDetails(@PathVariable Long memberId) {
        return memberService.findMember(memberId);
    }

    @PatchMapping("/account/profile")
    public MemberResponseDTO memberModify(@LoginUser SessionUserDTO sessionUser,
                                          @RequestParam(value="nickname", required = false) String nickname,
                                          @RequestParam(value="image", required = false)MultipartFile file) throws IOException {
        return memberService.modifySessionMember(sessionUser.getMemberId(), nickname, file);
    }

    @DeleteMapping("/account")
    public String memberDelete(@LoginUser SessionUserDTO sessionUser) {
        return memberService.deleteSessionMember(sessionUser.getMemberId());
    }
}
