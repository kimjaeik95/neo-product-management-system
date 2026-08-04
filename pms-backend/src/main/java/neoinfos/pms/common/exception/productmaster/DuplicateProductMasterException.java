package neoinfos.pms.common.exception.productmaster;

import neoinfos.pms.common.exception.CustomException;
import neoinfos.pms.common.exception.ErrorCode;

/**
 * packageName    : neoinfos.pms.common.exception.productmaster
 * fileName       : DuplicateProductMasterException
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
public class DuplicateProductMasterException extends CustomException {
    public DuplicateProductMasterException() {
        super(ErrorCode.DUPLICATE_PRODUCT_MASTER_CODE);
    }

    public DuplicateProductMasterException(String message) {
        super(ErrorCode.DUPLICATE_PRODUCT_MASTER_CODE, message);
    }
}
