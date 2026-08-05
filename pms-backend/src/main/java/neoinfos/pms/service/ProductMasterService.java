package neoinfos.pms.service;

import neoinfos.pms.dto.ProductMasterListProjection;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.dto.ProductMasterUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * packageName    : neoinfos.pms.service
 * fileName       : ProductMasterService
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
public interface ProductMasterService {
    ProductMasterResponse createProductMaster(ProductMasterRequest productMasterRequest);
    Page<ProductMasterListProjection> findALLProductMasters(Pageable pageable);

    ProductMasterResponse findProductMasterById(Long productNo);

    ProductMasterResponse updateProductMasterById(Long productNo, ProductMasterUpdateRequest updateRequest);

    void softDeleteById(Long productNo);
}
