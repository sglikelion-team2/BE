package wooribe.zarit.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AiRequestDto {
    private String model;
    private List<Message> messages;

    @Getter
    @AllArgsConstructor
    public static class Message {
        private String role;
        private List<Content> content;
    }

    public interface Content { // Marker interface
    }

    @Getter
    @AllArgsConstructor
    public static class TextContent implements Content {
        private String type;
        private String text;
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUrlContent implements Content {
        private String type;
        @JsonProperty("image_url")
        private ImageUrl imageUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUrl {
        private String url;
    }
}
