package neoinfos.pms.service;

import neoinfos.pms.dto.CategoryRequest;
import neoinfos.pms.dto.CategoryResponse;
import neoinfos.pms.dto.ProductMasterRequest;
import neoinfos.pms.entity.Category;

/**
 * packageName    : neoinfos.pms.service
 * fileName       : ProductMasterServcie
 * author         : JAEIK
 * date           : 8/3/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/3/26        JAEIK       최초 생성
 */
public interface ProductMasterService {
    CategoryResponse createProduct(CategoryRequest categoryRequest);
}
