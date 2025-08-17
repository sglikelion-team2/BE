package wooribe.zarit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import wooribe.zarit.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        // 오류 메시지 추출
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();

        // ApiResponse 객체를 원하는 형식으로 만들어서 반환
        ApiResponse apiResponse = new ApiResponse(false, 400, defaultMessage, null);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    // IllegalArgumentException 처리 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiResponse apiResponse = new ApiResponse(false, 400, e.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    // 사진 크기 초과
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        String errorMessage = "업로드할 수 있는 최대 파일 크기는 " + e.getMaxUploadSize() + "MB입니다.";
        ApiResponse apiResponse = new ApiResponse(false, 413, errorMessage, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}


