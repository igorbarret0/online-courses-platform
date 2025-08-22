package tech.igor.online_courses_platform.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAssetRequest(
        @JsonProperty("input") String s3Url,
        @JsonProperty("collection_id") String collectionId,
        @JsonProperty("format") String format
) {
}
