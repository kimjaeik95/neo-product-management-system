package neoinfos.pms.common.exception.category;

import neoinfos.pms.common.exception.CustomException;
import neoinfos.pms.common.exception.ErrorCode;

/**
 * packageName    : neoinfos.pms.common.exception.category
 * fileName       : CategoryUsedExcetprion
 * author         : JAEIK
 * date           : 8/5/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/5/26        JAEIK       최초 생성
 */
public class CategoryUsedException extends CustomException {

    public CategoryUsedException() {
        super(ErrorCode.CATEGORY_USED);
    }

    public CategoryUsedException(String message) {
        super(ErrorCode.CATEGORY_USED, message);
    }
}
