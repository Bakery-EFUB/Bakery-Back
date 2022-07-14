package bakery.caker.service;

import bakery.caker.domain.Member;
import bakery.caker.dto.MemberResponseDTO;
import bakery.caker.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDTO findSessionMember(Long memberId) {
        Member member = memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다 id= "+memberId));
        return new MemberResponseDTO(member);
    }
}
