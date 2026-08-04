package neoinfos.pms.service;

import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.dto.ProductMasterResponse;
import neoinfos.pms.entity.Category;

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
}
