package mi.mi_clush.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import mi.mi_clush.global.dto.ApiResponse;
import mi.mi_clush.global.utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j(topic = "Global Exception Handler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseUtils.error(e.getMessage());
    }

    @ExceptionHandler(GlobalException.class)
    public ApiResponse<?> handleGlobalException(GlobalException e) {
        log.error(e.getErrorCode().getDetail());
        return ResponseUtils.error(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(
                        error.getField(), error.getDefaultMessage()
                ));
        return ResponseUtils.error(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ApiResponse<?> jsonProcessingExceptionHandler(JsonProcessingException e) {
        log.error(e.getMessage(), e);
        return ResponseUtils.error(e.getMessage());
    }
}
