package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.CommentDTO;
import bakery.caker.dto.SheetDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.CommentService;
import bakery.caker.service.SheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class SheetController {
    private final SheetService sheetService;
    private final CommentService commentService;

    @GetMapping()
    public ResponseEntity<?> getOrders(){
        return new ResponseEntity<>(sheetService.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/{loc_gu}/{loc_dong}")
    public ResponseEntity<?> getFilteredOrders(@PathVariable("loc_gu") String locGu, @PathVariable("loc_dong") String locDong){
        return new ResponseEntity<>(sheetService.getLocOrders(locGu, locDong), HttpStatus.OK);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<?> getOrder(@PathVariable("order_id") Long orderId){
        return new ResponseEntity<>(sheetService.getOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/myPin")
    public ResponseEntity<?> getCommentOrders(@LoginUser SessionUserDTO sessionUser){
        return new ResponseEntity<>(sheetService.getOrdersByComment(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @GetMapping("/myOrder")
    public ResponseEntity<?> getMyOrders(@LoginUser SessionUserDTO sessionUser){
        return new ResponseEntity<>(sheetService.getMyOrders(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(@LoginUser SessionUserDTO sessionUser, @RequestPart SheetDTO sheetDTO, @RequestPart MultipartFile file){
        sheetService.saveOrder(sessionUser.getMemberId(), sheetDTO, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> deleteOrder(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId){
        sheetService.deleteOrder(sessionUser.getMemberId(), orderId);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

    @PatchMapping("/{order_id}")
    public ResponseEntity<?> updateOrder(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId, @RequestBody SheetDTO sheetDTO){
        sheetService.updateOrder(sessionUser.getMemberId(), orderId, sheetDTO);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @GetMapping("/newOrder")
    public ResponseEntity<?> getNewOrders(){
        return new ResponseEntity<>(sheetService.getOrdersByCreatedAt(), HttpStatus.OK);
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
