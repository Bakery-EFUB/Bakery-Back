package bakery.caker.controller;

import bakery.caker.config.Authority;
import bakery.caker.dto.MemberRequestDTO;
import bakery.caker.dto.MemberResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.exception.CustomException;
import bakery.caker.exception.ErrorCode;
import bakery.caker.service.JwtTokenProvider;
import bakery.caker.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static bakery.caker.dto.MemberResponseDTO.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final HttpSession httpSession;
    private final JwtTokenProvider jwtTokenProvider;

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
    public MemberProfileResponseDTO memberModify(HttpServletRequest httpRequest, @RequestBody MemberRequestDTO requestDTO){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        return memberService.modifySessionMember(sessionUser.getMemberId(), requestDTO);
    }

    @PatchMapping("/account/profile/image")
    public MemberProfileResponseDTO memberModify(HttpServletRequest httpRequest, @RequestPart(value="image")MultipartFile file) {

        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        return memberService.modifySessionMemberImage(sessionUser.getMemberId(), file);
    }

    @GetMapping("/signup/baker")
    public void roleModify(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) throws IOException {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        if(sessionUser.getAuthority().equals(Authority.CLIENT.getValue())) {
            memberService.modifyRole(sessionUser.getMemberId());
            httpServletResponse.sendRedirect("https://dev.caker.shop/oauth2/authorization/kakao");
        }
        else {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "이미 사장님으로 가입된 유저입니다.");
        }
    }

    @DeleteMapping("/account")
    public String memberDelete(HttpServletRequest httpRequest) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        return memberService.deleteSessionMember(sessionUser.getMemberId());
    }
}
