package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.OrderDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    public final OrderService orderService;

    @GetMapping()
    ResponseEntity<?> getOrders(){
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/{loc_gu}/{loc_dong}")
    ResponseEntity<?> getFilteredOrders(@PathVariable("loc_gu") String locGu, @PathVariable("loc_dong") String locDong){
        return new ResponseEntity<>(orderService.getLocOrders(locGu, locDong), HttpStatus.OK);
    }

    @GetMapping("/{order_id}")
    ResponseEntity<?> getOrder(@PathVariable("order_id") Long orderId){
        return new ResponseEntity<>(orderService.getOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/myPin")
    ResponseEntity<?> getCommentOrders(@LoginUser SessionUserDTO sessionUser){
        return new ResponseEntity<>(orderService.getOrdersByComment(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @GetMapping("/myOrder")
    ResponseEntity<?> getMyOrders(@LoginUser SessionUserDTO sessionUser){
        return new ResponseEntity<>(orderService.getMyOrders(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<?> createOrder(@LoginUser SessionUserDTO sessionUser, @RequestBody OrderDTO orderDTO){
        orderService.saveOrder(sessionUser.getMemberId(), orderDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{order_id}")
    ResponseEntity<?> deleteOrder(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId){
        orderService.deleteOrder(sessionUser.getMemberId(), orderId);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

    @PatchMapping("/{order_id}")
    ResponseEntity<?> updateOrder(@LoginUser SessionUserDTO sessionUser, @PathVariable("order_id") Long orderId, @RequestBody OrderDTO orderDTO){
        orderService.updateOrder(sessionUser.getMemberId(), orderId, orderDTO);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @GetMapping("/newOrder")
    ResponseEntity<?> getNewOrders(){
        return new ResponseEntity<>(orderService.getOrdersByCreatedAt(), HttpStatus.OK);
    }
}
