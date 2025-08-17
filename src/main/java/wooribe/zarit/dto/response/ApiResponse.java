package wooribe.zarit.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "isSuccess", "code", "message", "result" })
public class ApiResponse <T>{

    @JsonProperty("isSuccess")
    private Boolean isSuccess;

    private int code;
    private String message;
    private T result;

    public ApiResponse (boolean success, int code, String message) {
        this.isSuccess = success;
        this.code = code;
        this.message = message;
    }
}
