package bakery.caker.controller;

import bakery.caker.config.Authority;
import bakery.caker.config.LoginUser;
import bakery.caker.dto.MemberRequestDTO;
import bakery.caker.dto.MemberResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.JwtTokenProvider;
import bakery.caker.service.MemberService;
import bakery.caker.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private final HttpSession httpSession;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup/kakao/{role}")
    public void MemberSignUp(@PathVariable String role, HttpServletResponse httpServletResponse) throws IOException {
        httpSession.setAttribute("firstLogin", true);
        httpServletResponse.sendRedirect("https://caker.shop/oauth2/authorization/kakao");
    }

    @GetMapping("/signin/kakao")
    public void MemberSignIn(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("https://caker.shop/oauth2/authorization/kakao");
    }

    @GetMapping("/account/profile")
    public MemberProfileResponseDTO sessionMemberDetails(HttpServletRequest httpRequest) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        return memberService.findSessionMember(sessionUser.getMemberId());
    }

    @GetMapping("/{memberId}")
    public MemberResponseDTO memberDetails(@PathVariable Long memberId) {
        return memberService.findMember(memberId);
    }

    @PatchMapping("/account/profile")
    public MemberResponseDTO memberModify(HttpServletRequest httpRequest,
                                          @RequestParam(value="nickname", required = false) String nickname,
                                          @RequestParam(value="image", required = false)MultipartFile file) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        return memberService.modifySessionMember(sessionUser.getMemberId(), nickname, file);
    }

    @GetMapping("/signup/baker")
    public void roleModify(@LoginUser SessionUserDTO sessionUser, HttpServletResponse httpServletResponse) throws IOException {
        if(sessionUser.getAuthority()== Authority.CLIENT.getValue()) {
            memberService.modifyRole(sessionUser.getMemberId());
            httpServletResponse.sendRedirect("https://caker.shop/oauth2/authorization/kakao");
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
