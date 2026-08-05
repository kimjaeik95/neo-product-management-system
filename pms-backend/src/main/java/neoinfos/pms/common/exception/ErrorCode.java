package neoinfos.pms.common.exception;

import org.springframework.http.HttpStatus;

/**
 * packageName    : neoinfos.pms.common.exception
 * fileName       : ErrorCode
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
public enum ErrorCode {
    DUPLICATE_CATEGORY_CODE("중복된 제품분류 코드입니다.", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND("제품분류가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    CATEGORY_USED("사용중인 제품분류 입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_PRODUCT_MASTER_CODE("중복된 제품마스터 코드입니다.", HttpStatus.CONFLICT),
    PRODUCT_MASTER_NOT_FOUND("제품마스터가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_INPUT_VALUE("잘못된 입력값입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    HANDLE_ACCESS_DENIED("존재하지 않는 API 경로입니다.", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
