package neoinfos.pms.service;

import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    Page<ProductMasterResponse> findALLProductMasters(Pageable pageable);

    ProductMasterResponse findProductMasterById(Long productNo);
}
