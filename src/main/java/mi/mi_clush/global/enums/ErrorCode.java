package mi.mi_clush.global.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST : 이 응답은 잘못된 문법으로 인해 서버가 요청을 이해할 수 없다는 의미입니다. */
    JWT_PREFIX_ERROR(BAD_REQUEST, "인증 식별자 형식이 잘못되었습니다."),
    USER_LOGIN_FAILURE(BAD_REQUEST, "로그인 실패하였습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않았다는 의미입니다. */
    UNAUTHORIZED_USER(UNAUTHORIZED, "인증에 실패하였습니다."),
    JWT_EXPIRATION(UNAUTHORIZED, "유효시간 만료, 재인증이 필요합니다."),


    /* 403 FORBIDDEN : 클라이언트가 콘텐츠에 접근할 권리를 가지고 있지 않다는 의미입니다.*/

    /* 404 NOT_FOUND : 서버는 요청 받은 리소스를 찾을 수 없다는 의미입니다. */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String detail;

    ErrorCode(final HttpStatus httpStatus, final String detail) {
        this.httpStatus = httpStatus;
        this.detail = detail;
    }
}
