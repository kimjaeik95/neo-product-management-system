package neoinfos.pms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.dto.ProductMasterUpdateRequest;
import neoinfos.pms.service.ProductMasterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/product-masters")
    public ResponseEntity<Page<ProductMasterResponse>> getProductMasters(@PageableDefault(size = 20) Pageable pageable) {
        Page<ProductMasterResponse> productMasters = productMasterService.findALLProductMasters(pageable);
        return ResponseEntity.ok().body(productMasters);
    }

    @GetMapping("/product-masters/{productNo}")
    public ResponseEntity<ProductMasterResponse> getProductMaster(@PathVariable("productNo") Long productNo) {
        ProductMasterResponse productMasterById = productMasterService.findProductMasterById(productNo);
        return ResponseEntity.ok().body(productMasterById);
    }

    @PutMapping("/product-masters/{productNo}")
    public ResponseEntity<ProductMasterResponse> updateProductMasterById(@PathVariable("productNo") Long productNo,@Valid @RequestBody ProductMasterUpdateRequest updateRequest) {
        ProductMasterResponse productMasterResponse = productMasterService.updateProductMasterById(productNo, updateRequest);
        return ResponseEntity.ok().body(productMasterResponse);
    }

    @DeleteMapping("/product-masters/{productNo}")
    public ResponseEntity<Void> deleteProductMasterById(@PathVariable("productNo") Long productNo) {
        productMasterService.softDeleteById(productNo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
