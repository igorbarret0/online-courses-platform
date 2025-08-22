package tech.igor.online_courses_platform.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCollectionDto(
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("signed_url") boolean signedUrl,
        @JsonProperty("bucket_name") String bucketName,
        @JsonProperty("access_key") String accessKey,
        @JsonProperty("secret") String secret,
        @JsonProperty("region") String regin
) {
}
