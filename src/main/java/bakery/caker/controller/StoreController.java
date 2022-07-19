package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.domain.Member;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.dto.StoreResponseDTO;
import bakery.caker.repository.MemberRepository;
import bakery.caker.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class StoreController {
    private StoreService storeService;
    private MemberRepository memberRepository;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/stores")
    ResponseEntity<?> getStoreList() {
        return new ResponseEntity<>(storeService.getStoreList(), HttpStatus.OK);
    }


    @GetMapping("/stores/recomends")
    ResponseEntity<?> getStoreRecomendList() {
        return new ResponseEntity<>(storeService.getStoreRecomendList(), HttpStatus.OK);
    }

    @GetMapping("/stores/search")
    ResponseEntity<?> getMemberById(@RequestParam(name = "keyword") String q){
      return new ResponseEntity<>(storeService.selectStoreByQuery(q), HttpStatus.OK);
   }

    @GetMapping("/stores/{store_id}")
    ResponseEntity<?> getStoreDetail(@PathVariable("store_id") Long store_id) {
        return new ResponseEntity<>(storeService.getStoreDetail(store_id), HttpStatus.OK);
    }

    @GetMapping("/stores/myStore")
    ResponseEntity<?> getMyStoreDetail(@LoginUser SessionUserDTO sessionUser) {
        return new ResponseEntity<>(storeService.getStoreDetailByOwner(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @PostMapping("/stores/myStore")
    ResponseEntity<?> writeMyStore(@LoginUser SessionUserDTO sessionUser, @RequestPart StoreResponseDTO storedata, @RequestPart MultipartFile mainImg, @RequestPart List<MultipartFile> menuImg) throws IOException {
        return new ResponseEntity<>(storeService.saveStore(sessionUser.getMemberId(), storedata, mainImg, menuImg), HttpStatus.OK);
    }
}