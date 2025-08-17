package wooribe.zarit.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class Top5CafeResponseDto {
    private final String name;
    private final List<CafeSummaryDto> pin;

    public Top5CafeResponseDto(String name, List<CafeSummaryDto> pin) {
        this.name = name;
        this.pin = pin;
    }
}
