package bakery.caker.controller;

import bakery.caker.config.Authority;
import bakery.caker.config.LoginUser;
import bakery.caker.dto.MemberRequestDTO;
import bakery.caker.dto.MemberResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.MemberService;
import bakery.caker.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static bakery.caker.dto.MemberResponseDTO.*;

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
    public MemberProfileResponseDTO sessionMemberDetails(@LoginUser SessionUserDTO sessionUser) {
        return memberService.findSessionMember(sessionUser.getMemberId());
    }

    @GetMapping("/{memberId}")
    public MemberResponseDTO memberDetails(@PathVariable Long memberId) {
        return memberService.findMember(memberId);
    }

    @PatchMapping("/account/profile")
    public MemberResponseDTO memberModify(@LoginUser SessionUserDTO sessionUser,
                                          @RequestParam(value="nickname", required = false) String nickname,
                                          @RequestParam(value="image", required = false)MultipartFile file) {
        return memberService.modifySessionMember(sessionUser.getMemberId(), nickname, file);
    }

    @GetMapping("/signup/baker")
    public void roleModify(@LoginUser SessionUserDTO sessionUser, HttpServletResponse httpServletResponse) throws IOException {
        if(sessionUser.getAuthority()== Authority.CLIENT) {
            memberService.modifyRole(sessionUser.getMemberId());
            httpServletResponse.sendRedirect("http://localhost:8080/oauth2/authorization/kakao");
        }
        else {
            throw new IllegalArgumentException("이미 사장님으로 가입된 유저입니다.");
        }
    }

    @DeleteMapping("/account")
    public String memberDelete(@LoginUser SessionUserDTO sessionUser) {
        return memberService.deleteSessionMember(sessionUser.getMemberId());
    }
}
