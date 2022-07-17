package bakery.caker.service;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Order;
import bakery.caker.domain.Recomment;
import bakery.caker.dto.OrderDTO;
import bakery.caker.dto.OrderResponseDTO;
import bakery.caker.repository.CommentRepository;
import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.OrderRepository;
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
public class OrderService {
    private final OrderRepository orderRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;
    private final MemberRepository memberRepository;

    //새로운 order 저장
    @Transactional
    public void saveOrder(Long memberId, OrderDTO order){
        memberRepository.findById(memberId).ifPresent(
                member -> orderRepository.save(order.toEntity(member)));
    }

    //order pickupDate 업데이트
    @Transactional
    public void updateOrder(Long memberId, Long orderId, OrderDTO order){
        orderRepository.findById(orderId).ifPresent(
                o -> {
                    if(o.getMember().getMemberId().equals(memberId)){
                        o.updatePickup(order.getPickupDate());
                        orderRepository.save(o);
                    }
                }
        );
    }

    //order 사용 완료
    @Transactional
    public void deleteOrder(Long memberId, Long orderId){
        orderRepository.findById(orderId).ifPresent(
                o -> {
                    if(o.getMember().getMemberId().equals(memberId)){
                        o.updateFinishedFlag();
                        orderRepository.save(o);
                    }
                }
        );
    }

    //처리되지 않은 모든 order 읽어오기
    public OrderResponseDTO getOrders(){
        return new OrderResponseDTO(orderRepository.findAllByFinishedFlag(false));
    }

    //처리되지 않은 location 별 order 읽어오기
    public OrderResponseDTO getLocOrders(String locationGu, String locationDong){
        return new OrderResponseDTO(orderRepository.findAllByLocationGuAndLocationDongAndFinishedFlag(locationGu, locationDong, false));
    }

    //특정 order 가져오기, 없는 경우 null return
    public Order getOrder(Long orderId){
        AtomicReference<Order> order = new AtomicReference<>();
        orderRepository.findById(orderId).ifPresent(
                order::set);
        return order.get();
    }

    //최근 6개 order 보여주기
    public OrderResponseDTO getOrdersByCreatedAt(){
        List<Order> orders = orderRepository.findAllByFinishedFlagOrderByCreatedAtDesc(false);
        return new OrderResponseDTO(orders.subList(0,6));
    }

    //내가 댓글 단 제안서
    public OrderResponseDTO getOrdersByComment(Long memberId){
        List<Comment> comments = new java.util.ArrayList<>(Collections.emptyList());
        List<Recomment> recomments = new java.util.ArrayList<>(Collections.emptyList());
        List<Order> orders = new java.util.ArrayList<>(Collections.emptyList());
        memberRepository.findById(memberId).ifPresent(
                member -> {
                    comments.addAll(commentRepository.findAllByWriter(member));
                    recomments.addAll(recommentRepository.findAllByWriter(member));
                }
        );

        for(Comment comment : comments){
            orders.add(comment.getOrder());
        }
        for(Recomment recomment : recomments){
            orders.add(recomment.getComment().getOrder());
        }

        return new OrderResponseDTO(orders);
    }

    //내가 작성한 제안서 조회
    public OrderResponseDTO getMyOrders(Long memberId){
        List<Order> orders = new java.util.ArrayList<>(Collections.emptyList());
        memberRepository.findById(memberId).ifPresent(
                member -> orders.addAll(orderRepository.findAllByMember(member)));
        return new OrderResponseDTO(orders);
    }
}