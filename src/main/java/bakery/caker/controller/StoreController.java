package bakery.caker.controller;

import bakery.caker.config.Authority;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.dto.StoreResponseDTO;
import bakery.caker.exception.CustomException;
import bakery.caker.exception.ErrorCode;
import bakery.caker.service.JwtTokenProvider;
import bakery.caker.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/stores")
    ResponseEntity<?> storeList() {
        return new ResponseEntity<>(storeService.getStoreList(), HttpStatus.OK);
    }

    @GetMapping("/stores/recommends")
    ResponseEntity<?> storeRecomendList() {
        return new ResponseEntity<>(storeService.getStoreRecomendList(), HttpStatus.OK);
    }

    @GetMapping("/stores/search")
    ResponseEntity<?> memberById(@RequestParam(name = "keyword") String q){
      return new ResponseEntity<>(storeService.selectStoreByQuery(q), HttpStatus.OK);
   }

    @GetMapping("/stores/{storeId}")
    ResponseEntity<?> storeDetail(@PathVariable("storeId") Long storeId) {
        return new ResponseEntity<>(storeService.getStoreDetail(storeId), HttpStatus.OK);
    }

    @GetMapping("/stores/myStore")
    ResponseEntity<?> myStoreDetail(HttpServletRequest httpRequest) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);
        return new ResponseEntity<>(storeService.getStoreDetailByOwner(sessionUser.getMemberId()), HttpStatus.OK);
    }

    @CrossOrigin(origins = "https://bakery-front-j4r1jvyhh-bakeryshop.vercel.app/, http://localhost:3000")
    @PostMapping("/stores/myStore")
    ResponseEntity<?> myStoreUpdate(HttpServletRequest httpRequest,  @RequestBody StoreResponseDTO storedata) throws IOException {

        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);

        if(sessionUser.getAuthority().equals(Authority.TRAINEE)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, null);
        }
        else {
            return new ResponseEntity<>(storeService.saveStore(sessionUser.getMemberId(), storedata), HttpStatus.OK);
        }
    }


    @CrossOrigin(origins = "https://bakery-front-j4r1jvyhh-bakeryshop.vercel.app/, http://localhost:3000")
    @PatchMapping("/stores/myStore/image")
    ResponseEntity<?> myStoreImageUpdate(HttpServletRequest httpRequest, @RequestParam("storeId") Long storeId, @RequestParam(value="mainImg", required = false) MultipartFile mainImg, @RequestParam(value="menuImg", required = false) List<MultipartFile> menuImg) throws IOException {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(httpRequest);

        if(sessionUser.getAuthority().equals(Authority.TRAINEE)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, null);
        }
        else {
            return new ResponseEntity<>(storeService.updateStore(storeId, mainImg, menuImg), HttpStatus.OK);
        }
    }
}