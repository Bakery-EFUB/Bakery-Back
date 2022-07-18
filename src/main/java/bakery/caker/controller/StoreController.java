package bakery.caker.controller;

import bakery.caker.config.LoginUser;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.dto.StoreResponseDTO;
import bakery.caker.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class StoreController {
    private StoreService storeService;

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
    public List<?> getMemberById(@RequestParam String q){
      return storeService.selectStoreByQuery(q);
   }

    @GetMapping("/stores/{store_id}")
    ResponseEntity<?> getStoreDetail(@PathVariable("store_id") Long store_id) {
        storeService.getStoreDetail(store_id);
        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/stores/myStore")
    ResponseEntity<?> getMyStoreDetail(@LoginUser SessionUserDTO sessionUser) {
        storeService.getStoreDetailByOwner(sessionUser.getMemberId());
        return new ResponseEntity<>("successfully deleted", HttpStatus.OK);
    }

    @PostMapping("/stores/myStore")
    ResponseEntity<?> writeMyStore(@RequestPart StoreResponseDTO storedata, @RequestPart MultipartFile mainImg, @RequestPart List<MultipartFile> menuImg) throws IOException {
        return new ResponseEntity<>(storeService.saveStore(storedata, mainImg, menuImg), HttpStatus.OK);
    }
}