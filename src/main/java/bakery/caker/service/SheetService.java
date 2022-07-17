package bakery.caker.service;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Sheet;
import bakery.caker.domain.Recomment;
import bakery.caker.dto.SheetDTO;
import bakery.caker.dto.SheetOrderResponseDTO;
import bakery.caker.repository.CommentRepository;
import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.SheetRepository;
import bakery.caker.repository.RecommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class SheetService {
    private final SheetRepository sheetRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;
    private final MemberRepository memberRepository;

    //새로운 order 저장
    @Transactional
    public void saveOrder(Long memberId, SheetDTO order){
        memberRepository.findById(memberId).ifPresent(
                member -> sheetRepository.save(order.toEntity(member)));
    }

    //order pickupDate 업데이트
    @Transactional
    public void updateOrder(Long memberId, Long orderId, SheetDTO order){
        sheetRepository.findById(orderId).ifPresent(
                o -> {
                    if(o.getMember().getMemberId().equals(memberId)){
                        o.updatePickup(order.getPickupDate());
                        sheetRepository.save(o);
                    }
                }
        );
    }

    //order 사용 완료
    @Transactional
    public void deleteOrder(Long memberId, Long orderId){
        sheetRepository.findById(orderId).ifPresent(
                o -> {
                    if(o.getMember().getMemberId().equals(memberId)){
                        o.updateFinishedFlag();
                        sheetRepository.save(o);
                    }
                }
        );
    }

    //처리되지 않은 모든 order 읽어오기
    public SheetOrderResponseDTO getOrders(){
        return new SheetOrderResponseDTO(sheetRepository.findAllByFinishedFlag(false));
    }

    //처리되지 않은 location 별 order 읽어오기
    public SheetOrderResponseDTO getLocOrders(String locationGu, String locationDong){
        return new SheetOrderResponseDTO(sheetRepository.findAllByLocationGuAndLocationDongAndFinishedFlag(locationGu, locationDong, false));
    }

    //특정 order 가져오기, 없는 경우 null return
    public Sheet getOrder(Long orderId){
        AtomicReference<Sheet> order = new AtomicReference<>();
        sheetRepository.findById(orderId).ifPresent(
                order::set);
        return order.get();
    }

    //최근 6개 order 보여주기
    public SheetOrderResponseDTO getOrdersByCreatedAt(){
        List<Sheet> sheets = sheetRepository.findAllByFinishedFlagOrderByCreatedAtDesc(false);
        return new SheetOrderResponseDTO(sheets.subList(0,6));
    }

    //내가 댓글 단 제안서
    public SheetOrderResponseDTO getOrdersByComment(Long memberId){
        List<Comment> comments = new java.util.ArrayList<>(Collections.emptyList());
        List<Recomment> recomments = new java.util.ArrayList<>(Collections.emptyList());
        List<Sheet> sheets = new java.util.ArrayList<>(Collections.emptyList());
        memberRepository.findById(memberId).ifPresent(
                member -> {
                    comments.addAll(commentRepository.findAllByWriter(member));
                    recomments.addAll(recommentRepository.findAllByWriter(member));
                }
        );

        for(Comment comment : comments){
            sheets.add(comment.getSheet());
        }
        for(Recomment recomment : recomments){
            sheets.add(recomment.getComment().getSheet());
        }

        return new SheetOrderResponseDTO(sheets);
    }

    //내가 작성한 제안서 조회
    public SheetOrderResponseDTO getMyOrders(Long memberId){
        List<Sheet> sheets = new java.util.ArrayList<>(Collections.emptyList());
        memberRepository.findById(memberId).ifPresent(
                member -> sheets.addAll(sheetRepository.findAllByMember(member)));
        return new SheetOrderResponseDTO(sheets);
    }
}