package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.domain.Member;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.dto.StoreResponseDTO;
import bakery.caker.repository.MemberRepository;
import bakery.caker.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/stores")
    ResponseEntity<?> StoreList() {
        return new ResponseEntity<>(storeService.getStoreList(), HttpStatus.OK);
    }


    @GetMapping("/stores/recomends")
    ResponseEntity<?> StoreRecomendList() {
        return new ResponseEntity<>(storeService.getStoreRecomendList(), HttpStatus.OK);
    }

    @GetMapping("/stores/search")
    ResponseEntity<?> MemberById(@RequestParam(name = "keyword") String q){
      return new ResponseEntity<>(storeService.selectStoreByQuery(q), HttpStatus.OK);
   }

    @GetMapping("/stores/{storeId}")
    ResponseEntity<?> StoreDetail(@PathVariable("storeId") Long storeId) {
        return new ResponseEntity<>(storeService.getStoreDetail(storeId), HttpStatus.OK);
    }

    @GetMapping("/stores/myStore")
    ResponseEntity<?> MyStoreDetail(@LoginUser SessionUserDTO sessionUser) {
        return new ResponseEntity<>(storeService.getStoreDetailByOwner(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @PostMapping("/stores/myStore")
    ResponseEntity<?> MyStoreUpdate(@LoginUser SessionUserDTO sessionUser, @RequestPart StoreResponseDTO storedata, @RequestPart MultipartFile mainImg, @RequestPart List<MultipartFile> menuImg) throws IOException {
        return new ResponseEntity<>(storeService.saveStore(sessionUser.getMemberId(), storedata, mainImg, menuImg), HttpStatus.OK);
    }
}