package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.CommentDTO;
import bakery.caker.dto.OrderDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.CommentService;
import bakery.caker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CommentService commentService;

    @GetMapping()
    public ResponseEntity<?> getOrders(){
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/{loc_gu}/{loc_dong}")
    public ResponseEntity<?> getFilteredOrders(@PathVariable("loc_gu") String locGu, @PathVariable("loc_dong") String locDong){
        return new ResponseEntity<>(orderService.getLocOrders(locGu, locDong), HttpStatus.OK);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<?> getOrder(@PathVariable("order_id") Long orderId){
        return new ResponseEntity<>(orderService.getOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/myPin")
    public ResponseEntity<?> getCommentOrders(@LoginUser SessionUserDTO sessionUser){
        return new ResponseEntity<>(orderService.getOrdersByComment(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @GetMapping("/myOrder")
    public ResponseEntity<?> getMyOrders(@LoginUser SessionUserDTO sessionUser){
        return new ResponseEntity<>(orderService.getMyOrders(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(@LoginUser SessionUserDTO sessionUser, @RequestBody OrderDTO orderDTO){
        orderService.saveOrder(sessionUser.getMemberId(), orderDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> deleteOrder(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId){
        orderService.deleteOrder(sessionUser.getMemberId(), orderId);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

    @PatchMapping("/{order_id}")
    public ResponseEntity<?> updateOrder(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId, @RequestBody OrderDTO orderDTO){
        orderService.updateOrder(sessionUser.getMemberId(), orderId, orderDTO);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @GetMapping("/newOrder")
    public ResponseEntity<?> getNewOrders(){
        return new ResponseEntity<>(orderService.getOrdersByCreatedAt(), HttpStatus.OK);
    }

    @GetMapping("/{order_id}/comments")
    public ResponseEntity<?> getComments(@PathVariable("order_id") Long orderId){
        return new ResponseEntity<>(commentService.getComments(orderId), HttpStatus.OK);
    }

    @PostMapping("/{order_id}/comments")
    public ResponseEntity<?> createComment(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId, @RequestBody CommentDTO commentDTO){
        commentService.createComment(sessionUser.getMemberId(), orderId, commentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}/comments/{comment_id}")
    public ResponseEntity<?> deleteComment(@LoginUser SessionUserDTO sessionUser, @PathVariable("comment_id") Long commentId){
        commentService.deleteComment(sessionUser.getMemberId(), commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{order_id}/comments/{comment_id}/recomments")
    public ResponseEntity<?> createRecomment(@LoginUser SessionUserDTO sessionUser, @PathVariable("comment_id") Long commentId, @RequestBody CommentDTO commentDTO){
        commentService.createRecomment(sessionUser.getMemberId(), commentId, commentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}/comments/{comment_id}/recomments/{recomment_id}")
    public ResponseEntity<?> deleteRecomment(@LoginUser SessionUserDTO sessionUser, @PathVariable("recomment_id") Long recommentId){
        commentService.deleteRecomment(sessionUser.getMemberId(), recommentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
