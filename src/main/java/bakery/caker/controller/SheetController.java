package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.CommentDTO;
import bakery.caker.dto.SheetDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.CommentService;
import bakery.caker.service.SheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<?> allOrderList(){
        return new ResponseEntity<>(sheetService.findOrders(), HttpStatus.OK);
    }

    @GetMapping("/{loc_gu}/{loc_dong}")
    public ResponseEntity<?> filteredOrderList(@PathVariable("loc_gu") String locGu, @PathVariable("loc_dong") String locDong){
        return new ResponseEntity<>(sheetService.findLocOrders(locGu, locDong), HttpStatus.OK);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<?> orderDetails(@PathVariable("order_id") Long orderId){
        return new ResponseEntity<>(sheetService.findOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/myPin")
    public ResponseEntity<?> commentOrderList(@LoginUser SessionUserDTO sessionUser){
        return new ResponseEntity<>(sheetService.findOrdersByComment(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @GetMapping("/myOrder")
    public ResponseEntity<?> myOrderList(@LoginUser SessionUserDTO sessionUser){
        return new ResponseEntity<>(sheetService.findMyOrders(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> orderAdd(@LoginUser SessionUserDTO sessionUser, @RequestPart SheetDTO sheetDTO, @RequestPart MultipartFile file){
        sheetService.addOrder(sessionUser.getMemberId(), sheetDTO, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> orderRemove(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId){
        sheetService.removeOrder(sessionUser.getMemberId(), orderId);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

    @PatchMapping("/{order_id}")
    public ResponseEntity<?> orderModify(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId, @RequestBody SheetDTO sheetDTO){
        sheetService.modifyOrder(sessionUser.getMemberId(), orderId, sheetDTO);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @GetMapping("/newOrder")
    public ResponseEntity<?> newOrderList(){
        return new ResponseEntity<>(sheetService.findOrdersByCreatedAt(), HttpStatus.OK);
    }

    @GetMapping("/{order_id}/comments")
    public ResponseEntity<?> commentList(@PathVariable("order_id") Long orderId){
        return new ResponseEntity<>(commentService.findComments(orderId), HttpStatus.OK);
    }

    @PostMapping("/{order_id}/comments")
    public ResponseEntity<?> commentAdd(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId, @RequestBody CommentDTO commentDTO){
        commentService.addComment(sessionUser.getMemberId(), orderId, commentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}/comments/{comment_id}")
    public ResponseEntity<?> commentRemove(@LoginUser SessionUserDTO sessionUser, @PathVariable("comment_id") Long commentId){
        commentService.removeComment(sessionUser.getMemberId(), commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{order_id}/comments/{comment_id}/recomments")
    public ResponseEntity<?> recommentAdd(@LoginUser SessionUserDTO sessionUser, @PathVariable("comment_id") Long commentId, @RequestBody CommentDTO commentDTO){
        commentService.addRecomment(sessionUser.getMemberId(), commentId, commentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}/comments/{comment_id}/recomments/{recomment_id}")
    public ResponseEntity<?> recommentRemove(@LoginUser SessionUserDTO sessionUser, @PathVariable("recomment_id") Long recommentId){
        commentService.removeRecomment(sessionUser.getMemberId(), recommentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
