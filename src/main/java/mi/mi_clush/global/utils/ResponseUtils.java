package mi.mi_clush.global.utils;

import mi.mi_clush.global.dto.ApiResponse;
import mi.mi_clush.global.enums.ErrorCode;
import mi.mi_clush.global.enums.SuccessCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ResponseUtils {

    public static <T> ApiResponse<T> success(T response) {
        return new ApiResponse<>(200, "OK", response);
    }

    public static ApiResponse<?> success(SuccessCode successCode) {
        int statusCode = successCode.getHttpStatus().value();
        String msg = successCode.getDetail();
        return new ApiResponse<>(statusCode, msg, null);
    }

    public static <T> ApiResponse<?> success(SuccessCode successCode, T response) {
        int statusCode = successCode.getHttpStatus().value();
        String msg = successCode.getDetail();
        return new ApiResponse<>(statusCode, msg, response);
    }

    public static <T> ApiResponse<T> error(T response) {
        return new ApiResponse<>(500, "ERROR", response);
    }

    public static ApiResponse<?> error(ErrorCode errorCode) {
        int statusCode = errorCode.getHttpStatus().value();
        String msg = errorCode.getDetail();
        return new ApiResponse<>(statusCode, msg, null);
    }

    public static ApiResponse<?> error(HttpStatus httpStatus, Map<String, String>  errors) {
        return new ApiResponse<>(httpStatus.value(), null, errors);
    }
}
