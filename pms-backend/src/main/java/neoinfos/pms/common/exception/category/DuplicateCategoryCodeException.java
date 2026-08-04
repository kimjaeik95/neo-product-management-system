package neoinfos.pms.common.exception.category;

import neoinfos.pms.common.exception.CustomException;
import neoinfos.pms.common.exception.ErrorCode;

/**
 * packageName    : neoinfos.pms.common.exception
 * fileName       : DuplicateCategoryCode
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
public class DuplicateCategoryCodeException extends CustomException {
    public DuplicateCategoryCodeException() {
        super(ErrorCode.DUPLICATE_CATEGORY_CODE);
    }

    public DuplicateCategoryCodeException(String message) {
        super(ErrorCode.DUPLICATE_CATEGORY_CODE, message);
    }
}
