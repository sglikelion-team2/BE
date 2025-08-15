package wooribe.zarit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse <T>{
    private boolean success;
    private int code;
    private String message;
    private T result;

    public ApiResponse(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
