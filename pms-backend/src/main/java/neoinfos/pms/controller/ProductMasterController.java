package neoinfos.pms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.service.ProductMasterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : neoinfos.pms.controller
 * fileName       : ProductMasterController
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductMasterController {
    private final ProductMasterService productMasterService;
    @PostMapping("/product-masters")
    public ResponseEntity<ProductMasterResponse> saveProductMaster(@Valid @RequestBody ProductMasterRequest request) {
        ProductMasterResponse response = productMasterService.createProductMaster(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
