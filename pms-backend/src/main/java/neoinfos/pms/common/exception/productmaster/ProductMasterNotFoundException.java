package neoinfos.pms.common.exception.productmaster;

import neoinfos.pms.common.exception.CustomException;
import neoinfos.pms.common.exception.ErrorCode;

/**
 * packageName    : neoinfos.pms.common.exception.productmaster
 * fileName       : ProductMasterNotFoundException
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
public class ProductMasterNotFoundException extends CustomException {
    public ProductMasterNotFoundException(Long categoryNo) {
        super(ErrorCode.PRODUCT_MASTER_NOT_FOUND, "제품마스터가 존재하지 않습니다 : " + categoryNo);
    }

    public ProductMasterNotFoundException(String message) {
        super(ErrorCode.PRODUCT_MASTER_NOT_FOUND, message);
    }
}

