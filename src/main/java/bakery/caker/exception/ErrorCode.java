package bakery.caker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400 BAD_REQUEST : 잘못된 요청
    CANNOT_EMPTY_CONTENT(BAD_REQUEST, "내용이 비어있을 수 없습니다."),
    INVALID_VALUE(BAD_REQUEST, "올바르지 않은 값입니다."),
    INVALID_IMAGE_FILE(BAD_REQUEST, "잘못된 이미지 파일입니다."),
    INVALID_SESSION_USER(BAD_REQUEST, "세션 유저가 비어있습니다."),

    //401 UNAUTHORIZED : 비인증 상태
    USER_UNAUTHORIZED(UNAUTHORIZED, "로그인이 필요합니다."),

    //403 FORBIDDEN : 권한 없음
    ACCESS_DENIED(FORBIDDEN, "해당 페이지를 요청할 권한이 없습니다."),

    //404 NOT_FOUND : Resource를 찾을 수 없음
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 멤버 정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(NOT_FOUND, "해당 제안서 정보를 찾을 수 없습니다."),
    STORE_NOT_FOUND(NOT_FOUND, "해당 가게 정보를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글 정보를 찾을 수 없습니다."),
    EVENT_NOT_FOUND(NOT_FOUND, "해당 예약 정보를 찾을 수 없습니다."),

    //500 INTERNAL_SERVER_ERROR : 서버 내 문제
    EXCEPTION(INTERNAL_SERVER_ERROR, "서버 내에 알 수 없는 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}
