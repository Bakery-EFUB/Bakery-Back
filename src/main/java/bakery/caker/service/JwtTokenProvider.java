package bakery.caker.service;

import bakery.caker.domain.Member;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.repository.MemberRepository;
import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final MemberRepository memberRepository;

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final long ACCESS_TOKEN_VALID_TIME = 3 * 60 * 60 * 1000L; //3시간
    //private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT token 생성
    public String createJwtAccessToken(String userPk, String roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date(); // 현재 시간 -> 유효기간 확인을 위함
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    // token 가공해서 정보 추출
    public Authentication getAuthentication(String token) {
        Member member = memberRepository.findMemberByKakaoId(Long.parseLong(getUserPk(token)));
        UserDetails sessionUserDTO = SessionUserDTO.builder().member(member).build();
        return new UsernamePasswordAuthenticationToken(sessionUserDTO, "", sessionUserDTO.getAuthorities());
    }


    public String getUserPk(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // HTTP 요청 안에서 헤더 찾아서 토큰 가져옴
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date()); // 유효하면 return
        } catch (Exception e){
            return false; //유효하지 않은 경우
        }
    }

    public SessionUserDTO getUserInfoByToken(HttpServletRequest request) {
        String token = resolveToken(request);
        if(validateToken(token)) {
            Member member = memberRepository.findMemberByKakaoId(Long.parseLong(getUserPk(token)));
            return new SessionUserDTO(member);
        }
        else {
            return null;
        }
    }
}
