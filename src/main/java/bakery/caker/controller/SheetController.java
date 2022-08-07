package bakery.caker.controller;

import bakery.caker.dto.CommentDTO;
import bakery.caker.dto.SheetDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.CommentService;
import bakery.caker.service.JwtTokenProvider;
import bakery.caker.service.SheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class SheetController {
    private final SheetService sheetService;
    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping()
    public ResponseEntity<?> allOrderList(){
        return new ResponseEntity<>(sheetService.findOrders(), HttpStatus.OK);
    }

    @GetMapping("/{loc_gu}/{loc_dong}")
    public ResponseEntity<?> filteredOrderList(@PathVariable("loc_gu") String locGu, @PathVariable("loc_dong") String locDong){
        return new ResponseEntity<>(sheetService.findLocOrders(locGu, locDong), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> filteredOrderListByType(@PathVariable("type") String type){
        return new ResponseEntity<>(sheetService.findOrdersByType(type), HttpStatus.OK);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<?> orderDetails(@PathVariable("order_id") Long orderId){
        return new ResponseEntity<>(sheetService.findOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/myPin") //baker만 접근 가능
    public ResponseEntity<?> commentOrderList(HttpServletRequest httpRequest){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);

        return new ResponseEntity<>(sheetService.findOrdersByComment(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @GetMapping("/myOrder")
    public ResponseEntity<?> myOrderList(HttpServletRequest httpRequest){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);

        return new ResponseEntity<>(sheetService.findMyOrders(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> orderAdd(HttpServletRequest httpRequest, @RequestBody SheetDTO sheetDTO){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        return new ResponseEntity<>(sheetService.addOrder(sessionUser.getMemberId(), sheetDTO), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "https://caker.shop, https://bakery-front-rho.vercel.app/, http://localhost:3000")
    @PatchMapping()
    public ResponseEntity<?> orderImageModify(HttpServletRequest httpRequest, @RequestParam("orderId") Long orderId, @RequestParam(value="file", required = false) MultipartFile file){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        sheetService.modifyImage(sessionUser.getMemberId(), orderId, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> orderRemove(HttpServletRequest httpRequest, @PathVariable("order_id") Long orderId){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        sheetService.orderWriterCheck(sessionUser, orderId);

        sheetService.removeOrder(sessionUser.getMemberId(), orderId);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

    @PatchMapping("/{order_id}")
    public ResponseEntity<?> orderModify(HttpServletRequest httpRequest, @PathVariable("order_id") Long orderId, @RequestBody SheetDTO sheetDTO){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        sheetService.orderWriterCheck(sessionUser, orderId);

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
    public ResponseEntity<?> commentAdd(HttpServletRequest httpRequest, @PathVariable("order_id") Long orderId, @RequestBody CommentDTO commentDTO){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        commentService.addComment(sessionUser.getMemberId(), orderId, commentDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}/comments/{comment_id}")
    public ResponseEntity<?> commentRemove(HttpServletRequest httpRequest, @PathVariable("comment_id") Long commentId){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        commentService.commentWriterCheck(sessionUser, commentId);
        commentService.removeComment(sessionUser.getMemberId(), commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{order_id}/comments/{comment_id}/recomments")
    public ResponseEntity<?> recommentAdd(HttpServletRequest httpRequest, @PathVariable("comment_id") Long commentId, @RequestBody CommentDTO commentDTO){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        commentService.addRecomment(sessionUser.getMemberId(), commentId, commentDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}/comments/{comment_id}/recomments/{recomment_id}")
    public ResponseEntity<?> recommentRemove(HttpServletRequest httpRequest, @PathVariable("recomment_id") Long recommentId){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        commentService.recommentWriterCheck(sessionUser, recommentId);
        commentService.removeRecomment(sessionUser.getMemberId(), recommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
