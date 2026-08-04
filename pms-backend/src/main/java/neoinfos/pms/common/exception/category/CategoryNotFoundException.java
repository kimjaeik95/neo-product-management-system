package neoinfos.pms.common.exception.category;

import neoinfos.pms.common.exception.CustomException;
import neoinfos.pms.common.exception.ErrorCode;

/**
 * packageName    : neoinfos.pms.common.exception
 * fileName       : CategoryNotFountException
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
public class CategoryNotFoundException extends CustomException {
    public CategoryNotFoundException(Long categoryNo) {
        super(ErrorCode.CATEGORY_NOT_FOUND, "제품분류가 존재하지 않습니다 : " + categoryNo);
    }

    public CategoryNotFoundException(String message) {
        super(ErrorCode.CATEGORY_NOT_FOUND, message);
    }
}
