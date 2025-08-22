package tech.igor.online_courses_platform.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCollectionResponse(

        @JsonProperty("id") String id
) {
}
